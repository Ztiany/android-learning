# [Android开发高手课](https://time.geekbang.org/column/intro/142) 实践 03

>所属章节——内存优化（上）：4GB内存时代，再谈内存优化

## 1 学习心得

阅读以下文章

- [Android 内存优化杂谈](https://mp.weixin.qq.com/s/Z7oMv0IgKWNkhLon_hFakg?)
- [Android 内存申请分析](https://mp.weixin.qq.com/s/b_lFfL1mDrNVKj_VAcA2ZA?)
- [《调查 RAM 使用情况》](https://developer.android.com/studio/profile/investigate-ram?hl=zh-cn)
- [《调试本地内存使用》](http://source.android.com/devices/tech/debug/native-memory)

了解 AddressSanitize：

- [AddressSanitize](http://source.android.com/devices/tech/debug/asan.html)
- [指南](http://github.com/google/sanitizers/wiki/AddressSanitizerOnAndroid)

概要总结：

- 系统一直更新，硬件也一直在更新，所以我们的优化方法也应该与之俱进
- Facebook 有一个叫 [device-year-class](https://github.com/facebook/device-year-class) 的开源库，它会用年份来区分设备的性能
- 手机内存技术参数，带宽的计算，内存的耗电量等。
- 内存并不是一个孤立的概念，手机内存不是越大越好，不仅考虑带宽，也要考虑性能，它跟操作系统、应用生态这些因素都有关
- 内存造成的问题
  - 内存问题造成**异常**以及具体原因
  - 内存问题造成**卡顿**以及具体原因
- 测试 GC 的性能
  - 发送SIGQUIT 信号获得 ANR 日志
  - 使用 systrace 来观察 GC 的性能耗时
- 内存优化的两个误区
  - 误区一：内存占用越少越好
  - 误区二：Native 内存不用管
- 考虑怎么提高内存的使用效率，而不是单纯的想着怎么减小内存使用的绝对值。
- Fresco 图片库将像素存于 native 中
- 跟踪 Java 内存分配
  - Allocation Tracker
  - MAT
  - 实现一个自定义的“Allocation Tracker”
- 跟踪 Native 内存分配
  - [AddressSanitize](http://source.android.com/devices/tech/debug/asan.html)
  - [《调试本地内存使用》](http://source.android.com/devices/tech/debug/native-memory)
- 内存优化：内存泄漏，减少内存的申请，及时回收，减少常驻内存，精简布局外，图片加载优化

## 2 实践：脱离 Android Studio 实现一个自定义的 Allocation Tracker

在今天文章里我提到，我们希望可以脱离 Android Studio 实现一个自定义的 Allocation Tracker，这样就可以将它用到自动化分析中。本期的 Sample 就提供了一个自定义的 Allocation Tracker 实现的示例，目前已经兼容到 Android 8.1。你可以用它练习实现自动化的内存分析，有哪些对象占用了大量内存，以及它们是如何导致 GC 等。

具体参考 [AndroidAdvanceWithGeektime/Chapter03](https://github.com/AndroidAdvanceWithGeektime/Chapter03)。
