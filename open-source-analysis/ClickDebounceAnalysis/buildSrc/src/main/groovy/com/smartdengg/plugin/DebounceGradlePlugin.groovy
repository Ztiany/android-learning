package com.smartdengg.plugin

import com.android.build.gradle.*
import com.android.build.gradle.api.BaseVariant
import com.android.builder.model.AndroidProject
import com.android.utils.FileUtils
import com.smartdengg.compile.WovenClass
import com.smartdengg.plugin.api.DebounceExtension
import com.smartdengg.plugin.internal.Utils
import groovy.util.logging.Slf4j
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.internal.reflect.Instantiator

import javax.inject.Inject

@Slf4j
class DebounceGradlePlugin implements Plugin<Project> {

    private final Instantiator instantiator

    @Inject
    DebounceGradlePlugin(Instantiator instantiator) {
        this.instantiator = instantiator
    }

    @Override
    void apply(Project project) {

        /*判断是否存在 Android Gradle Plugin*/
        def androidPlugin = [AppPlugin, LibraryPlugin, FeaturePlugin]
                .collect { project.plugins.findPlugin(it) as BasePlugin }
                .find { it != null }

        log.debug('Found Plugin: {}', androidPlugin)

        if (!androidPlugin) {
            throw new GradleException("'com.android.application' or 'com.android.library' or 'com.android.feature' plugin required.")
        }

        project.extensions["${DebounceExtension.NAME}"] = instantiator.newInstance(DebounceExtension)

        def extension = project.extensions.getByName("android") as BaseExtension
        def variantWeavedClassesMap = new LinkedHashMap<String, List<WovenClass>>()

        extension.registerTransform(new DebounceIncrementalTransform(project, variantWeavedClassesMap))

        project.afterEvaluate {
            Utils.forEachVariant(extension) { BaseVariant variant ->
                createWriteMappingTask(project, variant, variantWeavedClassesMap)
            }
        }

    }

    static void createWriteMappingTask(Project project, BaseVariant variant, Map<String, List<WovenClass>> variantWeavedClassesMap) {

        def mappingTaskName = "outputMappingFor${variant.name.capitalize()}"

        Task debounceTask = project.tasks[
                "transformClassesWith${DebounceIncrementalTransform.TASK_NAME.capitalize()}For${variant.name.capitalize()}"
        ]

        Task outputMappingTask = project.tasks.create(name: "${mappingTaskName}", type: OutputMappingTask) {
            classes = variantWeavedClassesMap
            variantName = variant.name
            outputMappingFile = FileUtils.join(project.buildDir, AndroidProject.FD_OUTPUTS, 'debounce', 'logs', variant.name, 'classes.txt')
        }

        debounceTask.configure(Utils.taskTimedConfigure)
        outputMappingTask.configure(Utils.taskTimedConfigure)

        debounceTask.finalizedBy(outputMappingTask)
        outputMappingTask.onlyIf { debounceTask.didWork }
        outputMappingTask.inputs.files(debounceTask.outputs.files)
    }

}


