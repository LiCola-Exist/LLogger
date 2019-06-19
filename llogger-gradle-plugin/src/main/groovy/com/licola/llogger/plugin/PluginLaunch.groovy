package com.licola.llogger.plugin

import com.android.build.gradle.AppExtension
import com.android.build.gradle.AppPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.logging.Logger

public class PluginLaunch implements Plugin<Project>{

    static Logger logger

    @Override
    void apply(Project project) {

        project.extensions.create("e1", Extension)

        project.task("readExt") << {
            println "e1=${project["e1"].testVar}"
        }
        project.gradle.addListener(new TaskTimeListener())


        def isApp= project.plugins.hasPlugin(AppPlugin)
        if (isApp){
            logger= project.getLogger()

            logger.info("project start plugin")

            def android= project.extensions.getByType(AppExtension)
            def transform= new RegisterTransform()

            android.registerTransform(transform)
        }
    }
}