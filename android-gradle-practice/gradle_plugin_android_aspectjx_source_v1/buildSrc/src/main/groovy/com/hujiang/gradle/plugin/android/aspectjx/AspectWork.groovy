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

import org.aspectj.bridge.IMessage
import org.aspectj.bridge.MessageHandler
import org.aspectj.tools.ajc.Main
import org.gradle.api.GradleException
import org.gradle.api.Project

/**
 * 执行aspectj编织工作
 * aspectj weave code logic here
 * @author simon
 * @version 1.0.0
 * @since 2016-04-20
 */
class AspectWork {

    Project project
    public String encoding
    public ArrayList<File> inPath = new ArrayList<File>()
    public ArrayList<File> aspectPath = new ArrayList<File>()
    public ArrayList<File> classPath = new ArrayList<File>()
    public String bootClassPath
    public String sourceCompatibility
    public String targetCompatibility
    public String destinationDir
    public List<String> ajcArgs

    AspectWork(Project proj) {
        project = proj
    }

    void doWork() {
        println "aspect do work.........."
        final def log = project.logger

        //http://www.eclipse.org/aspectj/doc/released/devguide/ajc-ref.html
        //
        // -sourceRoots:
        //  Find and build all .java or .aj source files under any directory listed in DirPaths. DirPaths, 找到所有的java或者.aj的编织类
        // like classpath,
        // is a single argument containing a list of paths to directories, delimited by the platform- specific classpath delimiter. Required by -incremental.

        // -inPath:
        //  Accept as source bytecode any .class files in the .jar files or directories on Path. ：将被编织的的类或jar文件
        // The output will include these classes, possibly as woven with any applicable aspects. ：输出将包含这些类
        // Path is a single argument containing a list of paths to zip files or directories, delimited by the platform-specific path delimiter.： 路径是一个参数包含一个zip文件或目录的路径列表,由平台特定的路径分隔符分隔

        // -classpath:
        //  Specify where to find user class files.//指定在哪里可以找到用户类文件。
        // Path is a single argument containing a list of paths to zip files or directories, delimited by the platform-specific path delimiter.

        // -aspectPath:
        //  Weave binary aspects from jar files and directories on path into all sources.。：将jar文件和目录中的二进制方法从路径整理到所有源文件中。
        // The aspects should have been output by the same version of the compiler.
        // When running the output classes, the run classpath should contain all aspectPath entries. Path, like classpath, is a single argument containing a list of paths to jar files, delimited by the platform- specific classpath delimiter.

        // -bootclasspath:
        //  Override location of VM's bootclasspath for purposes of evaluating types when compiling. ：覆盖VM的引导类路径的位置，以便在编译时评估类型。
        // Path is a single argument containing a list of paths to zip files or directories, delimited by the platform-specific path delimiter.

        // -d:
        //  Specify where to place generated .class files. If not specified, Directory defaults to the current working dir.：指定输出目录

        // -preserveAllLocals:
        //  Preserve all local variables during code generation (to facilitate debugging).：保存所有局部变量


        def args = [
                "-showWeaveInfo",
                "-encoding", encoding,
                "-source", sourceCompatibility,
                "-target", targetCompatibility,
                "-d", destinationDir,
                "-classpath", classPath.join(File.pathSeparator),
                "-bootclasspath", bootClassPath
        ]

        if (!getInPath().isEmpty()) {
            args << '-inpath'
            args << getInPath().join(File.pathSeparator)
        }

        if (!getAspectPath().isEmpty()) {
            args << '-aspectpath'
            args << getAspectPath().join(File.pathSeparator)
        }

        if (ajcArgs != null && !ajcArgs.isEmpty()) {
            if (!ajcArgs.contains('-Xlint')) {
                args.add('-Xlint:ignore')
            }
            if (!ajcArgs.contains('-warn')) {
                args.add('-warn:none')
            }

            args.addAll(ajcArgs)
        } else {
            args.add('-Xlint:ignore')//为横切代码中潜在的编程错误的消息设置默认级别
            args.add('-warn:none')// suppress all compiler warnings
        }

        MessageHandler handler = new MessageHandler(true);
        Main m = new Main()
        m.run(args as String[], handler)
        for (IMessage message : handler.getMessages(null, true)) {
            switch (message.getKind()) {
                case IMessage.ABORT:
                case IMessage.ERROR:
                case IMessage.FAIL:
                    log.error message.message, message.thrown
                    throw new GradleException(message.message, message.thrown)
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
        m.quit()
    }


    String getEncoding() {
        return encoding
    }

    void setEncoding(String encoding) {
        this.encoding = encoding
    }

    ArrayList<File> getInPath() {
        return inPath
    }

    void setInPath(ArrayList<File> inPath) {
        this.inPath = inPath
    }

    ArrayList<File> getAspectPath() {
        return aspectPath
    }

    void setAspectPath(ArrayList<File> aspectPath) {
        this.aspectPath = aspectPath
    }

    ArrayList<File> getClassPath() {
        return classPath
    }

    void setClassPath(ArrayList<File> classPath) {
        this.classPath = classPath
    }

    String getBootClassPath() {
        return bootClassPath
    }

    void setBootClassPath(String bootClassPath) {
        this.bootClassPath = bootClassPath
    }

    String getSourceCompatibility() {
        return sourceCompatibility
    }

    void setSourceCompatibility(String sourceCompatibility) {
        this.sourceCompatibility = sourceCompatibility
    }

    String getTargetCompatibility() {
        return targetCompatibility
    }

    void setTargetCompatibility(String targetCompatibility) {
        this.targetCompatibility = targetCompatibility;
    }

    String getDestinationDir() {
        return destinationDir
    }

    void setDestinationDir(String destinationDir) {
        this.destinationDir = destinationDir;
    }

    void setAjcArgs(List<String> ajcArgs) {
        this.ajcArgs = ajcArgs
    }
}
