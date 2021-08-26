LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)
LOCAL_MODULE := Hello
LOCAL_SRC_FILES := Hello.c
#添加Android Log库
LOCAL_LDLIBS += -llog
include $(BUILD_SHARED_LIBRARY)