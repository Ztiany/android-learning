package com.app.byteweave.plugin

import com.android.build.api.instrumentation.FramesComputationMode
import com.android.build.api.instrumentation.InstrumentationScope
import com.android.build.api.variant.AndroidComponentsExtension
import com.app.byteweave.plugin.image.ImageAspectClassVisitorFactory
import com.app.byteweave.plugin.throttle.ViewThrottleAspectClassVisitorFactory
import com.google.gson.Gson
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.getByType

class ByteweavePlugin : Plugin<Project> {

    override fun apply(project: Project) {
        logD("ByteweavePlugin applied to project ${project.name}")
        project.extensions.create<ByteweaveConfiguration>(name = ByteweaveConfiguration::class.java.simpleName)
        val androidComponents = project.extensions.getByType(AndroidComponentsExtension::class.java)

        androidComponents.onVariants { variant ->
            val configuration = project.extensions.getByType<ByteweaveConfiguration>()
            logE("ByteweavePlugin applied to variant ${variant.name}, with configuration: \n${Gson().toJson(configuration)}")

            // image aspect
            variant.instrumentation.transformClassesWith(
                ImageAspectClassVisitorFactory::class.java,
                InstrumentationScope.ALL
            ) { params ->
                params.config.set(configuration.legalImage)
            }

            // view throttle aspect
            variant.instrumentation.transformClassesWith(
                ViewThrottleAspectClassVisitorFactory::class.java,
                InstrumentationScope.ALL
            ) { params ->
                params.config.set(configuration.viewThrottle)
            }

            variant.instrumentation.setAsmFramesComputationMode(FramesComputationMode.COMPUTE_FRAMES_FOR_INSTRUMENTED_METHODS)
        }
    }

}