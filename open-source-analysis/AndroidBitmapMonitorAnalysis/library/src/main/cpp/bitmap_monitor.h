//
// Created by zhangshixin
//

#ifndef MY_APPLICATION_BITMAP_MONITOR_H
#define MY_APPLICATION_BITMAP_MONITOR_H

#include <jni.h>
#include <string>
#include <android/log.h>
#include <vector>
#include <mutex>
#include <sys/system_properties.h>

typedef long long ptr_long;

struct BitmapRecord {

    /** The bitmap native pointer. */
    ptr_long native_ptr;

    uint32_t width;

    /** The bitmap height in pixels. */
    uint32_t height;

    /** The number of byte per row. */
    uint32_t stride;

    /** The bitmap pixel format. See {@link AndroidBitmapFormat} */
    int32_t format;

    /** the time when the bitmap was created. */
    long long time;

    jstring large_bitmap_save_path;

    jobject java_bitmap_ref;

    jstring java_stack_jstring;

    jstring current_scene;

    bool restore_succeed;
};

struct BitmapMonitorContext {
    /** JavaVM */
    JavaVM *java_vm;

    /** 是否初始化了 */
    bool inited;
    /** 是否开启了 hook */
    bool open_hook;

    /** 被 hook 的创建 Bitmap 的函数 */
    void *shadowhook_stub;

    /** Bitmap.isRecycled 的函数 */
    jmethodID bitmap_recycled_method;

    /** BitmapMonitor 类 */
    jclass bitmap_monitor_jclass;

    /** Bitmap 中的 mNativePtr 字段 */
    jfieldID native_ptr_field;

    /** BitmapMonitor 的 dumpJavaStack 函数 */
    jmethodID dump_stack_method;

    /** BitmapMonitor 的 getCurrentScene 函数 */

    jmethodID get_current_scene_method;

    /** BitmapMonitorData 类 */
    jclass bitmap_info_jclass;

    /** BitmapMonitor 的 reportBitmapInfo 函数 */
    jmethodID report_bitmap_data_method;
    /** BitmapMonitor 的 reportBitmapFile 函数 */
    jmethodID report_bitmap_file_method;

    /** BitmapRecord 类 */
    jclass bitmap_record_class;

    /** BitmapRecord 类的构造函数 */
    jmethodID bitmap_record_constructor_method;

    /** 所有被创建的 Bitmap 的记录 */
    std::vector<BitmapRecord> bitmap_records;

    /** 被创建的 Bitmap 总数*/
    int64_t create_bitmap_size;

    /** 被创建的 Bitmap 总的内存占用*/
    int64_t create_bitmap_count;

    /** 同步锁 */
    std::mutex record_mutex;
};

//frameworks/base/libs/hwui/hwui/Bitmap.cpp

#define BITMAP_CREATE_SYMBOL_SO_RUNTIME_AFTER_10 "libhwui.so"
#define BITMAP_CREATE_SYMBOL_SO_RUNTIME "libandroid_runtime.so"

#define BITMAP_CREATE_SYMBOL_RUNTIME "_ZN7android6bitmap12createBitmapEP7_JNIEnvPNS_6BitmapEiP11_jbyteArrayP8_jobjecti"
#define BITMAP_CREATE_SYMBOL_BEFORE_8 "_ZN11GraphicsJNI12createBitmapEP7_JNIEnvPN7android6BitmapEiP11_jbyteArrayP8_jobjecti"

//默认 5 秒检查一次
#define BITMAP_RECORD_CHECK_THREAD_SLEEP_SECONDS 5
#define API_LEVEL_10_0 29
#define API_LEVEL_8_0 26

#define LOG_TAG "bitmap_monitor"
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO,LOG_TAG, __VA_ARGS__)

#endif //MY_APPLICATION_BITMAP_MONITOR_H
