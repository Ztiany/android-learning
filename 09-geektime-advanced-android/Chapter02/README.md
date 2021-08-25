# [Android开发高手课](https://time.geekbang.org/column/intro/142) 实践 02

>所属章节——崩溃优化（下）：应用崩溃了，你应该如何去分析？

## 1 学习笔记

1. 在崩溃现场应该采集哪些信息？
   1. 崩溃信息，从崩溃的基本信息，我们可以对崩溃有初步的判断。
   2. 系统信息，系统的信息有时候会带有一些关键的线索，对我们解决问题有非常大的帮助。
   3. 内存信息，OOM、ANR、虚拟内存耗尽等，很多崩溃都跟内存有直接关系。
   4. 资源信息，有的时候我们会发现应用堆内存和设备内存都非常充足，还是会出现内存分配失败的情况，这跟**资源泄漏**可能有比较大的关系。
   5. 应用信息，除了系统，其实我们的应用更懂自己，可以留下很多相关的信息。比如崩溃页面，操作路径。
2. 如何采集信息？
   1. UncaughtExceptionHandler
   2. 读取系统中相关位置的文件，比如 event-log `/system/etc/event-log-tags`、系统内存状态 `/proc/meminfo`、PSS 和 RSS 通过 `/proc/self/smap` 计算、虚拟内存可以通过 `/proc/self/status` 得到，通过 `/proc/self/maps` 文件可以得到具体的分布情况、文件句柄的限制可以通过 `/proc/self/limits`。
   3. 分析 Android 系统 log，包括 `kernel、radio、event、main` 四种。
3. 如何分析崩溃：崩溃分析三部曲
   1. 第一步：确定重点
      1. 确认严重程度：解决崩溃也要看性价比，我们优先解决 Top 崩溃或者对业务有重大影响，例如启动、支付过程的崩溃。
      2. 崩溃基本信息：确定崩溃的类型以及异常描述，对崩溃有大致的判断。是 Java、Native 还是 ANR。
      3. Logcat：Logcat 一般会存在一些有价值的线索，日志级别是 Warning、Error 的需要特别注意。
      4. 各个资源情况：结合崩溃的基本信息，看是不是跟 “内存信息” 有关，是不是跟“资源信息”有关。比如是物理内存不足、虚拟内存不足，还是文件句柄 fd 泄漏了。
   2. 第二步：查找共性，如果第一步还是不能有效定位问题，我们可以尝试查找这类崩溃有没有什么共性。找到了共性，也就可以进一步找到差异，离解决问题也就更进一步。
   3. 第三步：尝试复现，如果我们已经大概知道了崩溃的原因，为了进一步确认更多信息，就需要尝试复现崩溃。
4. 系统崩溃如何解决？它可能是某个 Android 版本的 bug，也可能是某个厂商修改 ROM 导致。这种情况下的崩溃堆栈可能完全没有我们自己的代码，很难直接定位问题。
   1. 查找可能的原因。
   2. 尝试规避。
   3. Hook 解决。

相关链接：

- [Natvie 崩溃信号介绍](https://www.mkssoftware.com/docs/man5/siginfo_t.5.asp)

## 2 实践：TimeoutException 崩溃修复

如果想向崩溃发起挑战，那么 Top 20 崩溃就是我们无法避免的对手。在这里面会有不少疑难的系统崩溃问题，TimeoutException 就是其中比较经典的一个。

>关乎 TimeoutException，具体参考[how-to-handle-java-util-concurrent-timeoutexception-android-os-binderproxy-fin](https://stackoverflow.com/questions/24021609/how-to-handle-java-util-concurrent-timeoutexception-android-os-binderproxy-fin)

```log
java.util.concurrent.TimeoutException:
         android.os.BinderProxy.finalize() timed out after 10 seconds
at android.os.BinderProxy.destroy(Native Method)
at android.os.BinderProxy.finalize(Binder.java:459)
```

1. 通过源码分析。我们发现 TimeoutException 是由系统的 FinalizerWatchdogDaemon 抛出来的。
2. 寻找可以规避的方法。尝试调用了它的 Stop() 方法，但是线上发现在 Android 6.0 之前会有线程同步问题。
3. 寻找其他可以 Hook 的点。通过代码的依赖关系，发现一个取巧的 Hook 点。

具体参考：[AndroidAdvanceWithGeektime/Chapter02](https://github.com/AndroidAdvanceWithGeektime/Chapter02)
