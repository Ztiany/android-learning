package com.maniu.robust
import org.gradle.api.Plugin
import org.gradle.api.Project

class ModifyPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        println "99号技师来了"
    }

}