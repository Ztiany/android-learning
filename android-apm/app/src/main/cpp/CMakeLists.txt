cmake_minimum_required(VERSION 3.22.1)
project("apm")

find_package(bytehook REQUIRED CONFIG)
find_package(shadowhook REQUIRED CONFIG)


# build memory_tracker library
add_library(
        "memory_tracker"
        SHARED
        memory/memory_tracker.cpp
        common/native_util.cpp
)
target_link_libraries("memory_tracker" android log bytehook::bytehook)


# build memory_allocation library
add_library(
        "memory_allocation"
        SHARED
        memory/memory_allocation.cpp
        common/native_util.cpp
)
target_link_libraries("memory_allocation" android log)


# build bitmap_monitor library
add_library(
        "bitmap_monitor"
        SHARED
        bitmap/bitmap_monitor.cpp
        bitmap/bitmap_saver.cpp
        common/native_util.cpp
)
target_link_libraries("bitmap_monitor" android log jnigraphics shadowhook::shadowhook)