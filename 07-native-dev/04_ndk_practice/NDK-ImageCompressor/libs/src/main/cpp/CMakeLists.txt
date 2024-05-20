cmake_minimum_required(VERSION 3.4.1)

# 引入头文件
include_directories(libjpeg)

# 添加libjpeg库（静态方式）
add_library(libjpeg STATIC IMPORTED)
set_target_properties(libjpeg PROPERTIES IMPORTED_LOCATION ${CMAKE_SOURCE_DIR}/../../../libs/${ANDROID_ABI}/libturbojpeg.a)

# 添加需要编译的代码
add_library(native-lib SHARED native-lib.cpp)
# 找到Android 原生库
find_library(log-lib log)
find_library(jnigraphics-lib jnigraphics)
# 链接
target_link_libraries(native-lib libjpeg ${log-lib} ${jnigraphics-lib})

