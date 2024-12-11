#ifndef ANDROID_APM_NATIVE_UTIL_H
#define ANDROID_APM_NATIVE_UTIL_H

#include <android/log.h>

#define LOG_TAG                 "native_tag"
#define LOG(fmt, ...)           __android_log_print(ANDROID_LOG_INFO, LOG_TAG, fmt, ##__VA_ARGS__)
#define log_d(fmt, ...)           __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, fmt, ##__VA_ARGS__)
#define log_i(fmt, ...)           __android_log_print(ANDROID_LOG_INFO, LOG_TAG, fmt, ##__VA_ARGS__)
#define log_w(fmt, ...)           __android_log_print(ANDROID_LOG_WARN, LOG_TAG, fmt, ##__VA_ARGS__)
#define log_e(fmt, ...)           __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, fmt, ##__VA_ARGS__)

int get_api_level();

long long get_current_time_millis();

#endif //ANDROID_APM_NATIVE_UTIL_H
