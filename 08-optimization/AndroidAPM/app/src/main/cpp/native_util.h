#ifndef ANDROID_APM_NATIVE_UTIL_H
#define ANDROID_APM_NATIVE_UTIL_H

#include <android/log.h>

#define LOG_TAG                 "native_tag"
#define LOG(fmt, ...)           __android_log_print(ANDROID_LOG_INFO, LOG_TAG, fmt, ##__VA_ARGS__)

#endif //ANDROID_APM_NATIVE_UTIL_H
