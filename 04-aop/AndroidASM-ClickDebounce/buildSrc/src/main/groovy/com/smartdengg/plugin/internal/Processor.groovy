package com.smartdengg.plugin.internal

import com.google.common.collect.ImmutableMap
import com.google.common.collect.Iterables
import com.smartdengg.compile.CheckAndCollectClassAdapter
import com.smartdengg.compile.DebounceModifyClassAdapter
import com.smartdengg.compile.MethodDescriptor
import com.smartdengg.compile.WovenClass
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter

import java.nio.file.*
import java.nio.file.attribute.BasicFileAttributes

/**编织工作处理器，用于执行具体的编织工作*/
class Processor {

    enum Input {
        JAR,
        FILE
    }

    static void run(Path input, Path output, List<WovenClass> wovenClasses, Map<String, List<String>> exclusion, Input type) throws IOException {

        switch (type) {

            case Input.JAR:
                processJar(input, output, wovenClasses, exclusion)
                break

            case Input.FILE:
                processFile(input, output, wovenClasses, exclusion)
                break
        }
    }

    /**
     * 这里使用的是 FileSystems 来遍历 jar 中的所有元素，然后按照文件的处理方式进行复制。。
     */
    private static void processJar(Path input, Path output, List<WovenClass> wovenClasses, Map<String, List<String>> exclusion) {

        Map<String, String> env = ImmutableMap.of('create', 'true')

        URI inputUri = URI.create("jar:file:/$input".replaceAll("\\\\", "/"))
        URI outputUri = URI.create("jar:file:/$output".replaceAll("\\\\", "/"))

        FileSystems.newFileSystem(inputUri, env).withCloseable { inputFileSystem ->

            ColoredLogger.logRed("processJar.-->inputFileSystem = ${inputFileSystem.toString()}")

            FileSystems.newFileSystem(outputUri, env).withCloseable { outputFileSystem ->
                ColoredLogger.logRed("processJar.-->outputFileSystem = ${outputFileSystem.toString()}")

                Path inputRoot = Iterables.getOnlyElement(inputFileSystem.rootDirectories)
                Path outputRoot = Iterables.getOnlyElement(outputFileSystem.rootDirectories)

                processFile(inputRoot, outputRoot, wovenClasses, exclusion)
            }

        }
    }

    private static void processFile(Path input, Path output, List<WovenClass> wovenClasses, Map<String, List<String>> exclusion) {
        Files.walkFileTree(input, new SimpleFileVisitor<Path>() {
            @Override
            FileVisitResult visitFile(Path inputPath, BasicFileAttributes attrs) throws IOException {
                Path outputPath = Utils.toOutputPath(output, input, inputPath)
                directRun(inputPath, outputPath, wovenClasses, exclusion)
                return FileVisitResult.CONTINUE
            }

            @Override
            FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                Path outputPath = Utils.toOutputPath(output, input, dir)
                Files.createDirectories(outputPath)
                return FileVisitResult.CONTINUE
            }
        })
    }

    static void directRun(Path input, Path output, List<WovenClass> wovenClasses, Map<String, List<String>> exclusion) {
        /*排除R.class 或者 BuildConfig.class*/
        if (Utils.isMatchCondition(input.toString())) {
            //读取待编织类字节码
            byte[] inputBytes = Files.readAllBytes(input)
            //开始进行编织
            byte[] outputBytes = visitAndReturnBytecode(inputBytes, wovenClasses, exclusion)
            /*把编织过后的类写过去*/
            Files.write(output, outputBytes)
        } else {
            /*直接复制过去*/
            Files.copy(input, output)
        }
    }

    /**真正的编织过程*/
    private static byte[] visitAndReturnBytecode(byte[] originBytes, List<WovenClass> wovenClasses, Map<String, List<String>> exclusion) {
        ClassReader classReader = new ClassReader(originBytes)
        ClassWriter classWriter = new ClassWriter(classReader, 0)

        /*收集一个类中，所有需要进行编织的方法*/
        Map<String, List<MethodDescriptor>> map = checkAndCollect(originBytes, exclusion)

        /*开始进行真正的 Click/OnItemClick 方法编织*/
        DebounceModifyClassAdapter classAdapter = new DebounceModifyClassAdapter(classWriter, map)

        try {
            /*
            ClassReader.EXPAND_FRAMES：ClassReader类提供了其他一些选项，
                        比如：SKIP_CODE，用于跳过对已编译代码的访问（如果只需要类的结构，那这个选项是很有用的）；
                        SKIP_FRAMES，用于跳过栈映射帧；EXPAND_FRAMES，用于解压缩这些帧。
                        从Java 6开始，除了字节代码之外，已编译类中还包含了一组栈映射帧。这一技术用于对帧进行压缩，为节省空间，已编译方法中并没有为每条指令包含一个帧

                即 EXPAND_FRAMES，旨在说明在读取 class 的时候需要同时展开栈映射帧（StackMap Frame），
                如果我们需要使用自定义的 MethodVisitor 去修改方法中的指令时必须要指定这个参数
             */
            classReader.accept(classAdapter, ClassReader.EXPAND_FRAMES)
            //move to visit end?
            wovenClasses.add(classAdapter.getWovenClass())
            return classWriter.toByteArray()
        } catch (Exception e) {
            e.printStackTrace()
        }

        return originBytes
    }

    private static Map<String, List<MethodDescriptor>> checkAndCollect(byte[] bytes, Map<String, List<String>> exclusion) {
        CheckAndCollectClassAdapter visitor = new CheckAndCollectClassAdapter(exclusion)
        try {
            new ClassReader(bytes).accept(visitor, ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES)
        } catch (Exception e) {
            e.printStackTrace()
        }
        return visitor.getUnWeavedClassMap()
    }

}