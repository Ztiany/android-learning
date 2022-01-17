package com.ztiany

import com.ztiany.extension.HelloExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 *
 * @author Ztiany
 *          Email ztiany3@gmail.com
 *          Date 2017/4/11 22:38
 */
class HelloPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        print "HelloPlugin run with ${project}"

        //这段代码将HelloExtension添加到project的extensions中
        project.extensions.add('hello', HelloExtension)
        //给Project添加一个task为hello
        project.task('helloTask') << {
            //在task中通过project获取HelloExtension获的message
            println "----------------------${project.hello.message}"
        }

    }
}
