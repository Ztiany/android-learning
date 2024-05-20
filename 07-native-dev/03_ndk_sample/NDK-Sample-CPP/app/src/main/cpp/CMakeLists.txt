# documentation: https://d.android.com/studio/projects/add-native-code.html
cmake_minimum_required(VERSION 3.4.1)

file(GLOB native-src ./*.cpp  ./*.h)

add_library(native-lib SHARED ${native-src})

find_library(log-lib log)

target_link_libraries(native-lib ${log-lib})