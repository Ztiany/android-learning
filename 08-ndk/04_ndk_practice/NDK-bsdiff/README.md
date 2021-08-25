# bsdiff/bspatch

bidiff/bspatch实现了不同版本文件的差量算法，可以从两个不同版本的文件生成补丁文件，然后旧版本的文件可以与补丁文件生成新版本的文件。
利用这一点，可以实现应用的增量更新。

不过补丁的大小依赖于两个不同文件之间的差异程度，如果新版本的文件只是基于旧版本的文件做了少许更新，则补丁文件相对较小，如果两个版本的文件差别非常之前，那么补丁
也会非常大的。


---
## 1 流程

1. apk中集成bspatch
1. 旧版本apk
1. 新版本apk
1. 服务器使用bsdiff生成差分包
1. 旧版本apk下载差分包，调用bspatch合成新版本，请求用户安装

---
## 2 windows平台编译(vs2013)
 
步骤：

1. 下载bsdiff windows版本源码
2. 创建Visual C++空项目
3. 导入源码和头文件
4. 生成dll动态库


生成dll动态库共Java调用：

1. 编写Java代码，使用javah生成头文件
2. vs中导入头文件，把mian方法修改为不同方法
3. 实现头文件中的方法，调用修改后的mian方法


编译过程遇到的问题：

- 用了不安全的函数：在项目属性->命令行中添加：`-D  _CRT_SECURE_NO_WARNINGS`
- 用了过时的函数：在项目属性->命令行中添加：`-D _CRT_NONSTDC_NO_DEPRECATE` 
- 提示某些变量未初始化，关闭VS的SDL检查
- JNI开发是如何找到java中的头文件，在项目属性->命令行中添加头文件包含目录，比如：`-I "E:/DevTools/Java/JDK1.8_151/include" -I "E:/DevTools/Java/JDK1.8_151/include/win32"`

---
## 3 Android集成bspatch

参考[AndroidSample](bspatch_android)


---
## 4 Linux编译bsdiff

注意使用`-I`引入java的include目录
```Shell
gcc -fPIC -shared bzip2.c bzlib.c randtable.c decompress.c compress.c crctable.c blocksort.c huffman.c bsdiff.c -o bsdiff.so #编译bsdiff库
```

---
## 引用

- [Binary diff/patch utility](http://www.daemonology.net/bsdiff/)
