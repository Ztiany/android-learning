package com.susion.rabbit.gradle

import com.android.build.api.transform.QualifiedContent
import com.android.build.api.transform.Transform
import com.android.build.api.transform.TransformInvocation
import com.android.build.gradle.internal.pipeline.TransformManager
import com.android.builder.model.AndroidProject
import com.susion.rabbit.gradle.core.AsmByteArrayTransformer
import com.susion.rabbit.gradle.core.AsmClassVisitorTransformer
import com.susion.rabbit.gradle.core.context.RabbitTransformInvocation
import com.susion.rabbit.gradle.core.rxentension.bootClasspath
import com.susion.rabbit.gradle.core.rxentension.compileClasspath
import com.susion.rabbit.gradle.core.rxentension.file
import com.susion.rabbit.gradle.core.rxentension.runtimeClasspath
import com.susion.rabbit.gradle.transform.*
import com.susion.rabbit.gradle.utils.RabbitTransformUtils
import java.util.concurrent.TimeUnit

/**
 * susionwang at 2019-11-12
 */
class RabbitFirstTransform : Transform() {

    override fun getName() = "rabbit-transform"

    override fun getInputTypes(): MutableSet<QualifiedContent.ContentType> = TransformManager.CONTENT_CLASS

    override fun isIncremental() = true

    override fun getScopes(): MutableSet<QualifiedContent.ScopeType> = TransformManager.SCOPE_FULL_PROJECT

    override fun transform(transformInvocation: TransformInvocation?) {
        if (transformInvocation == null) return

        println("=====================================================")
        println("bootClasspath = ${transformInvocation.bootClasspath}")
        println("compileClasspath = ${transformInvocation.compileClasspath}")
        println("runtimeClasspath = ${transformInvocation.runtimeClasspath}")
        println("=====================================================")

        RabbitTransformUtils.print("🍊 rabbit RabbitFirstTransform run")

        val classVisitorTransforms = listOf(
            ActivitySpeedMonitorTransform(),
            AppStartSpeedMeasureTransform(),
            RabbitPluginConfigTransform(),
            BlockCodeScanTransform()
        )

        val byteArraysTransforms = listOf(MethodCostMonitorTransform())

        RabbitTransformInvocation(
            "FirstTransformName",
            transformInvocation,
            listOf(
                AsmClassVisitorTransformer(classVisitorTransforms),
                AsmByteArrayTransformer(byteArraysTransforms)
            )
        ).apply {
            if (isIncremental) {
                onPreTransform(this)
                doIncrementalTransform()
            } else {
                //删除老的构建内容
                buildDir.file(AndroidProject.FD_INTERMEDIATES, "transforms", "dexBuilder")
                    .let { dexBuilder ->
                        if (dexBuilder.exists()) {
                            dexBuilder.deleteRecursively()
                        }
                    }
                outputProvider.deleteAll()
                onPreTransform(this)
                doFullTransform()
            }

            onPostTransform(this)

        }.executor.apply {
            shutdown()
            awaitTermination(1, TimeUnit.MINUTES)
        }
    }

}