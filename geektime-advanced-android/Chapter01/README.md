# [Android开发高手课](https://time.geekbang.org/column/intro/142) 实践 01

>所属章节——崩溃优化（上）：关于“崩溃”那些事儿练习

## 1 学习笔记

**Android 应用异常分类：Java 异常和 Native 异常**：

1. Java 异常崩溃我们非常熟悉，可以使用 UncaughtExceptionHandler 监控
2. Native 崩溃：一般都是因为在 Native 代码中访问非法地址，也可能是地址对齐出现了问题，或者发生了程序主动 abort，这些都会产生相应的 signal 信号，导致程序异常退出。

**关于 Native 异常的处置**：

1. 一个完整的 Native 崩溃从捕获到解析要经历哪些流程？
   1. 编译端。编译 `C/C++` 代码时，需要将带符号信息的文件保留下来。
   2. 客户端。捕获到崩溃时候，将收集到尽可能多的有用信息写入日志文件，然后选择合适的时机上传到服务器。
   3. 服务端。读取客户端上报的日志文件，寻找适合的符号文件，生成可读的 C/C++ 调用栈。
2. Native 崩溃监控的有哪些难点？（最核心的是怎么样保证客户端在各种极端情况下依然可以生成崩溃日志。）
   1. 情况一：文件句柄泄漏，导致创建日志文件失败，怎么办？
   2. 情况二：因为栈溢出了，导致日志生成失败，怎么办？
   3. 情况三：整个堆的内存都耗尽了，导致日志生成失败，怎么办？
   4. 情况四：堆破坏或二次崩溃导致日志生成失败，怎么办？
3. 选择第三方异常上报服务：包括腾讯的Bugly、阿里的啄木鸟平台、网易云捕、Google 的 Firebase 等等。

扩展：通过学习 [Android 平台 Native 代码的崩溃捕获机制及实现](https://mp.weixin.qq.com/s/g-WzYF3wWAljok1XjPoo7w?) 了解：

1. Native 异常捕获框架
2. 类 Unix 系统异常处理机制、如何捕获异常

**Breakpad**：

1. Native 异常可以使用 [Breakpad](https://chromium.googlesource.com/breakpad/breakpad/+/master) 进行监控
2. 关于 Breakpad：
   1. 了解 Breakpad 是什么、Breakpad 能做什么
   2. 动手实践，编译 Breakpad，使用 Breakpad 监控 Native 异常
   3. 阅读 Breakpad 相关代码，并掌握其实现原理

Breakpad 底层使用了 [linux-syscall-support](https://chromium.googlesource.com/linux-syscall-support/)。

**如何客观地衡量崩溃**：

- UV 崩溃率，公式：`UV 崩溃率 = 发生崩溃的 UV / 登录 UV`
- PV 崩溃率
- 启动崩溃率
- 重复崩溃率

安全模式概念：保障客户端的启动流程，在监控到客户端启动失败后，给用户自救的机会。具体参考 [安全模式：天猫App启动保护实践](https://mp.weixin.qq.com/s?__biz=MzUxMzcxMzE5Ng==&mid=2247488429&idx=1&sn=448b414a0424d06855359b3eb2ba8569&source=41#wechat_redirect)

**如何客观地衡量稳定性**：

崩溃率低不能完全等价于高稳定性，因为还有 ANR（Application Not Responding，程序没有响应）这个问题。

1. 线上 ANR 如何的监控
   1. 使用 FileObserver 监听 /data/anr/traces.txt 的变化。（高版本系统上，应用没有了读取该文件的权限，对此在海外可以使用 Google Play 服务，而国内，微信使用的是[Hardcoder](https://mp.weixin.qq.com/s/9Z8j3Dv_5jgf7LDQHKA0NQ?)）
   2. WatchDog 监控消息队列的运行时间。这个方案无法准确地判断是否真正出现了 ANR 异常，也无法得到完整的 ANR 日志。
2. 异常退出：除了常见的崩溃，还存在异常退出的情况。
   1. 主动自杀。`Process.killProcess()、exit()` 等。（不统计）
   2. 崩溃。出现了 Java 或 Native 崩溃。（单独统计）
   3. 系统重启；系统出现异常、断电、用户主动重启等，我们可以通过比较应用开机运行时间是否比之前记录的值更小。
   4. 被系统杀死。被 low memory killer 杀掉、从系统的任务管理器中划掉等。
   5. ANR。
3. 统计方法：在应用启动的时候设定一个标志，在主动自杀或崩溃后更新标志。
4. 异常率，除了常规崩溃和主动自杀外，实现对 `系统出现异常、断电、用户主动重启、被系统杀死、ANR` 等情况的统计。公式：`UV 异常率 = 发生异常退出或崩溃的 UV / 登录 UV`。
5. 异常率的意义：通过异常率我们可以比较全面的评估应用的稳定性，对于线上监控还需要完善崩溃的报警机制。

**总结**：

做一个高可用的崩溃收集 SDK 并不容易，它背后涉及 Linux 信号处理以及内存分配、汇编等知识，当你内功修炼得越深厚，学习这些底层知识就越得心应手。

## 2 实践：使用 Breakpad 来捕获一个 Native 崩溃实践

### 下载和编译 Breakpad

按照 [Breakpad](https://github.com/google/breakpad) 说明下载 Breakpad，如果 git 无法访问 `chromium.googlesource.com`（比如 clone 时出现 443 错误），可能是因为设置了代理，先配置一下 git 即可:

```bash
# 下面 `192.168.4.12:8080` 不同的 VPN 可能不一致。
git config --local http.proxy 192.168.4.12:8080
```

整体步骤：

```bash
# 设置代理
git config --local http.proxy 192.168.4.12:8080

# 下载 depot_tools 工具
git clone https://chromium.googlesource.com/chromium/tools/depot_tools
# 解压后配置路径
export PATH=$PATH:/mnt/d/linux/depot_tools

# 如果没有安装 python 2.7.x
apt install python

# 下载 breakpad
fetch breakpad
# 配置和编译
./configure && make
```

### 分析崩溃

触发 native 崩溃后，breakpad 会生成 *.dmp 文件。

```bash
# 根据 minidump 文件生成堆栈跟踪log
./minidump_stackwalk a9663bf9-2eff-40ca-2346cf9d-f30b238d.dmp > crashLog.txt
```

得到信息如下：

```shell
Operating system: Android
                  0.0.0 Linux 4.9.111 #1 SMP PREEMPT Wed Apr 10 02:52:24 CST 2019 aarch64
CPU: arm64
     8 CPUs

GPU: UNKNOWN

Crash reason:  SIGSEGV /SEGV_MAPERR
Crash address: 0x1000300020005
Process uptime: not available

Thread 0 (crashed)
 0  libnative-lib.so + 0x550 #注意这里的 0x550，下面需要用到
     x0 = 0x0001000300020005    x1 = 0x0000007fd55ce864
     x2 = 0x0000000012d995b0    x3 = 0x00000078b2a98e14
     x4 = 0x0000007fd55ce844    x5 = 0x0000000000000000
     x6 = 0x0000007fd55ce7e0    x7 = 0x00000078b5415c00
     x8 = 0x0000000000000003    x9 = 0x25fa764e80501f98
    x10 = 0x0000000000430000   x11 = 0x00000078b2e6d6d8
    x12 = 0x000000793b910510   x13 = 0x2d00dd85dff99863
    x14 = 0x000000793b511000   x15 = 0xffffffffffffffff
    x16 = 0x00000078967a253c   x17 = 0x0000007935c43cb4
    x18 = 0x0000000000000000   x19 = 0x00000078b5415c00
    x20 = 0x0000000000000000   x21 = 0x00000078b5415c00
    x22 = 0x0000007fd55ceb30   x23 = 0x00000078974e5d24
    x24 = 0x0000000000000004   x25 = 0x000000793bc805e0
    x26 = 0x00000078b5415ca0   x27 = 0x0000000000000001
    x28 = 0x0000007fd55ce860    fp = 0x0000007fd55ce860
     lr = 0x00000078b2d92fe4    sp = 0x0000007fd55ce820
     pc = 0x00000078967a2550
    Found by: given as instruction pointer in context

... 省略...
```

符号解析：用 ndk 中提供的 addr2line 来根据地址进行一个符号反解的过程：

```bash
# 输入命令，进入交互模式
D:\dev_tools\android-ndk-r13b-windows-x86_64\android-ndk-r13b\toolchains\aarch64-linux-android-4.9\prebuilt\windows-x 86_64\bin\aarch64-linux-android-addr2line.exe -f -C -e .\Geektime-AdvanceAndroid\Chapter01\app\build\intermediates\transforms\mergeJniLibs\debug\0\lib\arm64-v8a\libnative-lib.so

# 输入地址，addr2line 会输出代码行号
0x550
Java_com_dodola_breakpad_MainActivity_makeCrash
.\Geektime-AdvanceAndroid\Chapter01\app\src\main\cpp/native-lib.cpp:8
```

至此可以定位到，我们的 bug 出现在 `native-lib.cpp` 第 8 行。

具体参考[AndroidAdvanceWithGeektime/Chapter01](https://github.com/AndroidAdvanceWithGeektime/Chapter01)

## 3 todo

- [Google Breakpad：脱离符号的调试工具](https://jackwish.net/2015/introduction-of-google-breakpad.html)