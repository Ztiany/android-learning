cmake_minimum_required(VERSION 3.4.1)

set(CMAKE_C_FLAGS "-Wno-error=format-security")

include_directories(BZIP_SOURCE src/main/cpp/bzip)
aux_source_directory(BZIP_SOURCE src/main/cpp/bzip)
set(SOURCE src/main/cpp/bspatch.c)

add_library(native-lib SHARED ${SOURCE} ${BZIP_SOURCE})

find_library(log-lib log )
target_link_libraries(native-lib ${log-lib} )
