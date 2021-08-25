#include <jni.h>
#include "com_ztiany_sample2_JniUtils.h"

JNIEXPORT jstring JNICALL Java_com_ztiany_sample2_JniUtils_getMessage
  (JNIEnv * env, jobject jobj){

        char* charArr = "Hello Java~~";
        return (*env)->NewStringUTF(env, charArr);

 }
