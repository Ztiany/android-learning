package com.maniu.javamodify

import org.gradle.api.Plugin
import org.gradle.api.Project

class ModifyPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        project.android.registerTransform(new ModifyTransform(project))
    }

}