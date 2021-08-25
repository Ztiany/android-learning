#include <jni.h>
#include <string>
#include <android/log.h>

#include <string>

#include <fcntl.h>
#include <unistd.h>
#include <sys/stat.h>
#include <sys/mman.h>

#define NATIVE_LOG "native"
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG, NATIVE_LOG, __VA_ARGS__)

using namespace std;

int8_t *m_ptr;
int32_t m_size;
int m_fd;

extern "C"
JNIEXPORT void JNICALL
Java_me_ztiany_mmkv_simulation_NativeBridge_mmapWrite(JNIEnv *env, jobject thiz) {
    std::string data("abcdefg");
    memcpy(m_ptr, data.data(), data.size());
}

extern "C"
JNIEXPORT void JNICALL
Java_me_ztiany_mmkv_simulation_NativeBridge_mmapInit(JNIEnv *env, jobject thiz, jstring path) {
    const char *c_path = env->GetStringUTFChars(path, nullptr);

    LOGD("path = %s", c_path);

    //系统调用打开文件
    m_fd = open(c_path, O_RDWR | O_CREAT, S_IRWXU);

    //获得一页内存大小：Linux采用了分页来管理内存，即内存的管理中，内存是以页为单位，一般的 32 位系统一页为 4096 个字节
    m_size = getpagesize()/4;
    LOGD("page size = %d", m_size);

    //设置文件的大小为1页，使用空字节填充
    ftruncate(m_fd, m_size);

    env->ReleaseStringUTFChars(path, c_path);

    // m_size：映射区的长度。 需要是整数页个字节    byte[]
    m_ptr = (int8_t *) mmap(nullptr, m_size, PROT_READ | PROT_WRITE, MAP_SHARED, m_fd, 0);
    LOGD("m_ptr = %p", m_ptr);
}

extern "C"
JNIEXPORT void JNICALL
Java_me_ztiany_mmkv_simulation_NativeBridge_mmapRead(JNIEnv *env, jobject thiz) {
    //申请内存
    char *buf = static_cast<char *>(malloc(100));
    memcpy(buf, m_ptr, 100);
    std::string data(buf);
    free(buf);
    LOGD("read data: %s", data.c_str());
}

extern "C"
JNIEXPORT void JNICALL
Java_me_ztiany_mmkv_simulation_NativeBridge_destroy(JNIEnv *env, jobject thiz) {
    //取消映射
    munmap(m_ptr, m_size);
    //关闭文件
    close(m_fd);
}