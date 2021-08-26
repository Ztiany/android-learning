#ifndef JNISAMPLE_UTILS_H
#define JNISAMPLE_UTILS_H

#include "jni.h"

#define LOG_TAG "C-Log"

#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, __VA_ARGS__)
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)

char *jString2CString(JNIEnv *env, jstring jstr);

void cStringToJString(JNIEnv *env, char *str, jobject *receive);

#endif
