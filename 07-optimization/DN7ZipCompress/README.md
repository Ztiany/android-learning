# 动脑学院——在 Android 上集成 7zip 压缩

- 可执行文件方式，asset 中。
- 动态库方式，`.so`文件。

7z是一种文件压缩格式，具有高压缩比率，进行数据压缩有多种压缩算法可以选择。与其它压缩格式相比，得到的压缩文档较小，即压缩率最高，而 7zip 是完全免费而且开源的压缩软件，相比其他软件有更高的压缩比但同时耗费的资源也相对更多。支持压缩/解压缩：7z, XZ, BZIP2, GZIP, TAR, ZIP, WIM。

## 1 为什么需要压缩

1. 节省磁盘空间
2. 节省上传下载时间和流量

## 2 7zip 的使用

1. 压缩等级：`0：不压缩、1：快速压缩、5：正常压缩(默认值)、7：最大压缩、9：极限压缩`。
2. 压缩命令：`7z a  [输出文件] [待压缩文件/目录] -mx=9`
3. 解压命令：`7z x [压缩文件]  -o[输出目录]`

## 3 Android 下集成 7zip

### 源码位置

<https://sparanoid.com/lab/7z/download.htm>

### 集成方式1：直接使用可执行文件

7z 的使用不需要对执行过程进行干预，也就是不需要在执行过程中操作数据，只在乎最后得到一个 7z 文件或者解压出 7z 文件。因此可以使用命令行来使用 7zip 压缩与解压。(同理对于视频文件的压缩、转换也可以使用 ffmpeg 命令行，但是对于实时编码摄像头数据就必须编码完成)。

将可执行文件打包入 apk 中，安装后复制到指定目录，即可按下面方式使用：

```java
Runtime.getRuntime().exec(“xxx”)
```

编译 Android 可执行文件步骤：

1. 下载源码
2. 进入目录`/CPP/ANDROID/7zr`
3. 默认编译出 armeabi 架构，可以根据自己的需要在 `CPP/ANDROID/7zr/jni/Application.mk` 中增加/修改。
4. 执行编译 `ndk-build`。
5. 编译可执行文件输出在 `CPP/ANDROID/7zr/libs`。

>安装包包含了三种二进制， /usr/bin/7z，/usr/bin/7za，和 /usr/bin/7zr。 它们的菜单页解释它们的不同之处：
>
>- 7z 使用插件处理格式文件。
>- 7za 是独立可执行的。 7za 可以不需要其它任何插件的处理较少格式而不像 7z。
>- 7zr 是独立可执行的。 7zr 可以不需要其它任何插件的处理较少格式而不像 7z。 7zr是一个轻量级的 7za 只用来解压7z 格式的文件。

### 集成方式2：编译出动态库或静态库，通过 jni 调用

#### 编译动态库

修改 `CPP/ANDROID/7zr/jni/Android.mk` 如下：

```shell
#LOCAL_CFLAGS += -fPIE
#LOCAL_LDFLAGS += -fPIE -pie

#include $(BUILD_EXECUTABLE)
include $(BUILD_SHARED_LIBRARY)
```

>PIE是给可执行程序使用的flag（Position-Independent Executable位置无关可执行程序）ndk读取mk文件编译动态库不需要指定pic。

#### 编译静态库

修改 `CPP/ANDROID/7zr/jni/Android.mk` 如下：

```shell
#LOCAL_CFLAGS += -fPIE
#LOCAL_LDFLAGS += -fPIE -pie

#include $(BUILD_EXECUTABLE)
include $(BUILD_STATIC_LIBRARY)
```

## 4 示例代码

- [7ZipCompress](../../Code/DN7ZipCompress/README.md)
- 也可以参考 [Android使用7-zip库](https://www.jianshu.com/p/4d164d012336)
