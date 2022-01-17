package me.ztiany.buildsrc

import com.android.build.api.transform.*
import com.android.build.gradle.internal.pipeline.TransformManager
import com.google.common.collect.Sets
import org.apache.commons.codec.digest.DigestUtils
import org.apache.commons.io.FileUtils

/**
 *@author Ztiany
 *      Email: ztiany3@gmail.com
 *      Date : 2020-05-28 16:06
 */
class LogTransform : Transform() {

    override fun getName() = "LogTransform"

    override fun getInputTypes(): MutableSet<QualifiedContent.ContentType> {
        return mutableSetOf(QualifiedContent.DefaultContentType.CLASSES)
    }

    override fun isIncremental(): Boolean {
        return false
    }

    override fun getScopes(): MutableSet<in QualifiedContent.Scope> {
        return Sets.immutableEnumSet(QualifiedContent.Scope.PROJECT)
    }

    override fun transform(transformInvocation: TransformInvocation) {
        println("LogTransform: start transform, isIncremental = $isIncremental")
        if (!transformInvocation.isIncremental) {
            transformInvocation.outputProvider.deleteAll()
        }
        transformInvocation.inputs.forEach {
            it.directoryInputs.forEach {
                println("LogTransform: -------------------------------------------------------------------directoryInputs")
                processDirectory(it, transformInvocation.outputProvider)
            }
            it.jarInputs.forEach {
                println("LogTransform: -------------------------------------------------------------------jarInputs")
                processJar(it, transformInvocation.outputProvider)
            }
        }
    }

    private fun processJar(
        jarInput: JarInput,
        outputProvider: TransformOutputProvider
    ) {
        println("LogTransform: origin --> ${jarInput.file.absolutePath}, status = ${jarInput.status}")

        var jarName = jarInput.name
        val md5Name = DigestUtils.md5Hex(jarInput.file.absolutePath)
        if (jarName.endsWith(".jar")) {
            jarName = jarName.substring(0, jarName.length - 4)
        }
        //生成输出路径
        val dest = outputProvider.getContentLocation(
            jarName + md5Name,
            jarInput.contentTypes,
            jarInput.scopes,
            Format.JAR
        )
        println("LogTransform: dest --> $dest")
        //将输入内容复制到输出
        FileUtils.copyFile(jarInput.file, dest)
    }

    private fun processDirectory(
        directoryInput: DirectoryInput,
        outputProvider: TransformOutputProvider
    ) {
        println("LogTransform: origin --> ${directoryInput.file}")
        val dest = outputProvider.getContentLocation(
            directoryInput.name,
            directoryInput.contentTypes,
            directoryInput.scopes,
            Format.DIRECTORY
        )
        println("LogTransform: dest --> $dest")
        FileUtils.copyDirectory(directoryInput.file, dest)
    }

}