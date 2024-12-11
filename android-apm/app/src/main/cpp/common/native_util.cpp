#include "native_util.h"
#include <sys/system_properties.h>
#include <linux/time.h>
#include <sys/time.h>

int get_api_level() {
    char sdk_version_str[PROP_VALUE_MAX];
    __system_property_get("ro.build.version.sdk", sdk_version_str);
    return atoi(sdk_version_str);
}

long long get_current_time_millis() {
    struct timeval time{};
    gettimeofday(&time, nullptr);
    return (long long) time.tv_sec * 1000 + time.tv_usec / 1000;
}