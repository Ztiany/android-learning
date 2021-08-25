#设置构建本机库所需的C的最小版本。
cmake_minimum_required(VERSION 3.4.1)

set(CMAKE_VERBOSE_MAKEFILE on)
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=gnu++11")

#message可以输出日志
message("CMAKE_SOURCE_DIR : " ${CMAKE_SOURCE_DIR})

# configure import libs
set(distribution_DIR ${CMAKE_SOURCE_DIR}/distribution)

# 添加一个静态库
add_library(lib_gmath STATIC IMPORTED )
# set_target_properties用于给目标设置属性，参数为：目标库，需要设置的属性名，属性值
set_target_properties(lib_gmath PROPERTIES IMPORTED_LOCATION  ${distribution_DIR}/gmath/lib/${ANDROID_ABI}/libgmath.a)

# 添加一个动态库
add_library(lib_gperf SHARED IMPORTED )
set_target_properties(lib_gperf PROPERTIES IMPORTED_LOCATION  ${distribution_DIR}/gperf/lib/${ANDROID_ABI}/libgperf.so)

# 包含第三方库的头文件目录
include_directories(${distribution_DIR}/gmath/include ${distribution_DIR}/gperf/include)

# find_library() 命令在 CMake 构建脚本中用来定位 NDK 库
#添加一个预构建的库，并设置其名称，因为CMake搜索路径中默认包括了系统库，只需指定要添加的公共NDK库的名称即可
#这里添加的是NDK库的log库
find_library(log-lib log)


#添加一个动态库，即我们自己的本地代码
add_library(native-lib SHARED src/main/cpp/native-lib.c )
#指定要链接到目标库的库。可以链接多个库，第一个为本地库
target_link_libraries(native-lib ${log-lib} android lib_gperf lib_gmath )