package com.licola.llogger.plugin

import com.android.build.api.transform.*
import com.android.build.gradle.internal.pipeline.TransformManager
import org.apache.commons.codec.digest.DigestUtils
import org.apache.commons.io.FileUtils

class LoggerTransform extends Transform {

    /**
     * transform 名字
     */
    @Override
    String getName() {
        return LoggerTransform.simpleName
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

        TransformOutputProvider outputProvider = transformInvocation.outputProvider

        def inputs = transformInvocation.inputs

        LoggerCodeApi codeApi = new LoggerCodeApi()

        for (TransformInput input in inputs) {
            //scan class files
            for (DirectoryInput directoryInput in input.directoryInputs) {

                File src = directoryInput.file

                File dest = outputProvider.getContentLocation(directoryInput.name, directoryInput.contentTypes, directoryInput.scopes, Format.DIRECTORY)

                codeApi.readClassWithPath(src, dest)

                // copy to dest
                FileUtils.copyDirectory(src, dest)
            }

            //scan all jars
            for (JarInput jarInput in input.jarInputs) {

                String destName = jarInput.name
                //rename
                def hexName = DigestUtils.md5Hex(jarInput.file.absolutePath)
                if (destName.endsWith(".jar")) {
                    destName = destName.substring(0, destName.length() - ".jar".length())
                }
                //input file
                File src = jarInput.file

                //output file
                File dest = outputProvider.getContentLocation("${destName}_${hexName}", jarInput.contentTypes, jarInput.scopes, Format.JAR)

                codeApi.readClassWithJar(src, dest)

                // copy to dest
                FileUtils.copyFile(src, dest)
            }
        }

        codeApi.process()
    }
}