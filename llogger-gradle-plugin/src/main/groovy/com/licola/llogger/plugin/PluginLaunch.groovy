package com.licola.llogger.plugin

import com.android.build.gradle.AppExtension
import com.android.build.gradle.AppPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.logging.Logger

class PluginLaunch implements Plugin<Project> {

    static Logger logger

    @Override
    void apply(Project project) {
        logger = project.getLogger()

        logger.info("project start plugin")

        def isApp = project.plugins.hasPlugin(AppPlugin)
        if (isApp) {
            logger = project.getLogger()

            def android = project.extensions.getByType(AppExtension)
            def transform = new LoggerTransform()

            android.registerTransform(transform)
        }
    }
}