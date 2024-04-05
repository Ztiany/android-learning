//
// Created by zhangshixin
//

#include <jni.h>
#include <android/bitmap.h>
#include <android/trace.h>
#include <pthread.h>
#include <unistd.h>
#include <ctime>

#include "bitmap_monitor.h"
#include "bitmap_saver.h"
#include "shadowhook.h"

static struct BitmapMonitorContext g_ctx;

static long g_recycle_check_interval_second;
static long g_get_stack_threshold;
static long g_restore_image_threshold;
static const char *g_restore_image_dir;
static bool g_notify_check_local_image_size;

jstring dump_java_stack();

jstring get_current_scene();

long long get_current_time_millis();

bool is_empty_array(unsigned char *p) {
    return !*p;
}

/**
 * restore image from pixel buffer, skip that if buffer is empty
 * @param env
 * @param bitmap_obj
 * @param pixels
 * @param copy_file_path
 * @param check_size
 */
bool restore_image(
        JNIEnv *env,
        jobject bitmap_obj,
        unsigned int width,
        unsigned int height,
        unsigned int bit_per_pixel,
        char *copy_file_path,
        bool check_size
) {
    if (env == nullptr || bitmap_obj == nullptr || copy_file_path == nullptr) {
        return false;
    }
    jboolean object_recycled = env->IsSameObject(bitmap_obj, nullptr);

    if (object_recycled == JNI_TRUE) {
        //not reachable
        return false;
    }

    void *pixels;
    // 当你使用 AndroidBitmap_lockPixels 锁定一个 Bitmap 的像素数据时，得到的像素数据通常是按照 ARGB_8888 格式排列的，这是 Android 中最常用的像素格式之一。
    // 在这个格式中，每个像素由 4 个字节组成，分别代表 Alpha（透明度）、Red（红色）、Green（绿色）和 Blue（蓝色）通道。
    if (AndroidBitmap_lockPixels(env, bitmap_obj, &pixels) == 0) {
        LOGI("restore_image, width: %d, height: %d, %s", width, height, copy_file_path);

        if (check_size && is_empty_array((unsigned char *) pixels)) {
            //no pixel data yet
            return false;
        }

        // 转换为 BGR 格式
        convert_to_bgr((unsigned char *) pixels, height, width, bit_per_pixel);
        write_bitmap_file(reinterpret_cast<unsigned char *>(pixels), height, width, copy_file_path, bit_per_pixel);

        // 将修改过的像素数据转换回 ARGB_8888 格式
        convert_bgr_to_rgba((unsigned char *) pixels, height, width, bit_per_pixel);
        AndroidBitmap_unlockPixels(env, bitmap_obj);

        if (g_notify_check_local_image_size) {
            // 如果需要通知，则通知 Java 层有 Bitmap 被存储了
            auto save_path_jstring = reinterpret_cast<jstring>(env->NewStringUTF(copy_file_path));
            env->CallStaticVoidMethod(g_ctx.bitmap_monitor_jclass, g_ctx.report_bitmap_file_method, save_path_jstring);
        }

        return true;
    }

    LOGI("create_bitmap_proxy s6, Fail to lockPixels, %d", AndroidBitmap_lockPixels(env, bitmap_obj, &pixels));
    int length = sprintf(copy_file_path, "Restore image failed, fail to lockPixels.");
    copy_file_path[length] = '\0';
    return false;
}

/**
 * 保存记录，后续释放时，需要移除
 * @param android_bitmap_info
 * @param bitmap_saved_path
 */
void record_bitmap_allocated(
        ptr_long native_ptr,
        jobject *bitmap_obj_weak_ref,
        AndroidBitmapInfo *android_bitmap_info,
        jstring &bitmap_saved_path,
        jstring &stacks,
        jstring &current_scene,
        bool restore_succeed
) {
    if (android_bitmap_info == nullptr) {
        LOGI("record_bitmap_allocated skipped, android_bitmap_info is null!");
        return;
    }

    // 计算分配了多少内存
    long long allocation_byte_count = android_bitmap_info->stride * android_bitmap_info->height;
    // 计算每个像素占用多少字节
    unsigned int bit_per_pixel = android_bitmap_info->stride / android_bitmap_info->width;
    // Bitmap 的宽
    uint32_t width = android_bitmap_info->width;
    // Bitmap 的高
    uint32_t height = android_bitmap_info->height;

    LOGI("record_bitmap_allocated >>> width: %d, height: %d, stride: %d, bit_per_pixel: %d, "
         " allocation_byte_count: %lld, format: %d",
         width, height, android_bitmap_info->stride, bit_per_pixel,
         allocation_byte_count, android_bitmap_info->format);

    // std::lock_guard 是 C++11 引入的一个用于简化互斥锁管理的工具类，它提供了一种便捷的 RAII（Resource Acquisition Is Initialization）风格的锁管理方式。
    std::lock_guard<std::mutex> lock(g_ctx.record_mutex);

    // 记录到全局容器中
    auto time = get_current_time_millis();
    g_ctx.bitmap_records.push_back(
            {
                    .native_ptr = native_ptr,
                    .width =  width,
                    .height = height,
                    .stride =  android_bitmap_info->stride,
                    .format = android_bitmap_info->format,
                    .time = time,
                    .large_bitmap_save_path = bitmap_saved_path,
                    .java_bitmap_ref = *bitmap_obj_weak_ref,
                    .java_stack_jstring = stacks,
                    .current_scene = current_scene,
                    .restore_succeed = restore_succeed,
            }
    );

    // 记录总体数据
    g_ctx.create_bitmap_count++;
    g_ctx.create_bitmap_size += allocation_byte_count;
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

    // 调用原始函数
    jobject bitmap_obj = SHADOWHOOK_CALL_PREV(create_bitmap_proxy, env, bitmap, bitmap_create_flags, nine_patch_chunk, nine_patch_insets, density);

    if (bitmap_obj == nullptr) {
        //Fail to create bitmap
        LOGI(" s2 create bitmap failed!");
        return bitmap_obj;
    }

    // 获取本地 bitmap 的指针
    ptr_long native_ptr = -1;
    if (g_ctx.native_ptr_field != nullptr) {
        native_ptr = env->GetLongField(bitmap_obj, g_ctx.native_ptr_field);
    }

    // 获取 bitmap 的信息
    AndroidBitmapInfo android_bitmap_info{};
    int ret = AndroidBitmap_getInfo(env, bitmap_obj, &android_bitmap_info);
    if (ret < 0) {
        //Fail to get bitmap info
        LOGI("AndroidBitmap_getInfo failed, ret: %d", ret);
        return bitmap_obj;
    }
    // 分配了多少内存
    int64_t allocation_byte_count = android_bitmap_info.stride * android_bitmap_info.height;
    // Bitmap 的位深
    unsigned int bit_per_pixel = android_bitmap_info.stride / android_bitmap_info.width;
    // Bitmap 的宽
    uint32_t width = android_bitmap_info.width;
    // Bitmap 的高
    uint32_t height = android_bitmap_info.height;

    // 尝试获取堆栈信息
    jstring stack_jstring = nullptr;
    if (g_get_stack_threshold > 0 && allocation_byte_count >= g_get_stack_threshold) {
        stack_jstring = dump_java_stack();
        if (stack_jstring == nullptr) {
            //Fail to get java stacktrace
            LOGI("create_bitmap_proxy s5, Fail to get java stacktrace");
            return bitmap_obj;
        }
    }

    // 尝试存储到本地
    char copy_file_path[256];
    bool save_to_local = false;
    bool restore_succeed = true;

    if (g_restore_image_threshold > 0 && allocation_byte_count >= g_restore_image_threshold && g_restore_image_dir != nullptr) {
        //Get pixels and save to local storage
        if (g_ctx.java_vm != nullptr) {
            g_ctx.java_vm->AttachCurrentThread(&env, nullptr);
        }

        save_to_local = true;
        auto time = get_current_time_millis();

        int length = sprintf(copy_file_path, "%s/restore_%d_%d_%lld.bmp", g_restore_image_dir, width, height, time);
        copy_file_path[length] = '\0';
        // 执行存储
        restore_succeed = restore_image(env, bitmap_obj, width, height, bit_per_pixel, copy_file_path, true);
    }

    // 保存 Bitmap 的引用，使用弱引用，防止影响 GC
    jobject bitmap_obj_weak_ref = env->NewWeakGlobalRef(bitmap_obj);

    auto stack_global_ref = stack_jstring != nullptr ? reinterpret_cast<jstring>(env->NewGlobalRef(stack_jstring)) : nullptr;
    auto save_path_ref = save_to_local ? reinterpret_cast<jstring>(env->NewGlobalRef(env->NewStringUTF(copy_file_path))) : nullptr;

    // 当前 Bitmap 在哪个场景下创建的
    jstring current_scene = get_current_scene();
    auto current_scene_global_ref = current_scene != nullptr ? reinterpret_cast<jstring>(env->NewGlobalRef(current_scene)) : nullptr;

    // 记录到全局容器中
    record_bitmap_allocated(
            native_ptr,
            &bitmap_obj_weak_ref,
            &android_bitmap_info,
            save_path_ref,
            stack_global_ref,
            current_scene_global_ref,
            restore_succeed
    );

    return bitmap_obj;
}


//创建一个 BitmapInfo java 对象，用于上报
jobject create_bitmap_info_java_object(
        JNIEnv *env,
        int64_t remain_bitmap_count,
        long long remain_bitmap_size,
        jobjectArray java_bitmap_record_array
) {
    jclass bitmap_sum_class = g_ctx.bitmap_info_jclass;
    jmethodID bitmap_sum_constructor_method = env->GetMethodID(bitmap_sum_class, "<init>", "(JJJJ)V");

    jobject bitmap_sum_obj = env->NewObject(
            bitmap_sum_class,
            bitmap_sum_constructor_method,
            g_ctx.create_bitmap_count,
            g_ctx.create_bitmap_size,
            remain_bitmap_count,
            remain_bitmap_size
    );

    if (java_bitmap_record_array != nullptr) {
        jfieldID record_list_field = env->GetFieldID(bitmap_sum_class, "remainBitmapRecords", "[Ltop/shixinzhang/bitmapmonitor/BitmapRecord;");
        env->SetObjectField(bitmap_sum_obj, record_list_field, java_bitmap_record_array);
    }

    return bitmap_sum_obj;
}

/**
 * 循环检查，间隔睡眠，直到停止监控
 * @return
 */
static void *thread_routine(void *) {
    if (g_ctx.java_vm == nullptr) {
        return nullptr;
    }

    // 获取当前线程的 JNIEnv
    JNIEnv *env;
    jboolean result = g_ctx.java_vm->AttachCurrentThread(&env, nullptr);
    if (result != JNI_OK) {
        LOGI("AttachCurrentThread failed");
        return nullptr;
    }

    // 创建一个函数，用于线程睡眠
    auto sleep_some_time = []() {
        long sleep_time = g_recycle_check_interval_second >= 1 ? g_recycle_check_interval_second : BITMAP_RECORD_CHECK_THREAD_SLEEP_SECONDS;
        struct timeval tv{};
        tv.tv_sec = sleep_time;
        tv.tv_usec = 0;
        select(0, nullptr, nullptr, nullptr, &tv);
    };

    // 开始循环检查
    while (g_ctx.open_hook) {
        // 尝试获取锁
        if (!g_ctx.record_mutex.try_lock()) {
            // 获取失败，睡眠一会儿
            sleep_some_time();
            continue;
        }

        // 获取当前的记录
        auto bitmap_records = g_ctx.bitmap_records;
        std::vector<BitmapRecord> copy_records;

        // 如果没有记录，直接释放锁，睡眠一会儿
        if (bitmap_records.empty()) {
            g_ctx.record_mutex.unlock();
            sleep_some_time();
            continue;
        }

        // 把所有已经被回收的 Bitmap 移除
        int index = 0;
        long long sum_bytes_alloc = 0;
        for (auto it = bitmap_records.begin(); it != bitmap_records.end(); it++) {
            index++;
            //1.whether is reachable
            jboolean object_recycled = env->IsSameObject(it->java_bitmap_ref, nullptr);
            if (object_recycled == JNI_TRUE) {
                //not reachable
                continue;
            }
            auto bitmap_local_ref = env->NewLocalRef(it->java_bitmap_ref);

            //2.whether is recycled
            jboolean bitmap_recycled = env->CallBooleanMethod(bitmap_local_ref, g_ctx.bitmap_recycled_method);
            if (bitmap_recycled) {
                //recycled
                env->DeleteLocalRef(bitmap_local_ref);
                continue;
            }

            //not recycled, add for next check
            sum_bytes_alloc += (it->height * it->stride);
            copy_records.push_back({
                                           .native_ptr = it->native_ptr,
                                           .width =  it->width,
                                           .height = it->height,
                                           .stride =  it->stride,
                                           .format = it->format,
                                           .time = it->time,
                                           .large_bitmap_save_path = it->large_bitmap_save_path,
                                           .java_bitmap_ref = it->java_bitmap_ref,
                                           .java_stack_jstring = it->java_stack_jstring,
                                           .current_scene = it->current_scene,
                                           .restore_succeed = it->restore_succeed,
                                   });
            env->DeleteLocalRef(bitmap_local_ref);
        }// for loop end.

        // 如果有移除的，更新记录
        if (copy_records.size() != bitmap_records.size()) {
            g_ctx.bitmap_records = copy_records;
        }

        g_ctx.record_mutex.unlock();

        // 将当前的记录上报到 Java 层
        int64_t remain_bitmap_count = copy_records.size();
        jobject bitmap_info_jobject = create_bitmap_info_java_object(env, remain_bitmap_count, sum_bytes_alloc, nullptr);
        env->CallStaticVoidMethod(g_ctx.bitmap_monitor_jclass, g_ctx.report_bitmap_data_method, bitmap_info_jobject);
        sleep_some_time();
    }

    return nullptr;
}

void start_loop_check_recycle_thread() {
    pthread_t thread;
    pthread_attr_t attr;
    pthread_attr_init(&attr);
    pthread_attr_setdetachstate(&attr, PTHREAD_CREATE_DETACHED);

    pthread_create(&thread, &attr, thread_routine, nullptr);
}

int get_api_level() {
    char sdk_version_str[PROP_VALUE_MAX];
    __system_property_get("ro.build.version.sdk", sdk_version_str);
    return atoi(sdk_version_str);
}

jint do_hook_bitmap(
        long bitmap_recycle_check_interval,
        long get_stack_threshold,
        long restore_image_threshold,
        const char *restore_image_dir,
        bool notify_check_local_image_size
) {

    // 记录相关配置
    g_recycle_check_interval_second = bitmap_recycle_check_interval;
    g_get_stack_threshold = get_stack_threshold;
    g_restore_image_threshold = restore_image_threshold;
    g_restore_image_dir = restore_image_dir;
    g_notify_check_local_image_size = notify_check_local_image_size;

    // 获取系统版本
    int api_level = get_api_level();

    if (api_level > 33) {
        return -2;
    }

    LOGI(
            "hookBitmapNative called,  printStackThreshold: %ld, restore_image_threshold: %ld, api_level: %d",
            get_stack_threshold,
            restore_image_threshold,
            api_level
    );

    // 根据系统版本选择 so 和函数符号
    auto so = api_level > API_LEVEL_10_0 ? BITMAP_CREATE_SYMBOL_SO_RUNTIME_AFTER_10 : BITMAP_CREATE_SYMBOL_SO_RUNTIME;
    auto symbol = api_level >= API_LEVEL_8_0 ? BITMAP_CREATE_SYMBOL_RUNTIME : BITMAP_CREATE_SYMBOL_BEFORE_8;

    // 执行 hook
    auto stub = shadowhook_hook_sym_name(so, symbol, (void *) create_bitmap_proxy, nullptr);

    // 如果 hook 成功，记录相关信息
    if (stub != nullptr) {
        g_ctx.open_hook = true;
        g_ctx.shadowhook_stub = stub;
        JNIEnv *jni_env;
        if (g_ctx.java_vm->AttachCurrentThread(&jni_env, nullptr) == JNI_OK) {
            // 找到 Bitmap 的 isRecycled 方法
            jclass bitmap_java_class = jni_env->FindClass("android/graphics/Bitmap");
            g_ctx.bitmap_recycled_method = jni_env->GetMethodID(bitmap_java_class, "isRecycled", "()Z");

            // 找到 BitmapMonitorData 类及其构造函数
            jclass bitmap_info_jobject = jni_env->FindClass("top/shixinzhang/bitmapmonitor/BitmapMonitorData");
            g_ctx.bitmap_info_jclass = static_cast<jclass>(jni_env->NewGlobalRef(bitmap_info_jobject));
            g_ctx.report_bitmap_data_method = jni_env->GetStaticMethodID(
                    g_ctx.bitmap_monitor_jclass,
                    "reportBitmapInfo",
                    "(Ltop/shixinzhang/bitmapmonitor/BitmapMonitorData;)V"
            );

            // 找到 BitmapMonitor 的 reportBitmapFile 方法
            g_ctx.report_bitmap_file_method = jni_env->GetStaticMethodID(
                    g_ctx.bitmap_monitor_jclass,
                    "reportBitmapFile",
                    "(Ljava/lang/String;)V"
            );

        }

        //hook 成功后，开启一个线程，定时轮训当前保存的数据，如果发现有被 recycle 的，移出去，更新总体数据。
        start_loop_check_recycle_thread();

        return 0;
    }

    // 标记 hook 失败
    g_ctx.open_hook = false;
    g_ctx.shadowhook_stub = nullptr;
    return -1;
}

extern "C" jobject do_dump_info(JNIEnv *env, bool justCount, bool ensureRestoreImage) {
    if (g_ctx.java_vm != nullptr) {
        g_ctx.java_vm->AttachCurrentThread(&env, nullptr);
    }

    if (!g_ctx.record_mutex.try_lock()) {
        return nullptr;
    }

    auto bitmap_records = g_ctx.bitmap_records;
    int64_t remain_bitmap_count = bitmap_records.size();
    long long remain_bitmap_size = 0;
    int index = 0;
    jobjectArray java_bitmap_record_array = env->NewObjectArray(remain_bitmap_count,
                                                                g_ctx.bitmap_record_class, nullptr);

    for (auto record: bitmap_records) {
        remain_bitmap_size += record.height * record.stride;

        if (!justCount) {
            //还需要获取具体信息
            jstring save_path = record.large_bitmap_save_path;
            jstring stacks = record.java_stack_jstring;
            jstring current_scene = record.current_scene;
            bool restore_succeed = record.restore_succeed;

            if (ensureRestoreImage && save_path != nullptr && !restore_succeed) {
                //Need get pixels and restore again
                char *path = const_cast<char *>(env->GetStringUTFChars(save_path, 0));
                unsigned int bit_per_pixel = record.stride / record.width;
                unsigned int width = record.width;
                unsigned int height = record.height;

                restore_image(env, record.java_bitmap_ref, width, height, bit_per_pixel, path, false);
            }

            //每一条记录
            jobject java_record = env->NewObject(
                    g_ctx.bitmap_record_class,
                    g_ctx.bitmap_record_constructor_method,
                    (jlong) record.native_ptr,
                    (jint) record.width,
                    (jint) record.height,
                    (jint) (record.stride / record.width),
                    (jint) record.format,
                    record.time,
                    save_path,
                    stacks,
                    current_scene
            );

            env->SetObjectArrayElement(java_bitmap_record_array, index, java_record);
        }

        index++;
    }

    jobject bitmap_sum_obj = create_bitmap_info_java_object(env, remain_bitmap_count, remain_bitmap_size, java_bitmap_record_array);

    g_ctx.record_mutex.unlock();

    return bitmap_sum_obj;
}

jstring dump_java_stack() {
    if (!g_ctx.inited) {
        return nullptr;
    }

    JNIEnv *env;
    g_ctx.java_vm->AttachCurrentThread(&env, nullptr);
    auto stacks = env->CallStaticObjectMethod(g_ctx.bitmap_monitor_jclass, g_ctx.dump_stack_method);
    if (stacks == nullptr) {
        return nullptr;
    }
    return static_cast<jstring>(stacks);
}

jstring get_current_scene() {
    if (!g_ctx.inited) {
        return nullptr;
    }

    JNIEnv *env;
    g_ctx.java_vm->AttachCurrentThread(&env, nullptr);
    auto current_scene = env->CallStaticObjectMethod(g_ctx.bitmap_monitor_jclass, g_ctx.get_current_scene_method);
    if (current_scene == nullptr) {
        return nullptr;
    }
    return static_cast<jstring>(current_scene);
}

long long get_current_time_millis() {
    struct timeval t{};
    gettimeofday(&t, nullptr);
    return (long long) t.tv_sec * 1000 + t.tv_usec / 1000;
}

extern "C" JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM *vm, void *reserved) {
    JNIEnv *env;
    // 记录 java_vm
    g_ctx.java_vm = vm;
    if (vm->GetEnv(reinterpret_cast<void **>(&env), JNI_VERSION_1_6) != JNI_OK) {
        return JNI_ERR;
    }

    // 找到核心类 BitmapMonitor
    jclass clz = env->FindClass("top/shixinzhang/bitmapmonitor/BitmapMonitor");
    // 因为需要长期保存，所以需要用 NewGlobalRef
    g_ctx.bitmap_monitor_jclass = (jclass) env->NewGlobalRef(clz);

    // 找到 BitmapMonitor 相关函数的 id，这些 id 都是基本类型，不需要 NewGlobalRef
    jmethodID dump_stack_method_id = env->GetStaticMethodID(g_ctx.bitmap_monitor_jclass, "dumpJavaStack", "()Ljava/lang/String;");
    jmethodID get_current_scene_method_id = env->GetStaticMethodID(g_ctx.bitmap_monitor_jclass, "getCurrentScene", "()Ljava/lang/String;");
    g_ctx.dump_stack_method = dump_stack_method_id;
    g_ctx.get_current_scene_method = get_current_scene_method_id;

    // 找到 Bitmap 类的 mNativePtr 字段
    jclass bitmap_jclass = env->FindClass("android/graphics/Bitmap");
    g_ctx.native_ptr_field = env->GetFieldID(bitmap_jclass, "mNativePtr", "J");

    // 找到 BitmapRecord 类及其构造函数
    jclass bitmap_record_clz = env->FindClass("top/shixinzhang/bitmapmonitor/BitmapRecord");
    g_ctx.bitmap_record_class = (jclass) env->NewGlobalRef(bitmap_record_clz);
    if (g_ctx.bitmap_record_class != nullptr) {
        g_ctx.bitmap_record_constructor_method = env->GetMethodID(
                g_ctx.bitmap_record_class,
                "<init>",
                "(JIIIIJLjava/lang/String;Ljava/lang/String;Ljava/lang/String;)V"
        );
    }

    // 标记初始化完成
    g_ctx.inited = true;

    return JNI_VERSION_1_6;
}

extern "C" JNIEXPORT jint JNICALL
Java_top_shixinzhang_bitmapmonitor_BitmapMonitor_hookBitmapNative(
        JNIEnv *env,
        jclass clazz,
        jlong check_recycle_interval,
        jlong get_stack_threshold,
        jlong restore_image_threshold,
        jstring restore_image_dir,
        jboolean notify_check_local_image_size
) {

    const char *dir = env->GetStringUTFChars(restore_image_dir, 0);
    return do_hook_bitmap(
            check_recycle_interval,
            get_stack_threshold,
            restore_image_threshold,
            dir,
            notify_check_local_image_size
    );
}

extern "C" JNIEXPORT void JNICALL
Java_top_shixinzhang_bitmapmonitor_BitmapMonitor_stopHookBitmapNative(JNIEnv *env, jclass clazz) {
    g_ctx.open_hook = false;
    if (g_ctx.shadowhook_stub != nullptr) {
        shadowhook_unhook(g_ctx.shadowhook_stub);
    }
}

extern "C" JNIEXPORT jobject JNICALL
Java_top_shixinzhang_bitmapmonitor_BitmapMonitor_dumpBitmapCountNative(JNIEnv *env, jclass clazz) {
    if (!g_ctx.open_hook) {
        return nullptr;
    }
    return do_dump_info(env, true, false);
}

extern "C" JNIEXPORT jobject JNICALL
Java_top_shixinzhang_bitmapmonitor_BitmapMonitor_dumpBitmapInfoNative(JNIEnv *env, jclass clazz, jboolean ensureRestoreImage) {
    return do_dump_info(env, false, ensureRestoreImage);
}