LOCAL_PATH := $(call my-dir)

#清理变量
include $(CLEAR_VARS)

#外部库的路径
EXT_LIB_ROOT := $(LOCAL_PATH)/../../../distribution

#$表示调用函数，$(TARGET_ARCH_ABI)用于获取当前够建的cpu架构
#PREBUILT用于告诉编译器这个库文件已经编译好了，可以直接在下面的编译的中引用。

# 引入静态库
# 指定引入这个静态库的模块名
LOCAL_MODULE := local_gmath
# 指定静态库.a位置
LOCAL_SRC_FILES := $(EXT_LIB_ROOT)/gmath/lib/$(TARGET_ARCH_ABI)/libgmath.a
# 指定头文件位置
LOCAL_EXPORT_C_INCLUDES := $(EXT_LIB_ROOT)/gmath/include
#PREBUILT_STATIC_LIBRARY表示已经编译好了，直接引入这个静态库
include $(PREBUILT_STATIC_LIBRARY)

#引入动态库
include $(CLEAR_VARS)
LOCAL_MODULE := local_gperf
LOCAL_SRC_FILES := $(EXT_LIB_ROOT)/gperf/lib/$(TARGET_ARCH_ABI)/libgperf.so
LOCAL_EXPORT_C_INCLUDES := $(EXT_LIB_ROOT)/gperf/include
#PREBUILT_SHARED_LIBRARY表示已经编译好了，直接引入这个动态库
include $(PREBUILT_SHARED_LIBRARY)


# 够建App自己的动态库
include $(CLEAR_VARS)

#C指令
LOCAL_CFLAGS := -std=gnu++11

LOCAL_MODULE    := hello-libs
LOCAL_SRC_FILES := hello-libs.cpp

#引入android system库：log、android
LOCAL_LDLIBS    := -llog -landroid

# 声明包含静态库local_gmath
LOCAL_STATIC_LIBRARIES := local_gmath
# 声明包含动态库local_gperf
LOCAL_SHARED_LIBRARIES := local_gperf

include $(BUILD_SHARED_LIBRARY)