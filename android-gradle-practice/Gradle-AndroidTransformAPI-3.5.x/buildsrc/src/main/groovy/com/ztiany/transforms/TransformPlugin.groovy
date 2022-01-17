package com.ztiany.transforms


import com.android.build.gradle.AppExtension
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.execution.TaskExecutionGraph
import org.gradle.api.execution.TaskExecutionGraphListener

import java.util.function.Consumer

class TransformPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {

        if (!project.plugins.hasPlugin('com.android.application')) {
            throw new GradleException('Transform Plugin, Android Application plugin required')
        }
        /*
        find all transform task
                    transformClassesWithPrintTransformForDebug //自定义的
                    transformClassesWithTestTransformForDebug //自定义的
                    transformClassesWithDexBuilderForDebug //用于生成Dex
         */
        project.afterEvaluate {
            project.gradle.taskGraph.addTaskExecutionGraphListener(new TaskExecutionGraphListener() {
                @Override
                void graphPopulated(TaskExecutionGraph taskExecutionGraph) {
                    println("task: -------------------------------------------------------")
                    taskExecutionGraph.allTasks.forEach(new Consumer<Task>() {
                        @Override
                        void accept(Task task) {
                            println(task.name)
                            if (task.name.contains("transformClassesWithDex")) {
                                println(" transformClassesWithDex: "+task.class)
                            }
                        }
                    })
                    println("task: -------------------------------------------------------")
                }
            })
        }

        def android = project.extensions.findByType(AppExtension)
        android.registerTransform(new PrintConstructionTransform(project))
        android.registerTransform(new LogTransform())
    }

}