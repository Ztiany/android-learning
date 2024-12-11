package me.ztiany.apm.aspect.fluency

/**
 * App 的流畅度除了受绘制任务相关线程的执行时间影响，还受绘制任务执行时长的影响，
 * 当主线程有耗时长的任务执行时，也会导致 App 掉帧、卡顿。因此我们需要一种机制，
 * 能够监控到主线程是否卡顿，并且定位卡顿是哪里的代码导致的。
 *
 * 具体参考 Matrix 中的 `com/tencent/matrix/trace/core/LooperMonitor.java`。
 */
class LooperMonitor {

}