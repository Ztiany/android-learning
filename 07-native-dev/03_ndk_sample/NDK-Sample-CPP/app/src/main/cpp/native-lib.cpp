#include <jni.h>
#include <string>
#include "FileLock.h"
#include "Utils.h"
#include  <unistd.h>

FileLock *fileLock;

extern "C"
JNIEXPORT void JNICALL
Java_me_ztiany_ndk_cpp_flock_NativeFileLockTester_nativeFileLockInit(
        JNIEnv *env,
        jobject thiz,
        jstring file_name
) {

    const char *path = env->GetStringUTFChars(file_name, nullptr);

    int pid = getpid();
    LOGD("进程 %d 开始初始化 %s", pid, path);

    int fd = open(path, O_RDWR | O_CREAT, S_IRUSR | S_IWUSR | S_IXUSR);
    if (fd == -1) {
        LOGD("进程 %d 打开文件错误 %s", pid, path);
        return;
    }

    LOGD("进程 %d 文件锁初始化成功 %s", pid, path);
    fileLock = new FileLock(fd);

    env->ReleaseStringUTFChars(file_name, path);
}

extern "C"
JNIEXPORT void JNICALL
Java_me_ztiany_ndk_cpp_flock_NativeFileLockTester_nativeFileLockDoLock(
        JNIEnv *env,
        jobject thiz,
        jboolean isExclusive,
        jboolean wait
) {

    if (!fileLock) {
        LOGD("未初始化");
        return;
    }

    LockType lockType = isExclusive ? Exclusive : Shared;

    if (wait) {
        fileLock->lock(lockType);
    } else {
        fileLock->tryLock(lockType);
    }
}

extern "C"
JNIEXPORT void JNICALL
Java_me_ztiany_ndk_cpp_flock_NativeFileLockTester_nativeFileLockUnlock(
        JNIEnv *env,
        jobject thiz,
        jboolean isExclusive
) {

    if (!fileLock) {
        LOGD("未初始化");
        return;
    }

    LockType lockType = isExclusive ? Exclusive : Shared;
    fileLock->unLock(lockType);
}

extern "C"
JNIEXPORT void JNICALL
Java_me_ztiany_ndk_cpp_flock_NativeFileLockTester_nativeFileLockDestroy(
        JNIEnv *env,
        jobject thiz
) {

    if (fileLock) {
        delete fileLock;
    }

}

extern "C"
JNIEXPORT void JNICALL
Java_me_ztiany_ndk_cpp_flock_NativeFileLockTester_nativeFileLockTest(
        JNIEnv *env,
        jobject thiz,
        jstring file_name
) {

    const char *path = env->GetStringUTFChars(file_name, nullptr);
    int pid = getpid();
    LOGD("进程 %d 开始初始化 %s", pid, path);
    env->ReleaseStringUTFChars(file_name, path);

}