#include "com_ztiany_jni_sample_JniBridge.h"
#include "Utils.h"
#include "FileUtils.h"
#include <string.h>

/*
 * Class:     com_ztiany_jni_sample_JniBridge
 * Method:    stringFromC
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_ztiany_jni_sample_JniBridge_stringFromC(JNIEnv *env, jclass thiz) {
    char *str = "你好，Java";
    return (*env)->NewStringUTF(env, str);
}

/*
 * Class:     com_ztiany_jni_sample_JniBridge
 * Method:    intFromC
 * Signature: (II)I
 */
JNIEXPORT jint JNICALL Java_com_ztiany_jni_sample_JniBridge_intFromC(JNIEnv *env, jobject thiz, jint a, jint b) {
    jint add = a + b;
    return add;
}

/*
 * Class:     com_ztiany_jni_sample_JniBridge
 * Method:    addArray
 * Signature: ([II)[I
 */
JNIEXPORT jintArray JNICALL
Java_com_ztiany_jni_sample_JniBridge_addArray(JNIEnv *env, jobject thiz, jintArray jArray, jint add) {

    //获取数组的长度
    jsize length = (*env)->GetArrayLength(env, jArray);
    //获取数组的指针
    jint *arr = (*env)->GetIntArrayElements(env, jArray, JNI_FALSE);
    //加
    for (int i = 0; i < length; ++i) {
        arr[i] = arr[i] + add;
    }
    //释放数组
    (*env)->ReleaseIntArrayElements(env, jArray, arr, JNI_FALSE);
    return jArray;
}

/*
 * Class:     com_ztiany_jni_sample_JniBridge
 * Method:    bubbleSort
 * Signature: ([I)V
 */
JNIEXPORT void JNICALL Java_com_ztiany_jni_sample_JniBridge_bubbleSort(JNIEnv *env, jobject thiz, jintArray jintArr) {

    //获取数组指针
    jint *jintArrPointer = (*env)->GetIntArrayElements(env, jintArr, 0);//java int数组 转 c int数组
    //获取数组长度
    int len = (*env)->GetArrayLength(env, jintArr);//获取字符穿的长度
    int x;
    int y;
    for (x = 0; x < len - 1; x++) {
        for (y = 0; y < len - x - 1; y++) {
            if (jintArrPointer[y] > jintArrPointer[y + 1]) {
                int temp = jintArrPointer[y];
                jintArrPointer[y] = jintArrPointer[y + 1];
                jintArrPointer[y + 1] = temp;
            }
        }
    }
    (*env)->ReleaseIntArrayElements(env, jintArr, jintArrPointer, 0);
}

/*
 * Class:     com_ztiany_jni_sample_JniBridge
 * Method:    encryption
 * Signature: (Ljava/lang/String;)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_ztiany_jni_sample_JniBridge_encryption(JNIEnv *env, jobject thiz, jstring jstr) {
    char *cArr = jstring2Cstring(env, jstr);
    const char *cHello = "hello";
    strcat(cArr, cHello);
    return (*env)->NewStringUTF(env, cArr);
}

/*
 * Class:     com_ztiany_jni_sample_JniBridge
 * Method:    callJava
 * Signature: (Ljava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_com_ztiany_jni_sample_JniBridge_callJava
        (JNIEnv *env, jobject thiz, jstring jStr) {

    jclass clz = (*env)->FindClass(env, "com/ztiany/jni/sample/JniBridge");

    jmethodID showMessage = (*env)->GetStaticMethodID(env, clz, "showMessage", "(Ljava/lang/String;)V");
    (*env)->CallStaticVoidMethod(env, clz, showMessage, jStr);
}

/*
 * Class:     com_ztiany_jni_sample_JniBridge
 * Method:    throwError
 * Signature: (Ljava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_com_ztiany_jni_sample_JniBridge_throwError
        (JNIEnv *env, jobject thiz, jstring message) {

    //找到EOFException类
    jclass class_EOF = (*env)->FindClass(env, "java/io/EOFException");
    //获取构造函数
    //jmethodID id = (*env)->GetMethodID(env,class_EOF,"<init>", "()V");
    jmethodID id = (*env)->GetMethodID(env, class_EOF, "<init>", "(Ljava/lang/String;)V");
    //调用构造函数创建对象
    jthrowable obj_exc = (*env)->NewObject(env, class_EOF, id, message);
    //抛出异常
    if (JNI_TRUE) {
        (*env)->Throw(env, obj_exc);
        return;
    }

    //这是另外一种方式
    //(*env)->ThrowNew(env,(*env)->FindClass(env,"java/io/EOFException"), "Unexpected end of file");
}

JNIEXPORT jboolean JNICALL Java_com_ztiany_jni_sample_JniBridge_splitFile
        (JNIEnv *env, jobject thiz, jstring path, jstring path_pattern, jint file_num) {
    const char *path_c = (*env)->GetStringUTFChars(env, path, NULL);
    const char *path_pattern_c = (*env)->GetStringUTFChars(env, path_pattern, NULL);
    int count = file_num;
    bool isSuccess = split_file(path_c, path_pattern_c, count);

    (*env)->ReleaseStringUTFChars(env, path, path_c);
    (*env)->ReleaseStringUTFChars(env, path_pattern, path_pattern_c);

    if (isSuccess) {
        return JNI_TRUE;
    } else {
        return JNI_FALSE;
    }
}

JNIEXPORT jboolean JNICALL Java_com_ztiany_jni_sample_JniBridge_mergeFile
        (JNIEnv *env, jobject thiz, jstring path, jstring path_pattern, jint file_num) {
    const char *path_c = (*env)->GetStringUTFChars(env, path, NULL);
    const char *path_pattern_c = (*env)->GetStringUTFChars(env, path_pattern, NULL);
    int count = file_num;

    bool isSuccess = merge_file(path_pattern_c, count, path_c);

    (*env)->ReleaseStringUTFChars(env, path, path_c);
    (*env)->ReleaseStringUTFChars(env, path_pattern, path_pattern_c);

    if (isSuccess) {
        return JNI_TRUE;
    } else {
        return JNI_FALSE;
    }
}


/*
 * 动态注册的方法，Java可直接调用
 *
 * Class:     com_ztiany_jni_sample_JniBridge
 * Method:    dynamicRegisterFromJni
 * Signature: ()Ljava/lang/String;
 */
static jstring dynamicRegisterFromJni(JNIEnv *env, jobject thiz) {
    return (*env)->NewStringUTF(env, "动态注册调用成功");
}


//JNINativeMethod是一个结构体，这里初始化了一个JNINativeMethod数组，正是因为这个才可以动态调用任意 native 方法
JNINativeMethod nativeMethod[] = {{"dynamicRegisterFromJni", "()Ljava/lang/String;", (void *) dynamicRegisterFromJni}};


//此方法在jni库被加载时由JVM调用
JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM *jvm, void *reserved) {
    JNIEnv *env;
    if ((*jvm)->GetEnv(jvm, (void **) &env, JNI_VERSION_1_4) != JNI_OK) {
        return -1;
    }

    jclass clz = (*env)->FindClass(env, "com/ztiany/jni/sample/JniBridge");

    (*env)->RegisterNatives(env, clz, nativeMethod, sizeof(nativeMethod) / sizeof(nativeMethod[0]));

    return JNI_VERSION_1_4;
}


//在虚拟机关闭时调用
void JNI_OnUnload(JavaVM *vm, void *reserved) {
    printf("JVM unload");
}