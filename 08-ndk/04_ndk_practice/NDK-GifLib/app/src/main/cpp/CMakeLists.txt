cmake_minimum_required(VERSION 3.4.1)

file(GLOB libgif_src ${CMAKE_SOURCE_DIR}/giflib/*.c)

include_directories(${CMAKE_SOURCE_DIR}/giflib)

add_library(
        native-lib
        SHARED
        ${libgif_src}
        JavaInputStream.cpp
        GifFrame.cpp
        native-lib.cpp)

# native 日志库
find_library(
        log-lib
        log)

# Android 原生应用 API，用于支持 native 层读取 assets 资源。
# https://developer.android.com/ndk/guides/stable_apis#android_native_application_apis
find_library(
        android-lib
        android
)

# native bitmap 操作
find_library(
        jnigraphics-lib
        jnigraphics
)

target_link_libraries(
        native-lib
        ${android-lib}
        ${jnigraphics-lib}
        ${log-lib})