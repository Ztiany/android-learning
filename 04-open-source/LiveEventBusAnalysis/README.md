# [LiveEventBus](https://github.com/JeremyLiao/LiveEventBus) 源码分析

环境：

- 版本：88ec25bbc77afa62de24684abae66ad9de4d184b
- Gradle JDK：11

分析：

- **研究内容**：如果实现跨进程的事件总线？
- **研究结果**：LiveEventBus 内部通过 BroadcastReceiver 实现跨进程的事件总线。
