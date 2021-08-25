package com.app.plugins.aspectj

import com.android.build.gradle.AppPlugin
import com.android.build.gradle.LibraryPlugin
import org.aspectj.bridge.IMessage
import org.aspectj.bridge.MessageHandler
import org.aspectj.tools.ajc.Main
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * @使用ajc编译java代码 ， 同时织入切片代码
 * 使用 AspectJ 的编译器（ajc，一个java编译器的扩展）
 * 对所有受 aspect 影响的类进行织入。
 * 在 gradle 的编译 task 中增加额外配置，使之能正确编译运行。
 */
class AspectjPlugin implements Plugin<Project> {

    void apply(Project project) {

        //依赖aspectjrt
        project.dependencies {
            implementation 'org.aspectj:aspectjrt:1.8.9'
        }

        final def log = project.logger
        //开始
        def variants = getVariants(project)
        project.android[variants].all {
            variant ->
                println "========================================================================Aspectj start work"
                def javaCompile = variant.javaCompile
                javaCompile.doLast {
                    //java编译后加入action
                    def encoding = javaCompile.options.encoding
                    def sourceCompatibility = javaCompile.sourceCompatibility
                    def targetCompatibility = javaCompile.targetCompatibility

                    String[] args = [
                            "-showWeaveInfo",
                            "-encoding", encoding,
                            "-source", sourceCompatibility,
                            "-target", targetCompatibility,
                            "-inpath", javaCompile.destinationDir.toString(),
                            "-aspectpath", javaCompile.classpath.asPath,
                            "-d", javaCompile.destinationDir.toString(),
                            "-classpath", javaCompile.classpath.asPath,
                            "-bootclasspath", project.android.bootClasspath.join(File.pathSeparator)]



                    println "ajs args-------------------------------------------------------start"
                    for (String arg : args) {
                        println(arg)
                    }
                    println "ajs args-------------------------------------------------------end"

                    /*

                    -inpath 只有这个路径的类文件才会被aspject编织
                    E:\code\studio\my_github\Repository\Android\AndroidAspectj\AndroidAspectj02\app\build\intermediates\classes\debug

                    -aspectpath
                    E:\code\studio\my_github\Repository\Android\AndroidAspectj\AndroidAspectj02\library\build\intermediates\intermediate-jars\debug\classes.jar;
                    D:\dev_files\.gradle\caches\modules-2\files-2.1\org.aspectj\aspectjrt\1.8.10\1a14fe9e912f6e8bdbb5429b78b4090d8b47bc1\aspectjrt-1.8.10.jar;
                    D:\dev_files\.gradle\caches\transforms-1\files-1.1\appcompat-v7-26.1.0.aar\7d5cac7661154bb65e970afa7fa4f7a5\jars\classes.jar;
                    D:\dev_files\.gradle\caches\transforms-1\files-1.1\constraint-layout-1.0.2.aar\f55639ac1284ced83249aa6d3949cb14\jars\classes.jar;
                    D:\dev_files\.gradle\caches\transforms-1\files-1.1\animated-vector-drawable-26.1.0.aar\2c37f8f46ac5608161bbe547832e69e8\jars\classes.jar;
                    D:\dev_files\.gradle\caches\transforms-1\files-1.1\support-vector-drawable-26.1.0.aar\3a2a166df0070c80147d9c2bb0faf8a9\jars\classes.jar;
                    D:\dev_files\.gradle\caches\transforms-1\files-1.1\support-v4-26.1.0.aar\f105c47a5c87854bf795f2002fbb2702\jars\classes.jar;
                    D:\dev_files\.gradle\caches\transforms-1\files-1.1\support-media-compat-26.1.0.aar\38861d59b8fe5216d202c817e3e6371c\jars\classes.jar;
                    D:\dev_files\.gradle\caches\transforms-1\files-1.1\support-fragment-26.1.0.aar\7ee5b868d380278261c2e772a1efc99a\jars\classes.jar;
                    D:\dev_files\.gradle\caches\transforms-1\files-1.1\support-core-utils-26.1.0.aar\21927a30251694705503bea6cc0222ec\jars\classes.jar;
                    D:\dev_files\.gradle\caches\transforms-1\files-1.1\support-core-ui-26.1.0.aar\14f78a524b914168183e04f0566797f7\jars\classes.jar;
                    D:\dev_files\.gradle\caches\transforms-1\files-1.1\support-compat-26.1.0.aar\212425ba4cf954df4db0d9aafd334775\jars\classes.jar;
                    D:\dev_files\.gradle\caches\modules-2\files-2.1\com.android.support\support-annotations\26.1.0\814258103cf26a15fcc26ecce35f5b7d24b73f8\support-annotations-26.1.0.jar;
                    E:\DevTools\SDK\extras\m2repository\com\android\support\constraint\constraint-layout-solver\1.0.2\constraint-layout-solver-1.0.2.jar;
                    D:\dev_files\.gradle\caches\transforms-1\files-1.1\runtime-1.0.0.aar\a21318db1da292eb4695fabb45c2e9f8\jars\classes.jar;
                    D:\dev_files\.gradle\caches\modules-2\files-2.1\android.arch.lifecycle\common\1.0.0\e414a4cb28434e25c4f6aa71426eb20cf4874ae9\common-1.0.0.jar;
                    D:\dev_files\.gradle\caches\modules-2\files-2.1\android.arch.core\common\1.0.0\a2d487452376193fc8c103dd2b9bd5f2b1b44563\common-1.0.0.jar

                    -d 输出目录
                    E:\code\studio\my_github\Repository\Android\AndroidAspectj\AndroidAspectj02\app\build\intermediates\classes\debug

                    -classpath 与 aspectpath相同
                   
                    -bootclasspath
                    E:\DevTools\SDK\platforms\android-26\android.jar

                    采用这种方式aspect只会作用于项目自己代码，如果项目存在多个module，那么每个module都需要应用这个插件，而使用TransformAPI进行编织的话可以对项目以及项目依赖的
                    所有第三方库进行编织(项目很大时会比较慢)。但是一般情况下只需要对项目本身代码进行编织，
                    */

                    MessageHandler handler = new MessageHandler(true)

                    new Main().run(args, handler)//执行代码织入

                    for (IMessage message : handler.getMessages(null, true)) {
                        switch (message.getKind()) {
                            case IMessage.ABORT:
                            case IMessage.ERROR:
                            case IMessage.FAIL:
                                log.error message.message, message.thrown
                                break
                            case IMessage.WARNING:
                                log.warn message.message, message.thrown
                                break
                            case IMessage.INFO:
                                log.info message.message, message.thrown
                                break
                            case IMessage.DEBUG:
                                log.debug message.message, message.thrown
                                break
                        }
                    }
                    println "========================================================================Aspectj done"
                }
        }
    }

    //https://github.com/uPhyca/gradle-android-aspectj-plugin
    private static String getVariants(Project project) {
        def hasAppPlugin = project.plugins.hasPlugin(AppPlugin)
        def hasLibPlugin = project.plugins.hasPlugin(LibraryPlugin)
        println("-----------------------------hasAppPlugin $hasAppPlugin")
        println("-----------------------------hasLibPlugin $hasLibPlugin")
        if (!hasAppPlugin && !hasLibPlugin) {
            throw new GradleException("The 'com.android.application' or 'com.android.library' plugin is required.")
        }
        if (hasAppPlugin) {
            return "applicationVariants"
        } else {
            return "libraryVariants"
        }
    }

}