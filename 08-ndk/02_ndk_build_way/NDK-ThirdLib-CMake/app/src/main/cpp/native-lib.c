#include <jni.h>
#include <gmath.h>
#include "android/log.h"

#define LOG_TAG "System.out"
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, __VA_ARGS__)


JNIEXPORT jstring JNICALL
Java_com_ztiany_cmake_all_MainActivity_stringFromJNI(JNIEnv *env, jobject thiz/* this */) {
    char *hello = "Hello from C++";
    unsigned gp = gpower(3);
    LOGD("the result = %d", gp);
#ifdef Debug
    LOGD("run---------------debug");
#endif
    return (*env)->NewStringUTF(env, hello);
}
