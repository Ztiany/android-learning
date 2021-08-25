cmake_minimum_required(VERSION 3.8)
project(CForJNI)

set(CMAKE_C_STANDARD 99)
SET(LIBRARY_OUTPUT_PATH build)

include_directories(include)
include_directories(include/win32)

set(SOURCE_FILES Utils.c FileUtils.c native-lib.c)

add_library(CForJNI SHARED ${SOURCE_FILES})