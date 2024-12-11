#ifndef ANDROID_APM_BITMAP_MONITOR_H
#define ANDROID_APM_BITMAP_MONITOR_H

#include <cstdint>
#include <mutex>
#include <jni.h>
#include <vector>

#define BITMAP_CREATE_SYMBOL_SO_RUNTIME_AFTER_10 "libhwui.so"
#define BITMAP_CREATE_SYMBOL_SO_RUNTIME "libandroid_runtime.so"

#define BITMAP_CREATE_SYMBOL_RUNTIME "_ZN7android6bitmap12createBitmapEP7_JNIEnvPNS_6BitmapEiP11_jbyteArrayP8_jobjecti"
#define BITMAP_CREATE_SYMBOL_BEFORE_8 "_ZN11GraphicsJNI12createBitmapEP7_JNIEnvPN7android6BitmapEiP11_jbyteArrayP8_jobjecti"

#define BITMAP_RECORD_CHECK_THREAD_SLEEP_SECONDS 5

#define API_LEVEL_10_0 29
#define API_LEVEL_8_0 26

// Android 14
#define API_LEVEL_UNSUPPORTED 34

#define SUCCESS 0
#define FAILED -1

typedef long long ptr_long;

class BitmapRecord {
public:
    /** The bitmap native pointer. */
    ptr_long native_ptr;

    uint32_t width;
    uint32_t height;

    /** The number of byte per row. */
    uint32_t stride;

    /** The bitmap pixel format. See {@link AndroidBitmapFormat} */
    int32_t format;

    /** the time when the bitmap was created. */
    long long time;

    jstring bitmap_store_path;
    jobject java_bitmap_ref;
    jstring java_stack_trace;
    jstring creation_scene;

    bool restore_succeed;
};

class BitmapMonitorContext {
public:
    JavaVM *java_vm;
    bool initialized;


    bool hooked;
    void *fun_origin_bitmap_creation;


    jmethodID mid_bitmap_is_recycled;
    jfieldID fid_bitmap_native_ptr;
    jclass jclass_of_bitmap;


    jmethodID mid_bitmap_monitor_dump_stack;
    jmethodID mid_bitmap_monitor_get_current_scene;
    jmethodID mid_bitmap_monitor_report_statistics;
    jmethodID mid_bitmap_monitor_report_bitmap_stored;
    jclass jclass_of_bitmap_monitor;
    jobject jobject_bitmap_monitor;


    jmethodID mid_bitmap_record_constructor;
    jclass jclass_of_bitmap_record;


    jmethodID mid_bitmap_statistics_constructor;
    jclass jclass_of_bitmap_statistics;


    std::vector<BitmapRecord> bitmap_records;
    ino64_t total_bitmap_created_count;
    ino64_t total_bitmap_created_size;

    /** for thread safe */
    std::mutex record_mutex;


    /* configs from java. */
    long check_recycle_interval;
    long obtain_stack_threshold;
    long dump_to_file_threshold;
    const char *storage_dir;
};

#endif
