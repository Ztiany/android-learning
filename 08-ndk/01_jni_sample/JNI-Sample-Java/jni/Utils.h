#ifndef JNISAMPLE_UTILS_H
#define JNISAMPLE_UTILS_H

#include <jni.h>

char *jstring2Cstring(JNIEnv *env, jstring jstr);

jstring cstring2Jstring(JNIEnv *env, char *cstr);

void JNU_ThrowByName(JNIEnv *env, const char *name, const char *msg);

char *JNU_GetStringNativeChars(JNIEnv *env, jstring jstr);

jvalue
JNU_CallMethodByName(JNIEnv *env, jboolean *hasException, jobject obj, const char *name, const char *descriptor, ...);


#endif