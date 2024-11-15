package com.app.byteweave.plugin.throttle

import com.android.build.api.instrumentation.AsmClassVisitorFactory
import com.android.build.api.instrumentation.ClassContext
import com.android.build.api.instrumentation.ClassData
import com.android.build.api.instrumentation.InstrumentationParameters
import com.app.byteweave.plugin.ViewThrottleConfiguration
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.objectweb.asm.ClassVisitor


internal interface ViewThrottleAspectConfigParameters : InstrumentationParameters {

    @get:Input
    val config: Property<ViewThrottleConfiguration>

}

internal abstract class ViewThrottleAspectClassVisitorFactory : AsmClassVisitorFactory<ViewThrottleAspectConfigParameters> {

    override fun createClassVisitor(classContext: ClassContext, nextClassVisitor: ClassVisitor): ClassVisitor {
        val config = parameters.get().config.get()
        return ViewThrottleClassVisitor(
            config = config,
            nextClassVisitor = nextClassVisitor
        )
    }

    override fun isInstrumentable(classData: ClassData): Boolean {
        val throttleConfiguration = parameters.get().config.get()
        return throttleConfiguration.isAvailable() && throttleConfiguration.isIncluded(classData.className)
    }

}