package me.ztiany.apm.aspect.fluency

/**
 * 日常开发中，我们可能会被 IdleHandler 的名称所误导，觉得它是主线程空闲的时候调用的方法，
 * 耗时久一点也没关系，这其实是不正确的。IdleHandler 也会导致卡顿。
 *
 * 当 Looper 执行时，会不断地从消息队列里取消息，如果当前消息队列里没有消息，或者消息队列里
 * 的第一条消息还没有到执行时间，就会执行之前添加的所有 IdleHandler。也就是说，IdleHandler
 * 的 queueIdle 方法是在主线程执行的，如果在其中有耗时任务，同样会导致卡顿。
 *
 * Matrix 提供了另外一种思路：通过代理每个 IdleHandler 对象，在 queueIdle执 行前后增加卡
 * 顿监控逻辑。 Matrix 中的 `com/tencent/matrix/trace/tracer/IdleHandlerLagTracer.java`。
 */
class IdleHandlerMonitor {

}