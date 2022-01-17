package com.ztiany.transforms

import com.android.build.gradle.AppExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

class TransformPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        def android = project.extensions.findByType(AppExtension)
        android.registerTransform(new LogTransform())
        android.registerTransform(new PrintConstructionTransform(project))
    }

}