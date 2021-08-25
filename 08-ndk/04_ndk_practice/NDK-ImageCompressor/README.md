# 在 Android 中使用 [libjpeg-turbo](https://github.com/libjpeg-turbo/libjpeg-turbo)

- 1 下载 libjpeg-turbo 源码

- 2 在 linux 下编译 libjpeg-turbo，获取对应的 `.so` 共享库或者 `.a` 静态库。

```shell
# 编译脚本，具体参考：https://github.com/libjpeg-turbo/libjpeg-turbo/blob/master/BUILDING.md

# 编译 x86 abi
NDK_PATH=/home/ztiany/developer/android-ndk-r20-linux-x86_64/android-ndk-r20

TOOLCHAIN=clang

ANDROID_VERSION=19

cmake -G"Unix Makefiles" \
  -DANDROID_ABI=x86 \
  -DANDROID_PLATFORM=android-${ANDROID_VERSION} \
  -DANDROID_TOOLCHAIN=${TOOLCHAIN} \
  -DCMAKE_TOOLCHAIN_FILE=${NDK_PATH}/build/cmake/android.toolchain.cmake \

make

# 编译 arme-v7a abi
NDK_PATH=/home/ztiany/developer/android-ndk-r20-linux-x86_64/android-ndk-r20

TOOLCHAIN=clang

ANDROID_VERSION=19

cmake -G"Unix Makefiles" \
  -DANDROID_ABI=armeabi-v7a \
  -DANDROID_ARM_MODE=arm \
  -DANDROID_PLATFORM=android-${ANDROID_VERSION} \
  -DANDROID_TOOLCHAIN=${TOOLCHAIN} \
  -DCMAKE_ASM_FLAGS="--target=arm-linux-androideabi${ANDROID_VERSION}" \
  -DCMAKE_TOOLCHAIN_FILE=${NDK_PATH}/build/cmake/android.toolchain.cmake \

make
```

- 3 在 Android 中使用 libjpeg-turbo。
