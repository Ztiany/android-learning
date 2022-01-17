# Android Gradle Plugin3.6.x TransformAPI Sample

## Transform API 介绍

Starting with 1.5.0-beta1, the Gradle plugin includes a Transform API allowing 3rd party plugins to manipulate compiled class files before they are converted to dex files.

## 注意

如果想要直接以源码的方式引用插件，则插件项目名称必须为 `buildsrc`。

## 环境

- Android Gradle Plugin3.6.3
- AndroidStudio4.x

## AGP 行为变更

3.6.x 之前采用 TransformTask 来合成 Dex，3.6.x 不再使用 TransformTask，而是使用 DexArchiveBuilderTask 类，其任务名为 dexBuilderDebug。

更多可以参考 <https://developer.android.com/studio/releases/gradle-plugin>
