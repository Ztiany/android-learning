LOCAL_PATH := $(call my-dir)

#定义第一个静态模块
include $(CLEAR_VARS)

LOCAL_MODULE    := MathLib
LOCAL_SRC_FILES := lib_static.c
#BUILD_STATIC_LIBRARY用于指定系统生成静态库
include $(BUILD_STATIC_LIBRARY)


#定义一个动态库，并且依赖第一个静态库
include $(CLEAR_VARS)

LOCAL_MODULE := Math
LOCAL_SRC_FILES := lib_share.c

#LOCAL_STATIC_LIBRARIES表示引用一个静态库
LOCAL_STATIC_LIBRARIES := MathLib

#LOCAL_LDLIBS用于添加一个本地依赖库，这里添加Android Log库
LOCAL_LDLIBS += -llog

include $(BUILD_SHARED_LIBRARY)