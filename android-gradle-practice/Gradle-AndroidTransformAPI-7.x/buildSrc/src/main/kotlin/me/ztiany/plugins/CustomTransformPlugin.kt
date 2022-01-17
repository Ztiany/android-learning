package me.ztiany.plugins

import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import com.android.build.gradle.internal.pipeline.TransformTask
import me.ztiany.plugins.logprint.LogTransform
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.execution.TaskExecutionListener
import org.gradle.api.tasks.TaskState

/**
 *@author Ztiany
 *      Email: ztiany3@gmail.com
 *      Date : 2020-05-28 14:25
 */
internal class CustomTransformPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        if (!project.plugins.hasPlugin("com.android.application")) {
            throw GradleException("Systrace Plugin, Android Application plugin required")
        }

        val android = project.extensions.getByName("android") as BaseAppModuleExtension
        android.registerTransform(LogTransform())

        project.afterEvaluate {
            project.gradle.taskGraph.addTaskExecutionListener(object : TaskExecutionListener {

                override fun beforeExecute(p0: Task) {

                }

                //give attention to dexBuilderDebug/dexBuilderRelease.
                override fun afterExecute(task: Task, taskState: TaskState) {
                    try {
                        project.logger.error("afterExecute-->$task isTransformTask = ${task is TransformTask}")
                        project.logger.error("        input: ")
                        for (file in task.inputs.files.files) {
                            project.logger.error("                $file")
                        }
                        project.logger.error("        output: ")
                        for (file in task.outputs.files.files) {
                            project.logger.error("                $file")
                        }
                    } catch (e: NullPointerException) {
                        e.printStackTrace()
                    }
                }
            })
        }

    }

}