package com.smartdengg.plugin

import com.android.annotations.NonNull
import com.android.build.api.transform.*
import com.android.build.gradle.AppPlugin
import com.android.build.gradle.internal.pipeline.TransformManager
import com.google.common.base.Joiner
import com.google.common.collect.ImmutableList
import com.smartdengg.compile.WovenClass
import com.smartdengg.plugin.api.DebounceExtension
import com.smartdengg.plugin.internal.ColoredLogger
import com.smartdengg.plugin.internal.ForkJoinExecutor
import com.smartdengg.plugin.internal.Processor
import com.smartdengg.plugin.internal.Utils
import groovy.util.logging.Slf4j
import org.apache.commons.io.FileUtils
import org.gradle.api.Project
import proguard.util.PrintWriterUtil

import java.nio.file.Files
import java.nio.file.Path

import static com.android.builder.model.AndroidProject.FD_OUTPUTS
import static com.google.common.base.Preconditions.checkNotNull

@Slf4j
class DebounceIncrementalTransform extends Transform {

    static final TASK_NAME = 'debounce'

    Project project

    /**编织配置*/
    DebounceExtension debounceExt

    /**用于记录被编织的类*/
    Map<String, List<WovenClass>> wovenVariantClassesMap

    /**日志路劲*/
    File debounceOutDir

    DebounceIncrementalTransform(Project project, Map<String, List<WovenClass>> wovenVariantClassesMap) {
        this.project = project
        this.debounceExt = project."${DebounceExtension.NAME}"
        this.wovenVariantClassesMap = wovenVariantClassesMap
        this.debounceOutDir = new File(Joiner.on(File.separatorChar).join(project.buildDir, FD_OUTPUTS, 'debounce', 'logs'))

        ColoredLogger.logRed("debounceExt = $debounceExt")
        ColoredLogger.logRed("debounceOutDir = $debounceOutDir")
    }

    @NonNull
    @Override
    String getName() {
        return TASK_NAME
    }

    @NonNull
    @Override
    Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS
    }

    @NonNull
    @Override
    Set<QualifiedContent.Scope> getScopes() {
        if (project.plugins.hasPlugin(AppPlugin)) {
            return TransformManager.SCOPE_FULL_PROJECT
        }
        return TransformManager.PROJECT_ONLY
    }

    @Override
    boolean isIncremental() {
        //支持增量编译
        return true
    }

    @Override
    Collection<File> getSecondaryDirectoryOutputs() {
        return ImmutableList.of(debounceOutDir)
    }

    @Override
    void transform(TransformInvocation invocation) throws TransformException, InterruptedException, IOException {

        ForkJoinExecutor executor = ForkJoinExecutor.instance

        //用于记录构建过程中那些类被编织过
        def wovenClassesContainer = []

        /*根据构建变种名称存储被编织类*/
        wovenVariantClassesMap[invocation.context.variantName] = wovenClassesContainer

        TransformOutputProvider outputProvider = checkNotNull(invocation.getOutputProvider(), "Missing output object for run " + getName())

        /*非增量编译要删除之前所有的输出*/
        if (!invocation.isIncremental()) {
            outputProvider.deleteAll()
        }

        File changedFiles = new File(debounceOutDir, Joiner.on(File.separatorChar).join(invocation.context.variantName, 'files.txt'))
        FileUtils.touch(changedFiles)
        PrintWriter writer = PrintWriterUtil.createPrintWriterOut(changedFiles)

        try {
            invocation.inputs.each { inputs ->

                //处理 jar
                inputs.jarInputs.each { jarInput ->

                    //当前 transform 流程，这个jar的输入路径
                    Path inputPath = jarInput.file.toPath()
                    //当前 transform 流程，这个jar的应该输出的路径
                    Path outputPtah = outputProvider.getContentLocation(
                            jarInput.name,
                            jarInput.contentTypes,
                            jarInput.scopes,
                            Format.JAR).toPath()

                    /************************** Write to file START *************************************/
                    Utils.writeStatusToFile(
                            writer,
                            inputPath.toString(),
                            jarInput.status,
                            outputPtah.toString(),
                            invocation.isIncremental())
                    /************************** Write to file END *************************************/

                    /*如果是增量编译，则根据输入的状态进行处理*/
                    if (invocation.isIncremental()) {

                        switch (jarInput.status) {
                        //没有变化的就不用管
                            case Status.NOTCHANGED:
                                break
                            case Status.ADDED:
                            case Status.CHANGED:
                                //新增的和已经变化的就删掉之前对应的输出，重新处理
                                Files.deleteIfExists(outputPtah)
                                Processor.run(inputPath, outputPtah, wovenClassesContainer, debounceExt.exclusion, Processor.Input.JAR)
                                break
                            case Status.REMOVED:
                                //被删除也需要删掉之前对应的输出
                                Files.deleteIfExists(outputPtah)
                                break
                        }
                    } else {
                        /*全量编译不需要判断，直接开整*/
                        executor.execute {
                            Processor.run(inputPath, outputPtah, wovenClassesContainer, debounceExt.exclusion, Processor.Input.JAR)
                        }
                    }
                }

                //处理目录
                inputs.directoryInputs.each { directoryInput ->

                    //当前 transform 流程，这个目录的输入路径
                    Path inputRoot = directoryInput.file.toPath()
                    //当前 transform 流程，这个目录的应该输出的路径
                    Path outputRoot = outputProvider.getContentLocation(//
                            directoryInput.name,
                            directoryInput.contentTypes,
                            directoryInput.scopes,
                            Format.DIRECTORY).toPath()

                    /************************** Write to file START *************************************/
                    Utils.writeStatusToFile(
                            writer,
                            inputRoot.toString(),
                            directoryInput.changedFiles.entrySet().toString(),
                            outputRoot.toString(),
                            invocation.isIncremental())
                    /************************** Write to file END *************************************/

                    /*增量编译则按状态进行处理*/
                    if (invocation.isIncremental()) {

                        directoryInput.changedFiles.each { File inputFile, Status status ->

                            //这里只是将路径转换成对应的 Path
                            Path inputPath = inputFile.toPath()
                            Path outputPath = Utils.toOutputPath(outputRoot, inputRoot, inputPath)

                            switch (status) {
                                case Status.NOTCHANGED:
                                    break
                                case Status.ADDED:
                                case Status.CHANGED:
                                    //direct run byte code
                                    Processor.directRun(inputPath, outputPath, wovenClassesContainer, debounceExt.exclusion)
                                    break
                                case Status.REMOVED:
                                    Files.deleteIfExists(outputPath)
                                    break
                            }
                        }
                    } else {
                        /*全量编译不需要判断，直接开整*/
                        executor.execute {
                            Processor.run(inputRoot, outputRoot, wovenClassesContainer, debounceExt.exclusion, Processor.Input.FILE)
                        }
                    }
                }
            }
        } finally {
            executor.waitingForAllTasks()
            PrintWriterUtil.closePrintWriter(changedFiles, writer)
            ColoredLogger.log("Wrote file status to file://${PrintWriterUtil.fileName(changedFiles)}")
        }
    }
}
