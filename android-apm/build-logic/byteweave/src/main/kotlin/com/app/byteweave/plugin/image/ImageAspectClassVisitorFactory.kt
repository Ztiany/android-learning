package com.app.byteweave.plugin.image

import com.android.build.api.instrumentation.AsmClassVisitorFactory
import com.android.build.api.instrumentation.ClassContext
import com.android.build.api.instrumentation.ClassData
import com.android.build.api.instrumentation.InstrumentationParameters
import com.app.byteweave.plugin.LegalImageConfiguration
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.objectweb.asm.ClassVisitor

internal interface ImageAspectConfigParameters : InstrumentationParameters {

    @get:Input
    val config: Property<LegalImageConfiguration>

}

internal abstract class ImageAspectClassVisitorFactory : AsmClassVisitorFactory<ImageAspectConfigParameters> {

    override fun createClassVisitor(classContext: ClassContext, nextClassVisitor: ClassVisitor): ClassVisitor {
        val config = parameters.get().config.get()
        return LegalImageClassVisitor(
            config = config,
            nextClassVisitor = NewImageViewReplacementVisitor(
                config = config,
                nextClassVisitor = nextClassVisitor
            )
        )
    }

    override fun isInstrumentable(classData: ClassData): Boolean {
        return parameters.get().config.get().isAvailable()
    }

}