LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)
LOCAL_MODULE := gmath
LOCAL_SRC_FILES := gmath.c
#表示编译成静态库
include $(BUILD_STATIC_LIBRARY)