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
