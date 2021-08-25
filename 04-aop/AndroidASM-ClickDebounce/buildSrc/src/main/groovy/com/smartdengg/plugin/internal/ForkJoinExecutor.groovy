package com.smartdengg.plugin.internal

import com.google.common.base.Preconditions

import java.util.concurrent.ForkJoinPool
import java.util.concurrent.ForkJoinTask

/**
 * 创建时间:  2019/06/04 18:43 <br>
 * 作者:  SmartDengg <br>
 * 描述: 并发编织执行器，使用 ForkJoinPool 分治算法。
 */
@Singleton
class ForkJoinExecutor {

    ForkJoinPool forkJoinPool = new ForkJoinPool()

    private final List<ForkJoinTask<?>> subTasks = []

    void execute(Closure task) {
        boolean added = subTasks.add(forkJoinPool.submit(new Runnable() {
            @Override
            void run() {
                task.call()
            }
        }))
        Preconditions.checkState(added, "Failed to add task")
    }

    /**等待所有任务执行完毕*/
    void waitingForAllTasks() {
        try {
            subTasks*.join()
        } finally {
        }
    }

}
