#include "bitmap_monitor.h"
#include "bitmap_saver.h"
#include "../common/native_util.h"
#include <jni.h>
#include <shadowhook.h>
#include <android/bitmap.h>

// =================================================================================
// Global Variables
// =================================================================================

static struct BitmapMonitorContext g_bitmap_monitor_ctx;

// =================================================================================
// Internal: check and report
// =================================================================================

static jobject create_bitmap_statistics_java_object(
        JNIEnv *env,
        int64_t remained_bitmap_count,
        long long remained_bitmap_size,
        jobjectArray java_bitmap_record_array
) {

    jobject bitmap_sum_obj = env->NewObject(
            g_bitmap_monitor_ctx.jclass_of_bitmap_statistics,
            g_bitmap_monitor_ctx.mid_bitmap_statistics_constructor,
            g_bitmap_monitor_ctx.total_bitmap_created_count,
            g_bitmap_monitor_ctx.total_bitmap_created_size,
            remained_bitmap_count,
            remained_bitmap_size,
            java_bitmap_record_array
    );

    return bitmap_sum_obj;
}

static void *checker_thread_routine(void *) {
    if (g_bitmap_monitor_ctx.java_vm == nullptr) {
        log_e("checker_thread_routine failed, java_vm is null!");
        return nullptr;
    }

    // 获取当前线程的 JNIEnv
    JNIEnv *env;
    jboolean result = g_bitmap_monitor_ctx.java_vm->AttachCurrentThread(&env, nullptr);
    if (result != JNI_OK) {
        log_e("checker_thread_routine failed, AttachCurrentThread failed!");
        return nullptr;
    }

    // 创建一个函数，用于线程睡眠
    auto sleep_some_time = []() {
        long interval = g_bitmap_monitor_ctx.check_recycle_interval;
        long sleep_time = interval >= 1 ? interval : BITMAP_RECORD_CHECK_THREAD_SLEEP_SECONDS;
        struct timeval tv{};
        tv.tv_sec = sleep_time;
        tv.tv_usec = 0;
        select(0, nullptr, nullptr, nullptr, &tv);
    };

    // 开始循环检查
    while (g_bitmap_monitor_ctx.hooked) {
        if (!g_bitmap_monitor_ctx.record_mutex.try_lock()) {
            // 获取失败，睡眠一会
            sleep_some_time();
            continue;
        }

        // 获取当前的记录
        auto bitmap_records = g_bitmap_monitor_ctx.bitmap_records;
        if (bitmap_records.empty()) {
            g_bitmap_monitor_ctx.record_mutex.unlock();
            sleep_some_time();
            continue;
        }


        // 把所有已经被回收的 Bitmap 移除
        long long sum_bytes_alloc = 0;
        std::vector<BitmapRecord> copy_records;
        for (auto &bitmap_record: bitmap_records) {

            // 是否被回收
            jboolean object_recycled = env->IsSameObject(bitmap_record.java_bitmap_ref, nullptr);
            if (object_recycled == JNI_TRUE) {
                log_i("checker_thread_routine, bitmap is recycled, remove it!");
                // 移除掉 GC 回收的
                // TODO: 释放 record 中的 global ref。
                continue;
            }

            jobject bitmap_local_ref = env->NewLocalRef(bitmap_record.java_bitmap_ref);
            jboolean is_recycled = env->CallBooleanMethod(bitmap_local_ref, g_bitmap_monitor_ctx.mid_bitmap_is_recycled);
            if (is_recycled == JNI_TRUE) {
                // 回收了
                env->DeleteLocalRef(bitmap_local_ref);
                log_i("checker_thread_routine, bitmap is recycled, remove it!");
                // TODO: 释放 record 中的 global ref。
                continue;
            }

            sum_bytes_alloc += (bitmap_record.height * bitmap_record.stride);
            copy_records.push_back(
                    {
                            .native_ptr = bitmap_record.native_ptr,
                            .width =  bitmap_record.width,
                            .height = bitmap_record.height,
                            .stride =  bitmap_record.stride,
                            .format = bitmap_record.format,
                            .time = bitmap_record.time,
                            .bitmap_store_path = bitmap_record.bitmap_store_path,
                            .java_bitmap_ref = bitmap_record.java_bitmap_ref,
                            .java_stack_trace = bitmap_record.java_stack_trace,
                            .creation_scene = bitmap_record.creation_scene,
                            .restore_succeed = bitmap_record.restore_succeed,
                    }
            );
            env->DeleteLocalRef(bitmap_local_ref);
        } // end of for

        // 更新记录
        // TODO: 这里将导致深拷贝，考虑后续优化。
        g_bitmap_monitor_ctx.bitmap_records = copy_records;

        // 释放锁
        g_bitmap_monitor_ctx.record_mutex.unlock();

        // 将统计信息上报给 Java 层（这里只上报了 Bitmap 的创建数量和总大小，详细信息由 Java 层主动获取）
        int64_t remained_bitmap_count = g_bitmap_monitor_ctx.bitmap_records.size();
        int64_t remained_bitmap_size = sum_bytes_alloc;
        jobject bitmap_statistics = create_bitmap_statistics_java_object(env, remained_bitmap_count, remained_bitmap_size, nullptr);
        env->CallVoidMethod(
                g_bitmap_monitor_ctx.jobject_bitmap_monitor,
                g_bitmap_monitor_ctx.mid_bitmap_monitor_report_statistics,
                bitmap_statistics
        );

        // 睡眠一会
        sleep_some_time();
    }

    // 释放 JNIEnv
    g_bitmap_monitor_ctx.java_vm->DetachCurrentThread();

    return nullptr;
}

static void start_loop_check_recycle_thread() {
    pthread_t thread;
    pthread_attr_t attr;
    pthread_attr_init(&attr);
    // 常量 PTHREAD_CREATE_DETACHED 表示分离，分离的线程不能被其他线程 join，也就是不能被等待，线程结束后会自动释放资源。
    pthread_attr_setdetachstate(&attr, PTHREAD_CREATE_DETACHED);
    pthread_create(&thread, &attr, checker_thread_routine, nullptr);
}

// =================================================================================
// Internal: dump statistics
// =================================================================================

static jobject dump_bitmap_statistics(JNIEnv *env, bool simple_way) {
    if (!g_bitmap_monitor_ctx.record_mutex.try_lock()) {
        log_w("dump_bitmap_statistics failed, record_mutex try_lock failed!");
        return nullptr;
    }

    auto bitmap_records = g_bitmap_monitor_ctx.bitmap_records;
    int64_t remained_bitmap_count = bitmap_records.size();
    long long remained_bitmap_size = 0;
    int index = 0;
    jobjectArray java_bitmap_record_array = nullptr;

    if (!simple_way) {
        java_bitmap_record_array = env->NewObjectArray(remained_bitmap_count, g_bitmap_monitor_ctx.jclass_of_bitmap_record, nullptr);
        for (auto record: bitmap_records) {
            remained_bitmap_size += record.height * record.stride;
            jobject java_record = env->NewObject(
                    g_bitmap_monitor_ctx.jclass_of_bitmap_record,
                    g_bitmap_monitor_ctx.mid_bitmap_record_constructor,
                    record.width,
                    record.height,
                    record.stride / record.width,
                    record.format,
                    record.time,
                    record.native_ptr,
                    record.bitmap_store_path,
                    record.java_stack_trace,
                    record.creation_scene
            );
            env->SetObjectArrayElement(java_bitmap_record_array, index, java_record);
            index++;
        }
    }

    g_bitmap_monitor_ctx.record_mutex.unlock();

    return create_bitmap_statistics_java_object(
            env,
            remained_bitmap_count,
            remained_bitmap_size,
            java_bitmap_record_array
    );
}

// =================================================================================
// Internal: hook and record
// =================================================================================

static jstring dump_java_stack() {
    if (!g_bitmap_monitor_ctx.initialized || g_bitmap_monitor_ctx.mid_bitmap_monitor_dump_stack == nullptr) {
        log_w("dump_java_stack skipped, not initialized or mid_bitmap_monitor_dump_stack is null!");
        return nullptr;
    }

    JNIEnv *env;
    g_bitmap_monitor_ctx.java_vm->AttachCurrentThread(&env, nullptr);
    auto stacks = env->CallObjectMethod(
            g_bitmap_monitor_ctx.jobject_bitmap_monitor,
            g_bitmap_monitor_ctx.mid_bitmap_monitor_dump_stack
    );
    if (stacks == nullptr) {
        log_w("call dump_java_stack failed, return null!");
        return nullptr;
    }
    return static_cast<jstring>(stacks);
}

static jstring get_current_scene() {
    if (!g_bitmap_monitor_ctx.initialized || g_bitmap_monitor_ctx.mid_bitmap_monitor_get_current_scene == nullptr) {
        return nullptr;
    }

    JNIEnv *env;
    g_bitmap_monitor_ctx.java_vm->AttachCurrentThread(&env, nullptr);
    auto current_scene = env->CallObjectMethod(
            g_bitmap_monitor_ctx.jobject_bitmap_monitor,
            g_bitmap_monitor_ctx.mid_bitmap_monitor_get_current_scene
    );
    if (current_scene == nullptr) {
        return nullptr;
    }
    return static_cast<jstring>(current_scene);
}

static bool save_bitmap_to_file(
        JNIEnv *env,
        jobject bitmap_obj,
        AndroidBitmapInfo *bitmap_info,
        uint32_t width,
        uint32_t height,
        const char *path
) {
    if (env == nullptr || bitmap_obj == nullptr || path == nullptr) {
        log_e("save_bitmap_to_file failed, env or bitmap_obj or path is null!");
        return false;
    }

    jboolean is_recycled = env->IsSameObject(bitmap_obj, nullptr);
    if (is_recycled == JNI_TRUE) {
        log_e("save_bitmap_to_file failed, bitmap is recycled!");
        return false;
    }

    void *pixels = nullptr;
    if (AndroidBitmap_lockPixels(env, bitmap_obj, &pixels) < 0) {
        log_e("save_bitmap_to_file failed, AndroidBitmap_lockPixels failed!");
        return false;
    }
    if (pixels == nullptr) {
        log_e("save_bitmap_to_file failed, pixels is null!");
        return false;
    }

    unsigned int bytes_per_pixel = bitmap_info->stride / bitmap_info->width;
    // 为了存储为 BMP 文件，需要将像素数据转换为 BGRA 格式。
    convert_to_bgra(reinterpret_cast<unsigned char *>(pixels), height, width, bytes_per_pixel);
    write_bitmap_to_file(reinterpret_cast<unsigned char *>(pixels), height, width, path, bytes_per_pixel);
    // 再将像素数据转换回 RGBA 格式。
    convert_bgra_to_rgba(reinterpret_cast<unsigned char *>(pixels), height, width, bytes_per_pixel);
    AndroidBitmap_unlockPixels(env, bitmap_obj);

    log_i("save_bitmap_to_file succeed, path: %s", path);

    // notify Java layer
    jstring jstring_store_path = env->NewStringUTF(path);
    env->CallVoidMethod(
            g_bitmap_monitor_ctx.jobject_bitmap_monitor,
            g_bitmap_monitor_ctx.mid_bitmap_monitor_report_bitmap_stored,
            jstring_store_path
    );

    return true;
}

static void record_bitmap_allocated(
        ptr_long bitmap_native_ptr,
        long long creation_time,
        jobject *bitmap_obj_weak_ref,
        AndroidBitmapInfo *bitmap_info,
        jstring *store_path_global_ref,
        jstring *stack_trace_global_ref,
        jstring *creation_scene_global_ref,
        bool store_succeed
) {

    if (bitmap_info == nullptr) {
        log_w("record_bitmap_allocated skipped, android_bitmap_info is null!");
        return;
    }

    long long memory_allocation_byte_count = bitmap_info->stride * bitmap_info->height;
    unsigned int bytes_per_pixel = bitmap_info->stride / bitmap_info->width;
    uint32_t width = bitmap_info->width;
    uint32_t height = bitmap_info->height;

    // std::lock_guard 是 C++11 引入的一个用于简化互斥锁管理的工具类，它提供了一种便捷的 RAII（Resource Acquisition Is Initialization）风格的锁管理方式。
    std::lock_guard<std::mutex> locker(g_bitmap_monitor_ctx.record_mutex);

    g_bitmap_monitor_ctx.bitmap_records.push_back(
            {
                    .native_ptr = bitmap_native_ptr,
                    .width = width,
                    .height = height,
                    .stride = bitmap_info->stride,
                    .format = bitmap_info->format,
                    .time = creation_time,
                    .bitmap_store_path = *store_path_global_ref,
                    .java_bitmap_ref = *bitmap_obj_weak_ref,
                    .java_stack_trace = *stack_trace_global_ref,
                    .creation_scene = *creation_scene_global_ref,
                    .restore_succeed = store_succeed,
            }
    );

    g_bitmap_monitor_ctx.total_bitmap_created_count++;
    g_bitmap_monitor_ctx.total_bitmap_created_size += memory_allocation_byte_count;
    log_i("record_bitmap_allocated >>> width: %d, height: %d, stride: %d, bit_per_pixel: %d, "
          " allocation_byte_count: %lld, format: %d",
          width, height, bitmap_info->stride, bytes_per_pixel,
          memory_allocation_byte_count, bitmap_info->format
    );
}

jobject create_bitmap_proxy(
        JNIEnv *env,
        void *bitmap,
        int bitmap_create_flags,
        jbyteArray nine_patch_chunk,
        jobject nine_patch_insets,
        int density
) {
    // ShadowHook 的宏，用于记录当前的堆栈信息。
    SHADOWHOOK_STACK_SCOPE();
    log_i("create_bitmap_proxy called");

    // 调用原始函数
    jobject bitmap_obj = SHADOWHOOK_CALL_PREV(create_bitmap_proxy, env, bitmap, bitmap_create_flags, nine_patch_chunk, nine_patch_insets, density);
    if (bitmap_obj == nullptr) {
        //Fail to create bitmap
        log_e("create bitmap failed!");
        return bitmap_obj;
    }

    // 获取本地 bitmap 的指针
    ptr_long native_ptr = -1;
    if (g_bitmap_monitor_ctx.fid_bitmap_native_ptr != nullptr) {
        native_ptr = env->GetLongField(bitmap_obj, g_bitmap_monitor_ctx.fid_bitmap_native_ptr);
    }
    if (native_ptr == -1) {
        log_e("Failed to get native_ptr");
        return bitmap_obj;
    }

    // 获取 Bitmap 的信息
    AndroidBitmapInfo bitmap_info{};
    int ret = AndroidBitmap_getInfo(env, bitmap_obj, &bitmap_info);
    if (ret < 0) {
        //Fail to get bitmap info
        log_e("AndroidBitmap_getInfo failed, ret: %d", ret);
        return bitmap_obj;
    }

    int64_t memory_allocation_byte_count = bitmap_info.stride * bitmap_info.height;
    uint32_t width = bitmap_info.width;
    uint32_t height = bitmap_info.height;


    // 尝试获取堆栈信息
    jstring stack_trace = nullptr;
    if (g_bitmap_monitor_ctx.obtain_stack_threshold > 0 && memory_allocation_byte_count >= g_bitmap_monitor_ctx.obtain_stack_threshold) {
        stack_trace = dump_java_stack();
        if (stack_trace == nullptr) {
            //Fail to get java stacktrace
            log_e("Fail to get java stacktrace");
            return bitmap_obj;
        }
    }


    // 尝试获取当前场景
    jstring creation_scene = get_current_scene();


    // 尝试存储到文件
    char storage_path[256];
    bool should_store_to_file = false;
    bool store_succeed = true;
    long long creation_time = get_current_time_millis();
    if (g_bitmap_monitor_ctx.dump_to_file_threshold > 0 && memory_allocation_byte_count >= g_bitmap_monitor_ctx.dump_to_file_threshold) {
        should_store_to_file = true;
        int length = sprintf(storage_path, "%s/bitmap_%d_%d_%lld.bmp", g_bitmap_monitor_ctx.storage_dir, width, height, creation_time);
        storage_path[length] = '\0';
        store_succeed = save_bitmap_to_file(env, bitmap_obj, &bitmap_info, width, height, storage_path);
    }


    // TODO: 使用 native char* 代替 jstring，优化内存管理。
    // convert to (weak) global ref
    jobject bitmap_obj_weak_ref = env->NewWeakGlobalRef(bitmap_obj); // make it weak ref so that it won't affect GC.
    auto stack_trace_global_ref = stack_trace != nullptr ? reinterpret_cast<jstring>(env->NewGlobalRef(stack_trace)) : nullptr;
    auto store_path_global_ref = should_store_to_file ? reinterpret_cast<jstring>(env->NewGlobalRef(env->NewStringUTF(storage_path))) : nullptr;
    auto creation_scene_global_ref = creation_scene != nullptr ? reinterpret_cast<jstring>(env->NewGlobalRef(creation_scene)) : nullptr;
    // save to global container
    record_bitmap_allocated(
            native_ptr,
            creation_time,
            &bitmap_obj_weak_ref,
            &bitmap_info,
            &store_path_global_ref,
            &stack_trace_global_ref,
            &creation_scene_global_ref,
            store_succeed
    );

    // return the original bitmap object
    return bitmap_obj;
}

static int do_bitmap_creation_hook() {
    int api_level = get_api_level();
    if (api_level >= API_LEVEL_UNSUPPORTED) {
        log_w("Unsupported API level: %d", api_level);
        return FAILED;
    }
    log_i("hookBitmapNative called,  obtain_stack_threshold: %ld, restore_image_threshold: %ld, api_level: %d",
          g_bitmap_monitor_ctx.obtain_stack_threshold,
          g_bitmap_monitor_ctx.dump_to_file_threshold,
          api_level
    );

    auto so_name = api_level > API_LEVEL_10_0 ? BITMAP_CREATE_SYMBOL_SO_RUNTIME_AFTER_10 : BITMAP_CREATE_SYMBOL_SO_RUNTIME;
    auto bitmap_creation_symbol = api_level >= API_LEVEL_8_0 ? BITMAP_CREATE_SYMBOL_RUNTIME : BITMAP_CREATE_SYMBOL_BEFORE_8;
    auto stub = shadowhook_hook_sym_name(so_name, bitmap_creation_symbol, (void *) create_bitmap_proxy, nullptr);

    if (stub == nullptr) {
        g_bitmap_monitor_ctx.hooked = false;
        g_bitmap_monitor_ctx.fun_origin_bitmap_creation = nullptr;
        log_e("Failed to hook bitmap creation function");
        return FAILED;
    }

    g_bitmap_monitor_ctx.hooked = true;
    g_bitmap_monitor_ctx.fun_origin_bitmap_creation = stub;

    //hook 成功后，开启一个线程，定时轮训当前保存的数据，如果发现有被 recycle 的，移出去，更新总体数据。
    start_loop_check_recycle_thread();

    log_i("Hook bitmap creation function successfully");
    return SUCCESS;
}

// =================================================================================
// Initialization
// =================================================================================

extern "C" JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM *vm, void *reserved) {
    g_bitmap_monitor_ctx.java_vm = vm;

    JNIEnv *env;
    if (vm->GetEnv(reinterpret_cast<void **>(&env), JNI_VERSION_1_6) != JNI_OK) {
        return JNI_ERR;
    }

    // Find BitmapMonitor class and its methods.
    jclass bitmap_monitor_jclass = env->FindClass("me/ztiany/apm/aspect/bitmap/BitmapMonitor");
    if (bitmap_monitor_jclass == nullptr) {
        return JNI_ERR;
    }
    g_bitmap_monitor_ctx.jclass_of_bitmap_monitor = (jclass) env->NewGlobalRef(bitmap_monitor_jclass);
    g_bitmap_monitor_ctx.mid_bitmap_monitor_dump_stack = env->GetMethodID(
            bitmap_monitor_jclass,
            "dumpJavaStackTraceByNative",
            "()Ljava/lang/String;");
    g_bitmap_monitor_ctx.mid_bitmap_monitor_report_statistics = env->GetMethodID(
            bitmap_monitor_jclass,
            "reportBitmapStatisticsByNative",
            "(Lme/ztiany/apm/aspect/bitmap/BitmapStatistics;)V");
    g_bitmap_monitor_ctx.mid_bitmap_monitor_get_current_scene = env->GetMethodID(
            bitmap_monitor_jclass,
            "getCurrentSceneByNative",
            "()Ljava/lang/String;");
    g_bitmap_monitor_ctx.mid_bitmap_monitor_report_bitmap_stored = env->GetMethodID(
            bitmap_monitor_jclass,
            "reportLargeBitmapStoredByNative",
            "(Ljava/lang/String;)V");

    // Find Bitmap class and its methods/fields.
    jclass bitmap_jclass = env->FindClass("android/graphics/Bitmap");
    if (bitmap_jclass == nullptr) {
        return JNI_ERR;
    }
    g_bitmap_monitor_ctx.jclass_of_bitmap = (jclass) env->NewGlobalRef(bitmap_jclass);
    g_bitmap_monitor_ctx.mid_bitmap_is_recycled = env->GetMethodID(bitmap_jclass, "isRecycled", "()Z");
    g_bitmap_monitor_ctx.fid_bitmap_native_ptr = env->GetFieldID(bitmap_jclass, "mNativePtr", "J");

    // Find BitmapCreationRecord class and its constructor.
    jclass bitmap_record_jclass = env->FindClass("me/ztiany/apm/aspect/bitmap/BitmapCreationRecord");
    if (bitmap_record_jclass == nullptr) {
        return JNI_ERR;
    }
    g_bitmap_monitor_ctx.jclass_of_bitmap_record = (jclass) env->NewGlobalRef(bitmap_record_jclass);
    g_bitmap_monitor_ctx.mid_bitmap_record_constructor = env->GetMethodID(
            bitmap_record_jclass,
            "<init>",
            "(IIIIJJLjava/lang/String;Ljava/lang/String;Ljava/lang/String;)V"
    );


    // Find BitmapStatistics class and its constructor.
    jclass bitmap_statistics_jclass = env->FindClass("me/ztiany/apm/aspect/bitmap/BitmapStatistics");
    if (bitmap_statistics_jclass == nullptr) {
        return JNI_ERR;
    }
    g_bitmap_monitor_ctx.jclass_of_bitmap_statistics = (jclass) env->NewGlobalRef(bitmap_statistics_jclass);
    g_bitmap_monitor_ctx.mid_bitmap_statistics_constructor = env->GetMethodID(
            bitmap_statistics_jclass,
            "<init>",
            "(IJIJ[Lme/ztiany/apm/aspect/bitmap/BitmapCreationRecord;)V"
    );


    // set a flag to indicate that the initialization is done.
    g_bitmap_monitor_ctx.initialized = true;

    return JNI_VERSION_1_6;
}

// =================================================================================
// API
// =================================================================================

extern "C" JNIEXPORT jint JNICALL
Java_me_ztiany_apm_aspect_bitmap_BitmapMonitor_hookBitmapCreationNative(
        JNIEnv *env,
        jobject thiz,
        jlong check_recycle_interval,
        jlong obtain_stack_threshold,
        jlong dump_to_file_threshold,
        jstring storage_dir
) {

    if (g_bitmap_monitor_ctx.hooked) {
        log_w("Bitmap-Creation was already hooked.");
        return SUCCESS;
    }

    const char *dir = env->GetStringUTFChars(storage_dir, nullptr);
    // copy the content this dir refers to, so we can release the original one.
    const char *dir_copy = strdup(dir);
    env->ReleaseStringUTFChars(storage_dir, dir);

    g_bitmap_monitor_ctx.check_recycle_interval = check_recycle_interval;
    g_bitmap_monitor_ctx.obtain_stack_threshold = obtain_stack_threshold;
    g_bitmap_monitor_ctx.dump_to_file_threshold = dump_to_file_threshold;
    g_bitmap_monitor_ctx.storage_dir = dir_copy;
    g_bitmap_monitor_ctx.jobject_bitmap_monitor = env->NewGlobalRef(thiz);

    int hooked = do_bitmap_creation_hook();
    g_bitmap_monitor_ctx.hooked = hooked == SUCCESS;

    return hooked;
}

extern "C" JNIEXPORT void JNICALL
Java_me_ztiany_apm_aspect_bitmap_BitmapMonitor_releaseBitmapCreationNative(JNIEnv *env, jobject thiz) {
    g_bitmap_monitor_ctx.hooked = false;

    if (g_bitmap_monitor_ctx.fun_origin_bitmap_creation != nullptr) {
        shadowhook_unhook(g_bitmap_monitor_ctx.fun_origin_bitmap_creation);
    }
    g_bitmap_monitor_ctx.fun_origin_bitmap_creation = nullptr;

    if (g_bitmap_monitor_ctx.jobject_bitmap_monitor != nullptr) {
        env->DeleteGlobalRef(g_bitmap_monitor_ctx.jobject_bitmap_monitor);
    }

    if (g_bitmap_monitor_ctx.storage_dir != nullptr) {
        free((void *) g_bitmap_monitor_ctx.storage_dir);
    }

    log_i("Bitmap-Creation was released.");
}

extern "C" JNIEXPORT jobject JNICALL
Java_me_ztiany_apm_aspect_bitmap_BitmapMonitor_dumpBitmapSimpleStatisticsNative(JNIEnv *env, jobject thiz) {
    if (!g_bitmap_monitor_ctx.hooked) {
        log_e("dumpBitmapStatisticsNative failed, bitmap monitor is not hooked!");
        return nullptr;
    }
    return dump_bitmap_statistics(env, true);
}

extern "C" JNIEXPORT jobject JNICALL
Java_me_ztiany_apm_aspect_bitmap_BitmapMonitor_dumpBitmapStatisticsNative(JNIEnv *env, jobject thiz) {
    if (!g_bitmap_monitor_ctx.hooked) {
        log_e("dumpBitmapStatisticsNative failed, bitmap monitor is not hooked!");
        return nullptr;
    }
    return dump_bitmap_statistics(env, false);
}
