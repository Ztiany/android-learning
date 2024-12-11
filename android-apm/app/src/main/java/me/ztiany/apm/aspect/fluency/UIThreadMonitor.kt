package me.ztiany.apm.aspect.fluency

/**
 * 使用 Choreographer#FrameCallback 可以获取上一帧的耗时和 FPS，但这样做的缺点是只能获取指标，在得知卡顿时上一帧已经过去，
 * 无法定位到卡顿原因。要获取掉帧的原因，我们可以将 Choreographer 和 Looper 的卡顿监控机制结合起来。在收到 Choreographer
 * 的 doFrame 回调后，则认为当前正在执行绘制任务，如果此时 Looper 执行的消息耗时超过卡顿阈值，即认为出现了卡顿和掉帧。这时去
 * 抓栈的话就可以获取引起这一帧掉帧的原因。
 *
 * 要获取掉帧的原因，我们可以将 Choreographer 和 Looper 的卡顿监控机制结合起来。在收到 Choreographer 的 doFrame 回调后，
 * 则认为当前正在执行绘制任务，如果此时 Looper 执行的消息耗时超过卡顿阈值，即认为出现了卡顿和掉帧。这时去抓栈的话就可以获取引起
 * 这一帧掉帧的原因。
 *
 * 具体参考 Matrix 中的 `com/tencent/matrix/trace/core/UIThreadMonitor.java`。
 */
class UIThreadMonitor {

}