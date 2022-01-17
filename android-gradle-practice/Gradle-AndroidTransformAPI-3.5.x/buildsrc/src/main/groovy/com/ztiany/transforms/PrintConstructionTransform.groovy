package com.ztiany.transforms

import com.android.build.api.transform.*
import com.android.build.gradle.internal.pipeline.TransformManager
import org.apache.commons.codec.digest.DigestUtils
import org.apache.commons.io.FileUtils
import org.gradle.api.Project

/**向每个类的构造函数插入一句代码。【没有处理 jar 中的 class】*/
class PrintConstructionTransform extends Transform {

    private Project project

    PrintConstructionTransform(Project project) {
        this.project = project
    }

    @Override
    String getName() {
        println("PrintConstructionTransform: -------------------------------------------------------------------getName-------------------------------------------------------------------> PrintConstructionTransform")
        //自定义的Transform对应的Task名称
        return 'PrintConstructionTransform'
    }

    @Override
    Set<QualifiedContent.ContentType> getInputTypes() {
        println("PrintConstructionTransform: -------------------------------------------------------------------getInputTypes-------------------------------------------------------------------> CONTENT_CLASS")
        //指定输入的类型，通过这里的设定，可以指定我们要处理的文件类型，这样确保其他类型的文件不会传入
        return TransformManager.CONTENT_CLASS
    }

    @Override
    Set<? super QualifiedContent.Scope> getScopes() {
        println("PrintConstructionTransform: -------------------------------------------------------------------getScopes-------------------------------------------------------------------> SCOPE_FULL_PROJECT")
        //指定Transform的作用范围
        return TransformManager.SCOPE_FULL_PROJECT
    }

    @Override
    boolean isIncremental() {
        println("PrintConstructionTransform: -------------------------------------------------------------------isIncremental-------------------------------------------------------------------> false")
        return false
    }

    @Override
    void transform(Context context, Collection<TransformInput> inputs, Collection<TransformInput> referencedInputs, TransformOutputProvider outputProvider, boolean isIncremental) throws IOException, TransformException, InterruptedException {
        println("PrintConstructionTransform: transform is isIncremental = $isIncremental")
        if (!isIncremental) {
            try {
                outputProvider.deleteAll()
            } catch (e) {
                e.printStackTrace()
            }
        }
        // Transform的inputs有两种类型，一种是目录，一种是jar包，要分开遍历
        inputs.each {
            TransformInput input ->
                //对类型为“文件夹”的input进行遍历
                dirProcess(input, outputProvider)
                //对类型为jar文件的input进行遍历
                jarProcess(input, outputProvider)
        }
    }

    private static void dirProcess(TransformInput input, TransformOutputProvider outputProvider) {
        println("PrintConstructionTransform: -------------------------------------------------------------------directoryInputs-------------------------------------------------------------------")
        input.directoryInputs.each {
            DirectoryInput directoryInput ->
                processSingleDir(directoryInput, outputProvider)
        }
        println()
    }

    private static void processSingleDir(DirectoryInput directoryInput, TransformOutputProvider outputProvider) {
        //E:\code\studio\my_github\Repository\Gradle\TransformAPI\app\build\intermediates\classes\release
        //E:\code\studio\my_github\Repository\Gradle\TransformAPI\app\build\intermediates\classes\debug
        println "PrintConstructionTransform: origin --> ${directoryInput.file}"
        //文件夹里面包含的是我们手写的类以及R.class、BuildConfig.class以及R$XXX.class等
        // 获取output目录
        Inject.injectDir(directoryInput.file.canonicalPath, "com\\ztiany\\transform")
        def dest = outputProvider.getContentLocation(directoryInput.name, directoryInput.contentTypes, directoryInput.scopes, Format.DIRECTORY)
        println("PrintConstructionTransform: dest --> $dest")
        // 将input的目录复制到output指定目录
        FileUtils.copyDirectory(directoryInput.file, dest)
    }

    private static void jarProcess(TransformInput input, TransformOutputProvider outputProvider) {
        println("PrintConstructionTransform: -------------------------------------------------------------------jarInputs-------------------------------------------------------------------")
        input.jarInputs.each {
            JarInput jarInput -> processSingleJar(jarInput, outputProvider)
        }
        println()
    }

    private static void processSingleJar(JarInput jarInput, TransformOutputProvider outputProvider) {
        //jar文件一般是第三方依赖库jar文件
        // 重命名输出文件（同目录copyFile会冲突）
        /*
        E:\code\studio\my_github\Repository\Gradle\TransformAPI\app\libs\rxjava-2.1.4.jar
        C:\Users\Administrator\.android\build-cache\ebec13d62a453a46d257c64a1c87cd28ed5d23a2\output\jars\classes.jar
        C:\Users\Administrator\.android\build-cache\b4862f9b750350158ab7e579de0beb791ebe0774\output\jars\classes.jar
        C:\Users\Administrator\.android\build-cache\1a7feb49c84ccebb94a01fd208beb3ff22141e5b\output\jars\classes.jar
        E:\DevTools\SDK\extras\android\m2repository\com\android\support\support-annotations\26.0.0-alpha1\support-annotations-26.0.0-alpha1.jar
        E:\DevTools\SDK\extras\m2repository\com\android\support\constraint\constraint-layout-solver\1.0.2\constraint-layout-solver-1.0.2.jar
        C:\Users\Administrator\.android\build-cache\9526b8b517eb8181c1546478559c9c0ee4afdc3f\output\jars\classes.jar
        C:\Users\Administrator\.android\build-cache\4173999ec439f9ac7374bd6538cedddef46eda39\output\jars\classes.jar
        C:\Users\Administrator\.android\build-cache\ccb01fa6dfbade9d6ac2bde0e688384a2861099b\output\jars\classes.jar
        C:\Users\Administrator\.android\build-cache\bdd3e075dc3c1f8eb5d167fbb919a6e36cf21e66\output\jars\classes.jar
        C:\Users\Administrator\.android\build-cache\566e3d6a4bf505dd71b7dc354684b0fa956a985b\output\jars\classes.jar
        C:\Users\Administrator\.android\build-cache\09752267c8148847da9262a70bcc1f19de1aec05\output\jars\classes.jar
        C:\Users\Administrator\.android\build-cache\84a2403fa5cce0acddf4b96dc74d9137fe884a17\output\jars\classes.jar
         */

        println("PrintConstructionTransform: ${jarInput.file.getAbsolutePath()}, status = ${jarInput.status}")

        def jarName = jarInput.name

        def md5Name = DigestUtils.md5Hex(jarInput.file.getAbsolutePath())

        if (jarName.endsWith(".jar")) {
            jarName = jarName.substring(0, jarName.length() - 4)
        }

        //生成输出路径
        def dest = outputProvider.getContentLocation(jarName + md5Name, jarInput.contentTypes, jarInput.scopes, Format.JAR)
        println("PrintConstructionTransform: dest --> $dest")
        //将输入内容复制到输出
        FileUtils.copyFile(jarInput.file, dest)
    }

}