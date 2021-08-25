#ifndef JNISAMPLE_UTILS_H
#define JNISAMPLE_UTILS_H

#include <jni.h>

char *jstring2Cstring(JNIEnv *env, jstring jstr);

jstring cstring2Jstring(JNIEnv *env, char *cstr);

#endif