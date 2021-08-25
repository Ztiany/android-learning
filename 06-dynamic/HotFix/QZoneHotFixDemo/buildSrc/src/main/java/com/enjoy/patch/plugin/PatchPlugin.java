package com.enjoy.patch.plugin;

import com.android.build.gradle.AppExtension;
import com.android.build.gradle.AppPlugin;
import com.android.build.gradle.api.ApplicationVariant;

import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.io.FileUtils;
import org.gradle.api.Action;
import org.gradle.api.GradleException;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.execution.TaskExecutionListener;
import org.gradle.api.tasks.TaskOutputs;
import org.gradle.api.tasks.TaskState;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;
import java.util.regex.Matcher;

/**
 * 插桩与补丁生成插件。
 */
public class PatchPlugin implements Plugin<Project> {

    @Override
    public void apply(Project project) {

        if (!project.getPlugins().hasPlugin(AppPlugin.class)) {
            throw new GradleException("无法在非 android application 插件中使用热修复插件");
        }

        //创建一个patch{}配置
        //就和引入了 apply plugin: 'com.android.application' 一样，可以配置android{}
        project.getExtensions().create("patch", PatchExtension.class);

        //gradle执行会解析build.gradle文件，afterEvaluate表示在解析完成之后再执行我们的代码
        project.afterEvaluate(new Action<Project>() {

            @Override
            public void execute(final Project project) {
                printAllTaskInputAndOutput(project);

                //获得用户的配置，在debug模式下是否开启热修复
                final PatchExtension patchExtension = project.getExtensions().findByType(PatchExtension.class);
                //debug模式下是否也进行插桩
                final boolean debugOn = patchExtension.debugOn;
                //得到 android 的配置
                AppExtension android = project.getExtensions().getByType(AppExtension.class);

                // android 项目默认会有 debug 和 release，
                // 那么 getApplicationVariants 就是包含了 debug 和 release 的集合，all 表示对集合进行遍历。
                android.getApplicationVariants().all(new Action<ApplicationVariant>() {
                    @Override
                    public void execute(ApplicationVariant applicationVariant) {
                        //当前用户是 debug 模式，并且没有配置debug运行执行热修复
                        if (applicationVariant.getName().contains("debug") && !debugOn) {
                            return;
                        }
                        //配置热修复插件生成补丁的一系列任务
                        configTasks(project, applicationVariant, patchExtension);
                    }
                });
            }
        });
    }

    private void printAllTaskInputAndOutput(final Project project) {
        project.getGradle().getTaskGraph().addTaskExecutionListener(new TaskExecutionListener() {
            @Override
            public void beforeExecute(Task task) {

            }

            @Override
            public void afterExecute(Task task, TaskState taskState) {
                try {
                    project.getLogger().error("afterExecute-->" + task);
                    project.getLogger().error("        input: ");
                    for (File file : task.getInputs().getFiles().getFiles()) {
                        project.getLogger().error("                " + file);
                    }

                    project.getLogger().error("        output: ");
                    for (File file : task.getOutputs().getFiles().getFiles()) {
                        project.getLogger().error("                " + file);
                    }
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    static void configTasks(final Project project, final ApplicationVariant variant, final PatchExtension patchExtension) {
        //获得: debug/release
        String variantName = variant.getName();

        //首字母大写：Debug/Release
        String capitalizeName = Utils.capitalize(variantName);

        //热修复补丁包的输出目录
        File outputDir;
        //如果没有指名输出目录，默认输出到 build/patch/debug(release) 下
        if (!Utils.isEmpty(patchExtension.output)) {
            outputDir = new File(patchExtension.output, variantName);
        } else {
            outputDir = new File(project.getBuildDir(), "patch/" + variantName);
        }
        outputDir.mkdirs();

        // ----------------------------------- 如果开启了混淆，则进行混淆配置，应用上一次的混淆规则 -----------------------------------
        //获得 android 的混淆任务：transformClassesAndResourcesWithProguardForDebug/transformClassesAndResourcesWithProguardForRelease
        final Task proguardTask = project.getTasks().findByName("transformClassesAndResourcesWithProguardFor" + capitalizeName);
        project.getLogger().error("configTasks: getTask = " + "transformClassesAndResourcesWithProguardFor" + capitalizeName + " : " + proguardTask);

        //备份本次的 mapping 文件
        final File mappingBak = new File(outputDir, "mapping.txt");

        //如果没开启混淆，则为 null，不需要备份 mapping
        if (proguardTask != null) {

            // 在混淆后备份mapping
            proguardTask.doLast(/*doLast：在这个任务之后再干一些事情*/new Action<Task>() {
                @Override
                public void execute(Task task) {
                    //混淆任务输出的所有文件
                    TaskOutputs outputs = proguardTask.getOutputs();
                    Set<File> files = outputs.getFiles().getFiles();
                    for (File file : files) {
                        //把 mapping 文件备份
                        if (file.getName().endsWith("mapping.txt")) {
                            try {
                                FileUtils.copyFile(file, mappingBak);
                                project.getLogger().error("备份混淆 mapping 文件:" + mappingBak.getCanonicalPath());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            break;
                        }
                    }
                }
            });//doLast end

        }

        //将上次混淆的 mapping 应用到本次，如果没有上次的混淆文件就没操作。
        Utils.applyMapping(proguardTask, mappingBak);

        // 在混淆后，记录类的 hash 值，并生成补丁包。
        final File hexFile = new File(outputDir, "hex.txt");

        // 用于打包所有补丁的类的 jar 包
        final File patchClassFile = new File(outputDir, "patchClass.jar");
        // 用 dx 打包后的jar包
        final File patchFile = new File(outputDir, "patch.jar");

        //找到打包 dex 的任务：transformClassesWithDexBuilderForDebug/transformClassesWithDexBuilderForRelease
        final Task dexTask = project.getTasks().findByName("transformClassesWithDexBuilderFor" + capitalizeName);
        project.getLogger().error("configTasks: getTask = " + "transformClassesWithDexBuilderFor" + capitalizeName + " : " + dexTask);

        // 在把class打包dex之前，插桩并记录每个class的md5 hash值
        dexTask.doFirst(/*doFirst：在任务之前执行的动作*/new Action<Task>() {
            @Override
            public void execute(Task task) {

                /* 插桩 记录md5并对比 */
                PatchGenerator patchGenerator = new PatchGenerator(project, patchFile, patchClassFile, hexFile);

                //用户配置的 application，实际上可以解析 manifest 自动获取，但是 java 实现太麻烦了，干脆让用户自己配置。
                String applicationName = patchExtension.applicationName;
                //windows下 目录输出是  xx\xx\，linux下是  /xx/xx，把 . 替换成平台相关的斜杠。
                applicationName = applicationName.replaceAll("\\.", Matcher.quoteReplacement(File.separator));
                //记录类的md5
                Map<String, String> newHexs = new HashMap<>();

                //任务的输入，dex 打包任务要输入什么？ 自然是所有的 class 与 jar 包了！
                Set<File> files = dexTask.getInputs().getFiles().getFiles();

                for (File file : files) {
                    String filePath = file.getAbsolutePath();
                    if (filePath.endsWith(".jar")) {
                        processJar(applicationName, file, newHexs, patchGenerator);
                        project.getLogger().error("------>processJar: " + filePath);
                    } else if (filePath.endsWith(".class")) {
                        processClass(applicationName, variant.getDirName(), file, newHexs, patchGenerator);
                        project.getLogger().error("------>processClass: " + filePath);
                    }
                }

                //类的md5集合 写入到文件
                Utils.writeHex(newHexs, hexFile);

                try {
                    //生成补丁
                    patchGenerator.generate();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * @param applicationName applicationName = com\enjoy\qzonefix\MyApplication
     * @param dirName         release
     * @param file            D:\code\code\Android\Dynamic\HotFix\QZoneHotFixDemo\app\build\intermediates\classes\release\android\support\customview\???.class
     */
    static void processClass(String applicationName, String dirName, File file, Map<String, String> hexs, PatchGenerator patchGenerator) {
        String filePath = file.getAbsolutePath();

        //注意这里的 filePath 包含了目录+包名+类名，所以去掉目录，得到类似 android\support\customview\???.class 的全类路径
        String className = filePath.split(dirName)[1].substring(1);

        //application 或者 android support 我们不管
        if (className.startsWith(applicationName) || Utils.isAndroidClass(className)) {
            return;
        }

        try {
            FileInputStream is = new FileInputStream(filePath);

            //1：在改类中插入对 AntilazyLoad 类的引用
            byte[] byteCode = ClassUtils.referHackWhenInit(is);
            //2：读取插入后的数字摘要
            String hex = Utils.hex(byteCode);
            is.close();

            //3：覆盖原有类
            FileOutputStream os = new FileOutputStream(filePath);
            os.write(byteCode);
            os.close();

            //4：记录改类的数字摘要信息
            hexs.put(className, hex);

            //5：对比缓存的数字摘要，不一致则认为是修改过的类，则放入补丁。（这里也可以使用注解的方式，就将需要带入补丁的类添加上特定的注解，以免补丁中的类过多。）
            patchGenerator.checkClass(className, hex, byteCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param applicationName applicationName = com\enjoy\qzonefix\MyApplication
     * @param file            D:\code\code\Android\Dynamic\HotFix\QZoneHotFixDemo\app\build\intermediates\classes\release\android\support\customview\???.class
     */
    static void processJar(String applicationName, File file, Map<String, String> hexs, PatchGenerator patchGenerator) {
        try {
            //在 jar 中，无论是 windows 还是 linux，分隔符都是 /
            applicationName = applicationName.replaceAll(Matcher.quoteReplacement(File.separator), "/");
            //原 jar 包
            JarFile jarFile = new JarFile(file);
            //临时 jar 包
            File bakJar = new File(file.getParent(), file.getName() + ".bak");
            JarOutputStream jarOutputStream = new JarOutputStream(new FileOutputStream(bakJar));

            //遍历原 jar 包
            Enumeration<JarEntry> entries = jarFile.entries();
            while (entries.hasMoreElements()) {
                //1：获取原 jar 中的一个元素
                JarEntry jarEntry = entries.nextElement();
                //2：将获得的元素放入到临时 jar 中
                jarOutputStream.putNextEntry(new JarEntry(jarEntry.getName()));

                InputStream is = jarFile.getInputStream(jarEntry);//元素的输入流
                String className = jarEntry.getName();//元素的名称

                //3：过滤掉不需要处理的类和其他类型的文件，仅处理需要处理的 class。
                if (className.endsWith(".class") && !className.startsWith(applicationName) && !Utils.isAndroidClass(className) && !className.startsWith("com/enjoy" + "/patch")) {

                    //4：在改类中插入对 AntilazyLoad 类的引用
                    byte[] byteCode = ClassUtils.referHackWhenInit(is);
                    String hex = Utils.hex(byteCode);
                    is.close();
                    hexs.put(className, hex);
                    //5：对比缓存的数字摘要，不一致则认为是修改过的类，则放入补丁。（这里也可以使用注解的方式，就将需要带入补丁的类添加上特定的注解，以免补丁中的类过多。）
                    patchGenerator.checkClass(className, hex, byteCode);

                    //6：处理后的类输出到临时 jar 文件。
                    jarOutputStream.write(byteCode);
                } else {
                    //7：不需要处理的类，直接输出到临时 jar 文件。
                    jarOutputStream.write(IOUtils.toByteArray(is));
                }

                jarOutputStream.closeEntry();
            }

            //8：临时 jar 替换到原来的 jar
            jarOutputStream.close();
            jarFile.close();
            file.delete();
            bakJar.renameTo(file);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}