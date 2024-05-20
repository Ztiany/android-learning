cmake_minimum_required(VERSION 3.4.1)
aux_source_directory(. SOURCE)
add_library(mmkv-simple SHARED ${SOURCE})
target_link_libraries(mmkv-simple log)