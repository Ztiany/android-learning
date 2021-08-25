# 1 Dagger2 + 组件化

1. Dagger2
1. 利用新的Dagger2 Android API实现更加解耦的注入方式
2. 组件化APP
3. 组件时如何组织各组件的Component
  - 作为Library的时候
  - 作为单独Application的时候
4. 各个Activity的Component作为AppComponent的Subcomponent，各个Fragment的Component作为对应ActivityComponent的Subcomponent的配置，或者某些Fragment的Component直接作为AppComponent的Subcomponent。


>在gradle.properties中可以切换组件化模式

# 2 工程结构

- BaseLibrary 所有组件都需要依赖的基础模块
- module_feature 一个单独的组件
- module_main 一个单独的组件
- Home 用于组合各个单独的功能组件

为了简洁，组建间跳转没有使用Router

# 3 关于Dagger2在Android上的扩展

```
    implementation "com.google.dagger:dagger:$daggerVersion"
    implementation "com.google.dagger:dagger-android:$daggerVersion"
    implementation "com.google.dagger:dagger-android-support:$daggerVersion"
    annotationProcessor "com.google.dagger:dagger-android-processor:$daggerVersion"
    annotationProcessor "com.google.dagger:dagger-compiler:$daggerVersion"
```

使用Dagger2在Android上的扩展，可以实现更加解耦的注入方式，具体参考文档：https://google.github.io/dagger/android。
在 Dagger2 2.16 之前，dagger.android 支持对其他注解的