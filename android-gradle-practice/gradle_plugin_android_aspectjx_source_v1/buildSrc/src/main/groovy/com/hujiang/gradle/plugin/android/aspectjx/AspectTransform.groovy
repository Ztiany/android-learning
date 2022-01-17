/*
 * Copyright 2016 firefly1126, Inc.

 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.gradle_plugin_android_aspectjx
 */
package com.hujiang.gradle.plugin.android.aspectjx

import com.android.SdkConstants
import com.android.build.api.transform.*
import com.android.build.gradle.internal.pipeline.TransformTask
import com.google.common.collect.ImmutableSet
import org.aspectj.util.FileUtil
import org.gradle.api.Project
import org.gradle.api.tasks.compile.JavaCompile

/**
 * aspectj transform
 * @author simon
 * @version 1.0.0
 * @since 2016-03-29
 */
class AspectTransform extends Transform {

    Project project//项目对象
    String encoding//编码
    String bootClassPath//引导类路径
    String sourceCompatibility//兼容版本
    String targetCompatibility//兼容版本

    AspectTransform(Project proj) {
        project = proj

        //获取配置
        def configuration = new AndroidConfiguration(project)

        // all the definitions in a build script have been applied，当构建脚本应用完毕后执行
        project.afterEvaluate {

            configuration.variants.all { variant ->
                //获取java编译任务，通过编译Task获取ajc需要的信息
                JavaCompile javaCompile = variant.hasProperty('javaCompiler') ? variant.javaCompiler : variant.javaCompile

                encoding = javaCompile.options.encoding
                bootClassPath = configuration.bootClasspath.join(File.pathSeparator)
                sourceCompatibility = javaCompile.sourceCompatibility
                targetCompatibility = javaCompile.targetCompatibility
            }
        }
    }

    @Override
    String getName() {
        return "AspectTransform"
    }

    //返回要处理的输入类型，这里只处理Resource
    @Override
    Set<QualifiedContent.ContentType> getInputTypes() {
        //即TransformManager.CONTENT_CLASS，包含了jar和文件夹中的class
        return ImmutableSet.<QualifiedContent.ContentType> of(QualifiedContent.DefaultContentType.CLASSES)
    }

    //指定Transform的作用范围
    @Override
    Set<QualifiedContent.Scope> getScopes() {

        def name = QualifiedContent.Scope.PROJECT_LOCAL_DEPS.name()

        //3.0 PROJECT_LOCAL_DEPS 已标记为废弃
        def deprecated = QualifiedContent.Scope.PROJECT_LOCAL_DEPS.getClass().getField(name).getAnnotation(Deprecated.class)

        if (deprecated == null) {
            println "cannot find QualifiedContent.Scope.PROJECT_LOCAL_DEPS Deprecated.class "
            return ImmutableSet.<QualifiedContent.Scope> of(
                    QualifiedContent.Scope.PROJECT,
                    QualifiedContent.Scope.PROJECT_LOCAL_DEPS,
                    QualifiedContent.Scope.EXTERNAL_LIBRARIES,
                    QualifiedContent.Scope.SUB_PROJECTS,
                    QualifiedContent.Scope.SUB_PROJECTS_LOCAL_DEPS)
        } else {
            println "find QualifiedContent.Scope.PROJECT_LOCAL_DEPS Deprecated.class "
            return ImmutableSet.<QualifiedContent.Scope> of(
                    QualifiedContent.Scope.PROJECT,
                    QualifiedContent.Scope.EXTERNAL_LIBRARIES,
                    QualifiedContent.Scope.SUB_PROJECTS)
        }
    }

    @Override
    boolean isIncremental() {
        //不是增量编译
        return false
    }

    @Override
    void transform(Context context
                   , Collection<TransformInput> inputs
                   , Collection<TransformInput> referencedInputs
                   , TransformOutputProvider outputProvider
                   , boolean isIncremental) throws IOException, TransformException, InterruptedException {

        TransformTask transformTask = (TransformTask) context

        println "task name:" + transformTask.variantName
        println("isIncremental:" + isIncremental)

        for (TransformInput input : inputs) {
            println "inputs-jar>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"
            for (JarInput jInput : input.jarInputs) {
                println(jInput.file.absolutePath)
            }

            println "inputs-dir>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"
            for (DirectoryInput dInput : input.directoryInputs) {
                println(dInput.file.absolutePath)
            }
        }

        for (TransformInput input : referencedInputs) {
            println "referenced Input jar>>>>>>>>>>>>>>>>>>>>>>>>>>>"
            for (JarInput jInput : input.jarInputs) {
                println(jInput.file.absolutePath)
            }

            println "referenced Input dir>>>>>>>>>>>>>>>>>>>>>>>>>>>"
            for (DirectoryInput dInput : input.directoryInputs) {
                println(dInput.file.absolutePath)
            }
        }

        println "inputs end<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<"

        //clean 不是增量编译就删掉之前的所有输出
        if (!isIncremental) {
            outputProvider.deleteAll()
        }

        println "transformTask.variantName = $transformTask.variantName <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<"

        if (transformTask.variantName.contains("AndroidTest")) {
            //因为是测试变种，所以下面代码只是把输入复制到目标输出目录
            println "there is no aspectjrt dependencies in classpath, do nothing "

            inputs.each { TransformInput input ->
                input.directoryInputs.each { DirectoryInput directoryInput ->
                    def dest = outputProvider.getContentLocation(directoryInput.name,
                            directoryInput.contentTypes, directoryInput.scopes,
                            Format.DIRECTORY)
                    FileUtil.copyDir(directoryInput.file, dest)
                    println "directoryInput = ${directoryInput.name}"
                }

                input.jarInputs.each { JarInput jarInput ->
                    def jarName = jarInput.name
                    def dest = outputProvider.getContentLocation(jarName,
                            jarInput.contentTypes, jarInput.scopes, Format.JAR)

                    FileUtil.copyFile(jarInput.file, dest)
                    println "jarInput = ${jarInput.name}"
                }
            }
        } else {
            //执行aspectj编织
            doAspectTransform(outputProvider, inputs)
        }
    }

    private void doAspectTransform(TransformOutputProvider outputProvider, Collection<TransformInput> inputs) {
        //step 1 创建编织工作对象，准备参数
        println "aspect start.........."
        AspectWork aspectWork = new AspectWork(project)//编织工作对象
        aspectWork.encoding = encoding//编码
        aspectWork.bootClassPath = bootClassPath//设置引导类路径
        aspectWork.sourceCompatibility = sourceCompatibility//兼容版本
        aspectWork.targetCompatibility = targetCompatibility//兼容版本

        //create aspect destination dir，处理此时aspect transform 目标目录
        //xxx\gradle_plugin_android_aspectjx_source\app\build\intermediates\transforms\AspectTransform\release\folders\1\1f\aspect
        //xxx\gradle_plugin_android_aspectjx_source\app\build\intermediates\transforms\AspectTransform\debug\folders\1\1f\aspect
        File resultDir = outputProvider.getContentLocation("aspect", getOutputTypes(), getScopes(), Format.DIRECTORY)
        println("resultDir--------------------------------------->${resultDir.absolutePath}")
        if (resultDir.exists()) {
            println "delete resultDir ${resultDir.absolutePath}"
            FileUtils.deleteFolder(resultDir)
        }
        FileUtils.mkdirs(resultDir)
        //指定输入目录
        aspectWork.destinationDir = resultDir.absolutePath

        //在Extension中定义的包含、排除、ajc参数
        List<String> includeJarFilter = project.aspectjx.includeJarFilter
        List<String> excludeJarFilter = project.aspectjx.excludeJarFilter
        aspectWork.setAjcArgs(project.aspectjx.ajcArgs)

        for (TransformInput transformInput : inputs) {

            //把输入的目录添加到要编织的目录中
            for (DirectoryInput directoryInput : transformInput.directoryInputs) {

                println "directoryInput:::${directoryInput.file.absolutePath}"
                aspectWork.aspectPath << directoryInput.file
                aspectWork.inPath << directoryInput.file
                aspectWork.classPath << directoryInput.file
            }

            //处理jar，去掉ExcludeFilter的jar
            for (JarInput jarInput : transformInput.jarInputs) {

                aspectWork.aspectPath << jarInput.file
                aspectWork.classPath << jarInput.file

                String jarPath = jarInput.file.absolutePath

                if (isIncludeFilterMatched(jarPath, includeJarFilter) && !isExcludeFilterMatched(jarPath, excludeJarFilter)) {
                    println "includeJar:::${jarPath}"
                    aspectWork.inPath << jarInput.file
                } else {
                    println "excludeJar:::${jarPath}"
                    //直接复制不应该包含编织的jar
                    copyJar(outputProvider, jarInput)
                }
            }
        }

        //step 2 执行编织工作
        aspectWork.doWork()

        //step 3 合并jar，提高构建速度
        //add class file to aspect result jar
        println "aspect jar merging.........."

        if (resultDir.listFiles().length > 0) {

            //获取一个新的目录，
            File jarFile = outputProvider.getContentLocation("aspected", getOutputTypes(), getScopes(), Format.JAR)
            println("jarFile--------------------------------------->${jarFile.absolutePath}")

            FileUtils.mkdirs(jarFile.getParentFile())
            FileUtils.deleteIfExists(jarFile)

            JarMerger jarMerger = new JarMerger(jarFile)

            try {
                jarMerger.setFilter(new JarMerger.IZipEntryFilter() {
                    @Override
                    boolean checkEntry(String archivePath) throws JarMerger.IZipEntryFilter.ZipAbortException {
                        println("archivePath------------$archivePath")
                        return archivePath.endsWith(SdkConstants.DOT_CLASS)
                    }
                })
                jarMerger.addFolder(resultDir)

            } catch (Exception e) {
                throw new TransformException(e)
            } finally {
                jarMerger.close()
            }
        }
        FileUtils.deleteFolder(resultDir)
        println "aspect done..................."
    }

    boolean isExcludeFilterMatched(String str, List<String> filters) {
        return isFilterMatched(str, filters, FilterPolicy.EXCLUDE)
    }

    boolean isIncludeFilterMatched(String str, List<String> filters) {
        return isFilterMatched(str, filters, FilterPolicy.INCLUDE)
    }

    boolean isFilterMatched(String str, List<String> filters, FilterPolicy filterPolicy) {
        if (str == null) {
            return false
        }

        if (filters == null || filters.isEmpty()) {
            return filterPolicy == FilterPolicy.INCLUDE
        }

        for (String s : filters) {
            if (isContained(str, s)) {
                return true
            }
        }

        return false
    }

    static boolean copyJar(TransformOutputProvider outputProvider, JarInput jarInput) {
        if (outputProvider == null || jarInput == null) {
            return false
        }

        String jarName = jarInput.name
        if (jarName.endsWith(".jar")) {
            jarName = jarName.substring(0, jarName.length() - 4)
        }

        File dest = outputProvider.getContentLocation(jarName, jarInput.contentTypes, jarInput.scopes, Format.JAR)

        FileUtil.copyFile(jarInput.file, dest)

        return true
    }

    static boolean isContained(String str, String filter) {
        if (str == null) {
            return false
        }

        String filterTmp = filter
        if (str.contains(filterTmp)) {
            return true
        } else {
            if (filterTmp.contains("/")) {
                return str.contains(filterTmp.replace("/", File.separator))
            } else if (filterTmp.contains("\\")) {
                return str.contains(filterTmp.replace("\\", File.separator))
            }
        }
        return false
    }

    enum FilterPolicy {
        INCLUDE, EXCLUDE
    }
}
