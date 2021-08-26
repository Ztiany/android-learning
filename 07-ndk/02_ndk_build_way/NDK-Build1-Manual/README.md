# AndroidStudio NDK 开发(OLD)

1. 徒手编写 `Android.mk` 和 `Application.mk` 文件。
2. 使用 `javah` 命令生成头文件，比如 `javah com.ztiany.sample1.JNIObj`，可以直接对 `.java` 文件使用此命令。
3. 使用 `nkd-build` 命令生成 `.so` 库，把生成的文件复制到 jniLibs 目录下。

>注意在 `gradle.properties` 下添加 `android.useDeprecatedNdk=true`。
