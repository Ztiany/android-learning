cmake_minimum_required(VERSION 3.4.1)
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=gnu++11")

# DIR
set(cpp_dir ${CMAKE_SOURCE_DIR}/src/main/cpp)

message("------------------------cmake: " ${cpp_dir})
message("------------------------cmake: " ${CMAKE_CXX_FLAGS})

# Fmod
add_library(lib_fmod SHARED IMPORTED )
set_target_properties(lib_fmod PROPERTIES IMPORTED_LOCATION  ${cpp_dir}/lib/${ANDROID_ABI}/libfmod.so)
add_library(lib_fmodL SHARED IMPORTED)
set_target_properties(lib_fmodL PROPERTIES IMPORTED_LOCATION  ${cpp_dir}/lib/${ANDROID_ABI}/libfmodL.so)
include_directories(${cpp_dir}/inc)

# Android log
find_library(log-lib  log)

# 构建Lib
# effects.cpp和play_sound.cpp不要同时编译，它们都由common_platform.cpp调用，是FMOD_main方法的不同实现
add_library(native-lib SHARED src/main/cpp/common_platform.cpp  src/main/cpp/common.cpp src/main/cpp/voice_changer.cpp src/main/cpp/play_sound.cpp)

target_link_libraries(native-lib  ${log-lib} android lib_fmod lib_fmodL)