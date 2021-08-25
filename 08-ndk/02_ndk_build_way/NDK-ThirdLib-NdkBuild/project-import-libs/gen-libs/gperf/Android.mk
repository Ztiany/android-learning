LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)
LOCAL_MODULE := gperf
LOCAL_SRC_FILES := gperf.c
#表示生成动态库
include $(BUILD_SHARED_LIBRARY)