# Android Gradle Plugin3.0.x TransformAPI Sample

使用 Android 的 Transform 和 Javassist 在项目构建阶段修改 class 文件，注入自定义代码。

## Transform API 介绍

Starting with 1.5.0-beta1, the Gradle plugin includes a Transform API allowing 3rd party plugins to manipulate compiled class files before they are converted to dex files.

## 注意

如果想要直接以源码的方式引用插件，则插件项目名称必须为 `buildsrc`。

## 环境

- Android Gradle Plugin3.0.1
- AndroidStudio3.0.1

## AGP 行为变更

参考 <https://developer.android.com/studio/releases/gradle-plugin>