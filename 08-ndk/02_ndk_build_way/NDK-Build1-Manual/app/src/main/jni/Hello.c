#include <jni.h>
#include "com_ztiany_sample1_JNIObj.h"

JNIEXPORT jstring JNICALL Java_com_ztiany_sample1_JNIObj_getMessage
  (JNIEnv * env, jobject jobj){
            char *answer = "Hello Java";
            return (*env)->NewStringUTF(env,answer);
}