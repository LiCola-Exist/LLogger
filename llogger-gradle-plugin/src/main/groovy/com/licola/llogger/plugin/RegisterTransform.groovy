package com.licola.llogger.plugin

import com.android.build.api.transform.*
import com.android.build.gradle.internal.pipeline.TransformManager

public class RegisterTransform extends Transform {

    /**
     * transform 名字
     */
    @Override
    String getName() {
        return "MyTransform"
    }

    /**
     * 输入文件的类型
     * 可供我们去处理的有两种类型, 分别是编译后的java代码, 以及资源文件(非res下文件, 而是assests内的资源)
     */
    @Override
    Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS
    }

    /**
     * 指定作用范围
     */
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

        def inputs = transformInvocation.inputs

        for (TransformInput input in inputs) {
            for (DirectoryInput item in input.directoryInputs) {
                println("dir:${item}")
            }

            for (JarInput item in input.jarInputs) {
                println("jar:${item}")
            }
        }
    }
}