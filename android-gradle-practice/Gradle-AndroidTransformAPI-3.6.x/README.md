# Android Gradle Plugin3.6.x TransformAPI Sample

## API 变更

- 没有使用 TransformTask 来合成 Dex，而是使用 DexArchiveBuilderTask。

## Transform API

Starting with 1.5.0-beta1, the Gradle plugin includes a Transform API allowing 3rd party plugins to manipulate compiled class files before they are converted to dex files.

## 注意

如果直接引用代码中的插件，则项目名称必须为 *buildsrc*

## 环境

- Android Gradle Plugin3.0.x
- AndroidStudio3.x
