#include <jni.h>
#include "lib_static.h"
#include "android/log.h"

#define LOG_TAG "System.out"
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, __VA_ARGS__)

JNIEXPORT jint JNICALL Java_com_ztiany_ndk_two_1libs_JniUtils_add
        (JNIEnv *env, jobject jobj, jint a, jint b) {
    LOGD("正在执行C代码");
    return add(a, b);
}