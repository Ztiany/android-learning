#ifndef NDK_SAMPLE_CPP_UTILS_H
#define NDK_SAMPLE_CPP_UTILS_H

#include <android/log.h>

#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR,"Native",__VA_ARGS__)
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG, "Native", __VA_ARGS__)
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, "Native", __VA_ARGS__)

#endif //NDK_SAMPLE_CPP_UTILS_H
