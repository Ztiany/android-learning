#include "Utils.h"

#include <malloc.h>
#include <string.h>

/*
 参考：https://toutiao.io/posts/9kk67m/preview

 Java 内部是使用 16bit 的 unicode 编码（UTF-16）来表示字符串的，无论中文英文都是 2 字节；
 Jni 内部是使用 MUTF-8 编码来表示字符串的，MUTF-8 是变长编码的 unicode，一般 ascii 字符是 1 字节，中文是 3 字节；
 c/c++ 使用的是原始数据，ascii 就是一个字节了，中文一般是 GB2312 编码，用两个字节来表示一个汉字。

 情况：
    1. 通过 Jni 的 NewStringUTF 方法把 C++ 的字符串转换为jstring时，如果入参为emoji表情或其他非 Modified UTF8 编码字符将导致 Crash。
    2. 使用 Jni 的 GetStringUTFChars 方法把 jstring 转换为 C++ 字符串时得到的字符串编码为 Modified UTF8，如果直接传递到服务端或其他使用方，emoji 表情将出现解析失败的问题。

方案：
    情况1：反射调用 Java String，构建标准字符编码字符串。
    情况2：与其他组件进行交互或与服务端进行通信时要注意不要误把变种 Modified UTF-8 当成 UTF-8 数据，可以先将 Java 的 String 用 UTF-8 编码转换成 byte 数组，再转换成 C/C++ 字符串即可保证字符编码为 UTF-8。
 */

/**
 * Java String 转换为标准 UTF-8 字符串，转换后的字符串是可以修改的。
 */
char *jString2CString(JNIEnv *env, jstring jstr) {
    char *rtn = NULL;
    jclass clsstring = (*env)->FindClass(env, "java/lang/String");
    jstring strencode = (*env)->NewStringUTF(env, "UTF-8");
    jmethodID mid = (*env)->GetMethodID(env, clsstring, "getBytes", "(Ljava/lang/String;)[B");
    // String .getByte("UTF-8");
    jbyteArray barr = (jbyteArray) (*env)->CallObjectMethod(env, jstr, mid, strencode);
    jsize alen = (*env)->GetArrayLength(env, barr);
    jbyte *ba = (*env)->GetByteArrayElements(env, barr, 0);
    if (alen > 0) {
        rtn = (char *) malloc(alen + 1);         //"\0" c中字符串以\0结尾
        memcpy(rtn, ba, alen);
        rtn[alen] = 0;//让最后一个字符='\0',表示是字符串的结尾
    }
    (*env)->ReleaseByteArrayElements(env, barr, ba, 0);
    return rtn;
}

void cStringToJString(JNIEnv *env, char *str, jobject *receive) {
    jclass strClazz = (*env)->FindClass(env, "java/lang/String");
    jmethodID constructorId = (*env)->GetMethodID(env, strClazz, "<init>", "([BLjava/lang/String;)V");
    //jbyte -> char
    //jbyteArray -> char[]
    jbyteArray bytes = (*env)->NewByteArray(env, strlen(str));
    //byte数组赋值 从str这个字符数组，复制到bytes这个字符数组
    (*env)->SetByteArrayRegion(env, bytes, 0, strlen(str), str);
    //字符编码jstring
    jstring charsetName = (*env)->NewStringUTF(env, "UTF-8");

    //调用构造函数，返回编码之后的jstring
    *receive = ((*env)->NewObject(env, strClazz, constructorId, bytes, charsetName));
}