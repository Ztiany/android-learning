# NdkBuild使用第三方库

JNI代码在被运行在android上时不能引用非/system/lib下的动态库。安卓操作系统的系统库文件都是放到/system/lib下的，
如果你的JNI代码想引用一些第三方库的功能，就得考虑将第三方库做成静态库，继而打入你生成的jni库中。

## project-tow-libs

演示：需要编写一个第三方Native库，需要将你写的代码提供给你的客户，而且还想在做完库文件后还有一个测试程序，
那么你可以在Android.mk中先编译出来一个静态库，然后再编译一个测试用的JNI动态库。

>代码来自GoogleSample/ndk/two-libs

## project-import-libs

>代码来自GoogleSample/ndk/hello-libs

演示：其他开发者提供了静态库+头文件或者动态库加头文件，自己在开发JNI时如何导入这些库


## 其他

- [android-extend](https://github.com/Ztiany/android-extend)
- [认识Android.mk和Application.mk](http://www.jianshu.com/p/f23df3aa342c)
- [android.mk语法详解](http://blog.csdn.net/huangxiaominglipeng/article/details/17839239)
- [android.mk语法详解](http://www.jianshu.com/p/703ef39dff3f)

## 引用

- [googlesamples/android-ndk](https://github.com/googlesamples/android-ndk)