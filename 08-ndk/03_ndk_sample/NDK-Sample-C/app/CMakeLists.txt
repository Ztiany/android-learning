cmake_minimum_required(VERSION 3.4.1)

add_library(native-lib SHARED
        src/main/cpp/Utils.c
        src/main/cpp/native-lib.c
        src/main/cpp/pthread-lib.c
        )

find_library(log-lib log)

target_link_libraries(native-lib ${log-lib})