package com.licola.llogger.plugin

import javassist.ClassPool
import javassist.CtClass
import javassist.CtMethod
import org.apache.commons.io.IOUtils
import org.objectweb.asm.ClassReader
import org.objectweb.asm.tree.ClassNode

import java.util.jar.JarEntry
import java.util.jar.JarFile
import java.util.jar.JarOutputStream
import java.util.zip.ZipEntry

class LoggerCodeApi {

    private static final boolean DEBUG = true

    private static final String TARGET_INVOKE_CLASS = "com.licola.llogger.LLogger"
    private static final String TARGET_INVOKE_METHOD_NAME = "d"
    private static final String TARGET_CLASS_METHOD_NAME = "onClick"

    private static final String TARGET_INTERFACE_NAME = "android/view/View\$OnClickListener"

    HashMap<String, File> targetInfoMap = new HashMap<>()


    void log(String msg) {
        if (DEBUG) {
            println("api: " + msg)
        }
    }

    void process() {
        if (targetInfoMap.isEmpty()) {
            log("map is empty")
            return
        }

        for (Map.Entry<String, File> entry : targetInfoMap.entrySet()) {
            File targetFile = entry.value
            String targetClassName = entry.key
            log("map-> ${entry.key} : ${entry.value}")

            if (targetFile.getName().endsWith(".jar")) {
                insertCodeJar(targetClassName, targetFile)
            } else {
                insertCodeDir(targetClassName, targetFile)
            }

        }

    }

    void insertCodeJar(String targetClassName, File jarFile) {
        log("insercode jarFile:${jarFile.toString()}")
        def optJar = new File(jarFile.getParent(), jarFile.name + ".opt")
        if (optJar.exists()) {
            optJar.delete()
        }
        JarFile file = new JarFile(jarFile)
        Enumeration<JarEntry> enumeration = file.entries()
        JarOutputStream jarOutputStream = new JarOutputStream(new FileOutputStream(optJar))

        while (enumeration.hasMoreElements()) {
            JarEntry jarEntry = enumeration.nextElement()
            String entryName = jarEntry.getName()
            String className = entryName.substring(0, entryName.length() - ".class".length()).replaceAll("/", ".")

            ZipEntry zipEntry = new ZipEntry(entryName)
            InputStream inputStream = file.getInputStream(jarEntry)
            jarOutputStream.putNextEntry(zipEntry)

            if (targetClassName == className) {
                jarOutputStream.write(hackTarget(targetClassName, jarFile))
            } else {
                jarOutputStream.write(IOUtils.toByteArray(inputStream))
            }
            inputStream.close()
            jarOutputStream.closeEntry()
        }
        jarOutputStream.close()
        file.close()

        if (jarFile.exists()) {
            jarFile.delete()
        }

        optJar.renameTo(jarFile)

    }


    void insertCodeDir(String targetClassName, File dirFile) {
    }

    byte[] hackTarget(String targetClassName, File classFile) {
        log("start hack")

        ClassPool classPool = ClassPool.getDefault()
        classPool.insertClassPath(classFile.absolutePath)

        CtClass ctClass = classPool.get(targetClassName)
        if (ctClass.frozen) {
            //只需要修改一次 如果被修改过直接返回
            return ctClass.toBytecode()
        }

        CtMethod ctMethod = ctClass.getDeclaredMethod(TARGET_CLASS_METHOD_NAME)

        ctMethod.insertBefore("${TARGET_INVOKE_CLASS}.${TARGET_INVOKE_METHOD_NAME}(\"注入的点击事件打印\");")

        def bytes = ctClass.toBytecode()

        return bytes
    }

    void readClassWithPath(File input, File dest) {
        def root = input.absolutePath
        input.eachFileRecurse { File file ->
            String filePath = file.absoluteFile
            if (!file.isFile()) return
            if (!filePath.endsWith(".class")) return
            def className = getClassName(root, filePath)
            InputStream inputStream = new FileInputStream(new File(filePath))
            log("filePath:${filePath} className:${className}")
            findClickImpl(inputStream, className, dest)
            inputStream.close()
        }
    }

    void readClassWithJar(File input, File dest) {
        JarFile jarFile = new JarFile(input)
        Enumeration<JarEntry> entries = jarFile.entries()
        while (entries.hasMoreElements()) {
            JarEntry entry = entries.nextElement()
            String entryName = entry.getName()
            if (!entryName.endsWith(".class")) continue
            String className = entryName.substring(0, entryName.length() - ".class".length()).replaceAll("/", ".")
            log("entryName:${entryName} className:${className}")
            InputStream inputStream = jarFile.getInputStream(entry)

            findClickImpl(inputStream, className, dest)

            inputStream.close()
        }
        jarFile.close()
    }

    private boolean findClickImpl(InputStream inputStream, String className, File targetFile) {
        ClassReader reader = new ClassReader(inputStream)
        ClassNode node = new ClassNode()
        reader.accept(node, 1)
        String[] interfaces = reader.getInterfaces()

        if (interfaces == null || interfaces.length == 0) {
            return false
        }

        for (String interfaceName : interfaces) {
            if (TARGET_INTERFACE_NAME == interfaceName) {
                targetInfoMap.put(className, targetFile)
                return true
            }
        }

        return false
    }

    static String getClassName(String root, String classPath) {
        return classPath.substring(root.length() + "/".length(), classPath.length() - ".class".length()).replaceAll("/", ".")
    }
}