# Android Gradle Plugin7.x TransformAPI Sample

基于 Android Gradle Plugin + ASM 实现下面功能：

- 方法耗时统计
- 自动埋点

## Transform API 介绍

Starting with 1.5.0-beta1, the Gradle plugin includes a Transform API allowing 3rd party plugins to manipulate compiled class files before they are converted to dex files.

## 注意

如果想要直接以源码的方式引用插件，则插件项目名称必须为 `buildsrc`。

## 环境

- Android Gradle Plugin-7.0.4
- AndroidStudio-2020.3.1 patch4

## AGP 行为变更

参考 <https://developer.android.com/studio/releases/gradle-plugin>
