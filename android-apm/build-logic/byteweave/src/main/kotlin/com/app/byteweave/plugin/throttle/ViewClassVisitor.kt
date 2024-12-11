package com.app.byteweave.plugin.throttle

import com.app.byteweave.plugin.Descriptors
import com.app.byteweave.plugin.ViewThrottleConfiguration
import com.app.byteweave.plugin.filterLambda
import com.app.byteweave.plugin.hasAnnotation
import com.app.byteweave.plugin.isStatic
import com.app.byteweave.plugin.logE
import com.app.byteweave.plugin.nameWithDesc
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.Handle
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Type
import org.objectweb.asm.tree.ClassNode
import org.objectweb.asm.tree.InsnList
import org.objectweb.asm.tree.InsnNode
import org.objectweb.asm.tree.JumpInsnNode
import org.objectweb.asm.tree.LabelNode
import org.objectweb.asm.tree.MethodInsnNode
import org.objectweb.asm.tree.MethodNode
import org.objectweb.asm.tree.VarInsnNode

/**
 *  对 View 的点击事件进行节流处理。
 */
internal class ViewThrottleClassVisitor(
    private val config: ViewThrottleConfiguration,
    private val nextClassVisitor: ClassVisitor,
) : ClassNode(Opcodes.ASM5) {

    override fun visitEnd() {
        super.visitEnd()
        val methodToHook = mutableListOf<MethodNode>()
        findOutMethodToHook(methods, methodToHook)
        methodToHook.forEach {
            // 对方法进行节流处理
            throttleMethod(it)
        }
        accept(nextClassVisitor)
    }

    private fun findOutMethodToHook(methods: List<MethodNode>, methodToBeHooked: MutableList<MethodNode>) = methods.forEach { methodNode ->
        when {
            // 不处理静态方法
            methodNode.isStatic -> {
                // no-op
            }

            // 忽略带有 exclude 注解的方法
            methodNode.hasAnnotation(config.excludeAnnotationName) -> {
                // no-op
            }

            // 加上带有 include 有注解的方法
            methodNode.hasAnnotation(config.includeAnnotationName) -> {
                methodToBeHooked.add(methodNode)
            }

            // 使用了 butterknife.OnClick 注解的情况
            methodNode.hasAnnotation(Descriptors.BUTTER_KNIFE_ONCLICK) -> {
                methodToBeHooked.add(methodNode)
            }

            // 配置了 Hook Point 的情况
            methodNode.isHookPoint() -> {
                methodToBeHooked.add(methodNode)
            }
        }

        /*
        判断方法内部是否有需要处理的 lambda 表达式：

            InvokeDynamicInsnNode 的 name 和 desc 对应 LambdaMetafactory.metafactory 方法的前两个参数（除去 MethodHandles.Lookup caller）

               name 对应 name: The name of the method to implement. When used with invokedynamic, this is provided by the NameAndType of the
                               InvokeDynamic structure and is stacked automatically by the VM.
               desc 对应 inkMethodType: The expected signature of the CallSite. The parameter types represent the types of capture variables;
                                        the return type is the interface to implement. When used with invokedynamic, this is provided by the
                                        NameAndType of the InvokeDynamic structure and is stacked automatically by the VM. In the event that
                                        the implementation method is an instance method and this signature has any parameters, the first parameter
                                        in the invocation signature must correspond to the receiver.
         */
        val invokeDynamicNodes = methodNode.filterLambda {
            val nodeName = it.name
            val nodeDesc = it.desc
            /*
                 比如一个 Lambda 形式的 OnClickListener，它的：
                    name 为 onClick
                    desc 为 ()Landroid/view/View$OnClickListener; 或者 (参数类型)Landroid/view/View$OnClickListener; 总之 desc 描述的是生成接口实现的方法签名
                    如果 Lambda 没有引用外部变量，那么 desc 为 ()Landroid/view/View$OnClickListener; 如果有引用外部变量，那么 desc 为 (参数类型)Landroid/view/View$OnClickListener;
             */
            config.hookPoints.find { point ->
                point.methodName == nodeName && nodeDesc.endsWith(point.interfaceSignSuffix)
            } != null
        }

        invokeDynamicNodes.forEach {
            // bsm 即 BootstrapMethod，它是一个方法句柄，用于在运行时动态创建一个 CallSite 实例。
            // bsmArgs 即 BootstrapMethod 的参数，它是一个 Object 数组，用于传递给生成 CallSite 实例的参数。
            // bsmArgs 有三个元素，第二个参数的一个方法句柄，用于指向在编译器在外部类中生成的 Lambda 表达式的实现方法。
            // 根据 Lambda 生成的接口通过这个方法句柄来调用 Lambda 的实现方法。这个实现方法也就是我们需要处理的方法。
            (it.bsmArgs[1] as? Handle)?.let { handle ->
                val nameWithDesc = handle.name + handle.desc
                // 又在外部类中找编译器生成的 Lambda 表达式的实现方法
                methods.find { method ->
                    method.nameWithDesc == nameWithDesc
                }?.let(methodToBeHooked::add)
            }
        }

    }

    private fun MethodNode.isHookPoint(): Boolean {
        if (interfaces.isNullOrEmpty()) {
            return false
        }

        if (config.hookPoints.isEmpty()) {
            return false
        }
        val extraHookMethodList = config.hookPoints

        extraHookMethodList.forEach {
            if (interfaces.contains(it.className) && this.nameWithDesc == it.nameWithDesc) {
                return true
            }
        }
        return false
    }

    private fun throttleMethod(methodNode: MethodNode) {
        if (methodNode.instructions == null || methodNode.instructions.size() <= 0) {
            return
        }

        logE("Throttle method: ${methodNode.name}, ${methodNode.desc} of $name")

        // 获取方法的参数类型
        val argumentTypes = Type.getArgumentTypes(methodNode.desc)
        // 找到 android.view.View 参数的位置
        val viewIndex = argumentTypes.indexOfFirst { it.descriptor == Descriptors.VIEW }.takeIf {
            it >= 0
        } ?: return
        // 由于我们需要调用 Throttle 工具类的检测方法，需要将 View 作为第一个参数传递给 Throttle 工具类的检测方法，因此需要先将 View 加载到栈顶
        // 于是我们需要查找 View 在局部变量表中的位置，然后通过 ALOAD 指令将 View 加载到栈顶。
        val viewStackPosition = getVisitPosition(argumentTypes, viewIndex, methodNode.isStatic)

        val loadViewInsn = VarInsnNode(Opcodes.ALOAD, viewStackPosition)
        val invokeThrottleInsn = MethodInsnNode(
            Opcodes.INVOKESTATIC,
            config.checker.className,
            config.checker.methodName,
            "(Landroid/view/View;)Z"// 表示方法接收 View 参数，返回值为 boolean。
        )
        val checkLabelNode = LabelNode()
        val jumpInsn = JumpInsnNode(Opcodes.IFNE, checkLabelNode)
        val returnInsn = InsnNode(Opcodes.RETURN)

        // 用 InsnList 来存储我们需要插入的指令，这样可以按照顺序插入指令。不然还得倒序插入，因为 ASM 的指令插入是在当前指令的前面插入。
        val list = InsnList().apply {
            add(loadViewInsn)
            add(invokeThrottleInsn)
            add(jumpInsn)
            add(returnInsn)
            add(checkLabelNode)
        }
        methodNode.instructions.insert(list)
    }

    private fun getVisitPosition(
        argumentTypes: Array<Type>,
        parameterIndex: Int,
        isStaticMethod: Boolean,
    ): Int {
        if (parameterIndex < 0 || parameterIndex >= argumentTypes.size) {
            throw Error("getVisitPosition error")
        }
        return if (parameterIndex == 0) {
            if (isStaticMethod) {
                0
            } else {
                1
            }
        } else {
            getVisitPosition(
                argumentTypes,
                parameterIndex - 1,
                isStaticMethod
            ) + argumentTypes[parameterIndex - 1].size
        }
    }

}