cmake_minimum_required(VERSION 3.4.1)

#6.0及以后动态库不行，静态库可以
#add_library(
#        7zr
#        SHARED
#        IMPORTED
#)
#set_target_properties()

#根据Android.mk引入头文件
#设置头文件查找目录
include_directories(
        lib7zr/CPP/7zip/Archive
        lib7zr/CPP/7zip/Archive/7z
        lib7zr/CPP/7zip/Archive/BZip2
        lib7zr/CPP/7zip/Archive/Common
        lib7zr/CPP/7zip/Archive/GZip
        lib7zr/CPP/7zip/Archive/Cab
        lib7zr/CPP/7zip/Archive/Lzma
        lib7zr/CPP/7zip/Archive/Tar
        lib7zr/CPP/7zip/Archive/Zip
        lib7zr/CPP/7zip/Archive/Split
        lib7zr/CPP/7zip/Archive/Z
        lib7zr/CPP/7zip/Compress
        lib7zr/CPP/7zip/Crypto
        lib7zr/CPP/7zip/UI/Console
        lib7zr/CPP/7zip/UI/Common
        lib7zr/CPP/Windows
        lib7zr/CPP/Common
        lib7zr/CPP/7zip/Common
        lib7zr/C
        lib7zr/CPP/myWindows
        lib7zr/CPP
        lib7zr/CPP/include_windows
)

#Android 6.0以后使用这种方式，设置库查找目录
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -L${CMAKE_SOURCE_DIR}/../jniLibs/${CMAKE_ANDROID_ARCH_ABI}")

add_library(native-lib SHARED native-lib.cpp)

target_link_libraries(native-lib 7zr log)