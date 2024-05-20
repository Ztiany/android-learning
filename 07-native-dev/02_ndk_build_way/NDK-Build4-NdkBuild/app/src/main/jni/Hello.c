#include <jni.h>
//导入Android log头文件
#include <android/log.h>
#include "com_ztiany_sample2_JniUtils.h"

#define LOG_TAG "System.out"
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, __VA_ARGS__)
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)

JNIEXPORT jstring JNICALL Java_com_ztiany_sample2_JniUtils_getMessage
  (JNIEnv * env, jobject jobj){
        LOGI("执行C代码------------>");
        LOGD("执行C代码------------>");
        char* charArr = "Hello Java~~";
        return (*env)->NewStringUTF(env, charArr);

 }
