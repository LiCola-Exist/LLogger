package com.licola.llogger.plugin

import org.gradle.BuildListener
import org.gradle.BuildResult
import org.gradle.api.Task
import org.gradle.api.execution.TaskExecutionListener
import org.gradle.api.initialization.Settings
import org.gradle.api.invocation.Gradle
import org.gradle.api.tasks.TaskState

public class TaskTimeListener implements TaskExecutionListener, BuildListener {

    private long startTime;
    private times = []

    @Override
    void beforeExecute(Task task) {
        startTime = System.currentTimeMillis()
    }

    @Override
    void afterExecute(Task task, TaskState taskState) {
        def duration = System.currentTimeMillis() - startTime
        times.add([duration, task.path])
//        task.project.logger.warn "${task.path} spend ${duration}ms"
    }

    @Override
    void buildStarted(Gradle gradle) {

    }

    @Override
    void settingsEvaluated(Settings settings) {

    }

    @Override
    void projectsLoaded(Gradle gradle) {

    }

    @Override
    void projectsEvaluated(Gradle gradle) {

    }

    @Override
    void buildFinished(BuildResult buildResult) {
        println "my task speed time:"
        for (time in times) {
            printf "${time} \n"
        }
    }
}