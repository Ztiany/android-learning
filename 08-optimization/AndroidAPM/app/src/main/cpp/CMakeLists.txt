cmake_minimum_required(VERSION 3.22.1)
project("apm")

# ByteHook is an Android PLT hook library which supports armeabi-v7a, arm64-v8a, x86 and x86_64.
find_package(bytehook REQUIRED CONFIG)

# build apm library
add_library(
        ${CMAKE_PROJECT_NAME}
        SHARED
        memory_tracker.cpp
        memory_allocation.cpp
        native_util.cpp
)
target_link_libraries(${CMAKE_PROJECT_NAME} android log bytehook::bytehook)