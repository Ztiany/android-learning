package com.app.byteweave.plugin.image

import com.app.byteweave.plugin.Classes
import com.app.byteweave.plugin.Instructions
import com.app.byteweave.plugin.LegalImageConfiguration
import com.app.byteweave.plugin.logE
import com.app.byteweave.plugin.simpleClassName
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.ClassNode
import org.objectweb.asm.tree.MethodInsnNode
import org.objectweb.asm.tree.MethodNode
import org.objectweb.asm.tree.TypeInsnNode

/**
 *  替换父类：比如将继承自 ImageView 的 AppCompatImageView 替换为带有安全检查的特定 ImageView 子类。
 */
internal class LegalImageClassVisitor(
    config: LegalImageConfiguration,
    private val nextClassVisitor: ClassVisitor,
) : ClassNode(Opcodes.ASM5) {

    private val replacements = config.replacements

    override fun visit(
        version: Int,
        access: Int,
        name: String,
        signature: String?,
        superName: String,
        interfaces: Array<out String>?,
    ) {
        replacements[superName]?.takeIf {
            name != it
        }?.let {
            logE("LegalImageClassVisitor: $name extends $superName, replace with $it")
            super.visit(version, access, name, signature, it, interfaces)
        } ?: super.visit(version, access, name, signature, superName, interfaces)
    }

    override fun visitEnd() {
        super.visitEnd()
        accept(nextClassVisitor)
    }

}

/**
 * 将所有直接 new ImageView 的地方替换为新的 ImageView 子类。
 */
internal class NewImageViewReplacementVisitor(
    config: LegalImageConfiguration,
    private val nextClassVisitor: ClassVisitor,
) : ClassNode(Opcodes.ASM5) {

    private val targetView = config.replacements[Classes.IMAGE_VIEW]

    override fun visitEnd() {
        super.visitEnd()
        if (targetView == null) {
            accept(nextClassVisitor)
            return
        }

        methods.forEach { methodNode ->
            val instructions = methodNode.instructions
            instructions.forEach { instruction ->
                if (instruction.opcode == Opcodes.NEW) {
                    (instruction as? TypeInsnNode)?.let {
                        if (it.desc == Classes.IMAGE_VIEW) {
                            transformNewImageView(methodNode, instruction)
                        }
                    }
                }
            }
        }
        accept(nextClassVisitor)
    }

    private fun transformNewImageView(methodNode: MethodNode, newInsnNode: TypeInsnNode /*new ImageView 的指令*/) {
        val instructions = methodNode.instructions
        val typeInsnNodeIndex = instructions.indexOf(newInsnNode)
        //从 newInsnNode 指令开始遍历，找到调用 ImageView 构造函数的指令，然后对其进行替换。
        for (index in typeInsnNodeIndex + 1 until instructions.size()) {
            val targetNode = instructions[index]
            if (targetNode is MethodInsnNode && targetNode.isImageViewInitMethod()) {
                //将 ImageView 替换为 targetView
                newInsnNode.desc = targetView
                targetNode.owner = targetView
                logE("Replacing [new ${Classes.IMAGE_VIEW}] with [new $targetView] in ${simpleClassName}.${methodNode.name}")
                break
            }
        }
    }

    private fun MethodInsnNode.isImageViewInitMethod(): Boolean {
        return this.owner == Classes.IMAGE_VIEW && this.name == Instructions.NEW
    }

}