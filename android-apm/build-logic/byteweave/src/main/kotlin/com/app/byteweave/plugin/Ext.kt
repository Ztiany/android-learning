package com.app.byteweave.plugin

import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.ClassNode
import org.objectweb.asm.tree.InvokeDynamicInsnNode
import org.objectweb.asm.tree.MethodNode

internal val String.internalName: String
    get() = replace('.', '/')

internal val String.descriptor: String
    get() = "L${internalName};"

internal val ClassNode.simpleClassName: String
    get() = name.substringAfterLast('/')

internal val MethodNode.isStatic: Boolean
    get() = access and Opcodes.ACC_STATIC != 0

internal val MethodNode.nameWithDesc: String
    get() = name + desc

internal fun MethodNode.hasAnnotation(annotationDesc: String): Boolean {
    return visibleAnnotations?.find { it.desc == annotationDesc } != null
}

internal fun MethodNode.filterLambda(filter: (InvokeDynamicInsnNode) -> Boolean): List<InvokeDynamicInsnNode> {
    instructions ?: return emptyList()
    val dynamicList = mutableListOf<InvokeDynamicInsnNode>()
    instructions.forEach { instruction ->
        if (instruction is InvokeDynamicInsnNode) {
            if (filter(instruction)) {
                dynamicList.add(instruction)
            }
        }
    }
    return dynamicList
}

internal fun logD(message: String) {
    println("【===ByteWeave】===: $message")
}

internal fun logE(message: String) {
    System.err.println("【===ByteWeave】===: $message")
}