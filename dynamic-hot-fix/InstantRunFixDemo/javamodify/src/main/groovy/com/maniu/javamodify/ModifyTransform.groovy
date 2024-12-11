package com.maniu.javamodify
import com.android.SdkConstants
import com.android.build.api.transform.Format
import com.android.build.api.transform.QualifiedContent
import com.android.build.api.transform.Transform
import com.android.build.api.transform.TransformException
import com.android.build.api.transform.TransformInvocation
import com.android.build.gradle.internal.pipeline.TransformManager
import javassist.ClassPool
import javassist.CtClass
import javassist.CtMethod
import org.apache.commons.io.FileUtils
import org.gradle.api.Project


class ModifyTransform extends Transform {

    def pool = ClassPool.default

    def project

    ModifyTransform(Project project) {
        this.project = project
    }

    @Override
    String getName() {
        return "david"
    }

    @Override
    Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS
    }

    @Override
    Set<? super QualifiedContent.Scope> getScopes() {
        return TransformManager.SCOPE_FULL_PROJECT
    }

    @Override
    boolean isIncremental() {
        return false
    }

    @Override
    void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {
        super.transform(transformInvocation)

        project.android.bootClasspath.each {
            pool.appendClassPath(it.absolutePath)
        }

        transformInvocation.inputs.each {
            it.jarInputs.each {
                pool.insertClassPath(it.file.absolutePath)
                def dest= transformInvocation.outputProvider.getContentLocation(it.name, it.contentTypes, it.scopes, Format.JAR)
                FileUtils.copyFile(it.file, dest)
            }

            it.directoryInputs.each {
                def preFileName = it.file.absolutePath
                pool.insertClassPath(preFileName)
                findTarget(it.file, preFileName)
                def dest= transformInvocation.outputProvider.getContentLocation(it.name, it.contentTypes, it.scopes, Format.DIRECTORY)
                // 将input的目录复制到output指定目录
                FileUtils.copyDirectory(it.file, dest)
            }
        }
    }

    private void findTarget(File dir, String fileName) {
        if (dir.isDirectory()) {
            dir.listFiles().each {
                findTarget(it, fileName)
            }
        }else {
            modify(dir,fileName)
        }
    }

    private void modify(File dir, String fileName) {
        def filePath = dir.absolutePath

        if (!filePath.endsWith(SdkConstants.DOT_CLASS)) {
            return
        }

        if (filePath.contains('R$') || filePath.contains('R.class') || filePath.contains("BuildConfig.class")) {
            return
        }

        def className = filePath.replace(fileName, "")
                .replace("\\", ".")
                .replace("/", ".")

        println "========className======== " + className
        def name = className.replace(SdkConstants.DOT_CLASS, "").substring(1)
        CtClass ctClass=pool.get(name)

        //如果这个类是在 com.maniu.robustfix 需要加入这些代码  if 判断。
        if (name.contains("com.maniu.robustfix")) {
            def body="if (com.maniu.robustfix.PatchProxy.isSupport()) {}"
            addCode(ctClass, body, fileName)
        }
    }

    private void addCode(CtClass ctClass, String body, String fileName) {
        if (ctClass.getName().contains("PatchProxy")) {
            return;
        }
        CtMethod[] methods = ctClass.getDeclaredMethods()
        for (method in methods) {
            if (method.getName().contains("isSupport")) {
                continue
            }
            method.insertBefore(body)
        }
        ctClass.writeFile(fileName)
        ctClass.detach()
    }

}