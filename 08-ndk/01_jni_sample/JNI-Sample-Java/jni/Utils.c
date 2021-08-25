#include "Utils.h"
#include <malloc.h>
#include <string.h>

/**
 * Java String转换为C字符串，转换后的字符串是可以修改的
 */
char *jstring2Cstring(JNIEnv *env, jstring jstr) {
    char *rtn = NULL;
    jclass clsstring = (*env)->FindClass(env, "java/lang/String");
    jstring strencode = (*env)->NewStringUTF(env, "UTF-8");
    jmethodID mid = (*env)->GetMethodID(env, clsstring, "getBytes", "(Ljava/lang/String;)[B");
    // String .getByte("GB2312");
    jbyteArray barr = (jbyteArray) (*env)->CallObjectMethod(env, jstr, mid, strencode);
    jsize alen = (*env)->GetArrayLength(env, barr);
    //动态的获取内在堆内存中，需要被释放
    jbyte *ba = (*env)->GetByteArrayElements(env, barr, NULL);

    if (alen > 0) {
        rtn = (char *) malloc(alen + 1);         //"\0" c中字符串以\0结尾
        memcpy(rtn, ba, alen);
        rtn[alen] = 0;//让最后一个字符='\0',表示是字符串的结尾
    }

    (*env)->ReleaseByteArrayElements(env, barr, ba, 0);  //释放内存
    return rtn;
}

//解决某些情况下可能的中文乱码的问题，调用Java中String的构造函数来创建字符串
jstring cstring2Jstring(JNIEnv *env, char *c_str) {

    //获取String的构造函数
    jclass str_cls = (*env)->FindClass(env, "java/lang/String");
    jmethodID constructor_mid = (*env)->GetMethodID(env, str_cls, "<init>", "([BLjava/lang/String;)V");

    //创建字节数组
    jbyteArray bytes = (*env)->NewByteArray(env, strlen(c_str));

    //byte数组赋值：从c_str这个字符数组，复制到bytes这个字符数组
    (*env)->SetByteArrayRegion(env, bytes, 0, strlen(c_str), c_str);

    //字符编码jstring
    jstring charsetName = (*env)->NewStringUTF(env, "UTF-8");

    //调用构造函数，返回编码之后的jstring
    return (*env)->NewObject(env, str_cls, constructor_mid, bytes, charsetName);
}

jstring JNU_NewStringNative(JNIEnv *env, const char *str) {
    jstring result;
    jbyteArray bytes = 0;
    jclass Class_java_lang_String = (*env)->FindClass(env, "java/lang/String");
    jmethodID MID_String_init = (*env)->GetMethodID(env, Class_java_lang_String, "<init>", "([B)V");
    int len;
    //EnsureLocalCapacity 确保在当前线程中至少可以创建给定数量的本地引用。成功时返回0;否则返回一个负数并抛出一个OutOfMemoryError。
    if ((*env)->EnsureLocalCapacity(env, 2) < 0) {
        return NULL; /* out of memory error */
    }
    len = strlen(str);
    bytes = (*env)->NewByteArray(env, len);
    if (bytes != NULL) {
        (*env)->SetByteArrayRegion(env, bytes, 0, len, (jbyte *) str);
        result = (*env)->NewObject(env, Class_java_lang_String, MID_String_init, bytes);
        (*env)->DeleteLocalRef(env, bytes);
        return result;
    }
    /* else fall through */
    return NULL;
}

char *JNU_GetStringNativeChars(JNIEnv *env, jstring jstr) {
    jbyteArray bytes = 0;
    jthrowable exc;
    char *result = 0;

    jclass Class_java_lang_String = (*env)->FindClass(env, "java/lang/String");
    jmethodID MID_String_getBytes = (*env)->GetMethodID(env, Class_java_lang_String, "getBytes", "()[B");

    if ((*env)->EnsureLocalCapacity(env, 2) < 0) {
        return 0; /* out of memory error */
    }
    bytes = (*env)->CallObjectMethod(env, jstr, MID_String_getBytes);
    exc = (*env)->ExceptionOccurred(env);
    if (!exc) {
        jint len = (*env)->GetArrayLength(env, bytes);
        result = (char *) malloc(len + 1);
        if (result == 0) {
            JNU_ThrowByName(env, "java/lang/OutOfMemoryError", 0);
            (*env)->DeleteLocalRef(env, bytes);
            return 0;
        }
        (*env)->GetByteArrayRegion(env, bytes, 0, len, (jbyte *) result);
        result[len] = 0; /* NULL-terminate */
    } else {
        (*env)->DeleteLocalRef(env, exc);
    }
    (*env)->DeleteLocalRef(env, bytes);
    return result;
}

void JNU_ThrowByName(JNIEnv *env, const char *name, const char *msg) {
    jclass cls = (*env)->FindClass(env, name);
    /*if cls is NULL, an exception has already been thrown */
    if (cls != NULL) {
        (*env)->ThrowNew(env, cls, msg);
    }
    /*free the local ref */
    (*env)->DeleteLocalRef(env, cls);
}

jvalue
JNU_CallMethodByName(JNIEnv *env, jboolean *hasException, jobject obj, const char *name, const char *descriptor, ...) {

    va_list args;
    jclass clazz;
    jmethodID mid;
    jvalue result;

    if ((*env)->EnsureLocalCapacity(env, 2) == JNI_OK) {

        clazz = (*env)->GetObjectClass(env, obj);
        mid = (*env)->GetMethodID(env, clazz, name, descriptor);

        if (mid) {
            const char *p = descriptor;
            /* skip over argument types to find out the  return type */
            while (*p != ')') {
                p++;/* skip ')' */
            }
            p++;
            va_start(args, descriptor);//可变参数的处理逻辑
            switch (*p) {
                case 'V':
                    (*env)->CallVoidMethodV(env, obj, mid, args);
                    break;
                case '[':
                case 'L':
                    result.l = (*env)->CallObjectMethodV(env, obj, mid, args);
                    break;
                case 'Z':
                    result.z = (*env)->CallBooleanMethodV(env, obj, mid, args);
                    break;
                case 'B':
                    result.b = (*env)->CallByteMethodV(env, obj, mid, args);
                    break;
                case 'C':
                    result.c = (*env)->CallCharMethodV(env, obj, mid, args);
                    break;
                case 'S':
                    result.s = (*env)->CallShortMethodV(env, obj, mid, args);
                    break;
                case 'I':
                    result.i = (*env)->CallIntMethodV(env, obj, mid, args);
                    break;
                case 'J':
                    result.j = (*env)->CallLongMethodV(env, obj, mid, args);
                    break;
                case 'F':
                    result.f = (*env)->CallFloatMethodV(env, obj, mid, args);
                    break;
                case 'D':
                    result.d = (*env)->CallDoubleMethodV(env, obj, mid, args);
                    break;
                default:
                    (*env)->FatalError(env, "illegal descriptor");
            }
            va_end(args);
        }
        (*env)->DeleteLocalRef(env, clazz);
    }
    if (hasException) {
        *hasException = (*env)->ExceptionCheck(env);
    }
    return result;
}