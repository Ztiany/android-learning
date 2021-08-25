package com.smartdengg.compile;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import static com.smartdengg.compile.Utils.addDebouncedAnno;
import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.IFNE;
import static org.objectweb.asm.Opcodes.INVOKESTATIC;
import static org.objectweb.asm.Opcodes.RETURN;

/**
 * 创建时间:  2018/03/09 19:48 <br>
 * 作者:  SmartDengg <br>
 * 描述:
 */
class ListView$OnItemClickListenerMethodAdapter extends MethodVisitor {

    ListView$OnItemClickListenerMethodAdapter(MethodVisitor methodVisitor) {
        super(Opcodes.ASM6, methodVisitor);
    }

    @Override
    public void visitCode() {
        super.visitCode();
        //加上注解
        addDebouncedAnno(mv);

        //添加防抖代码
        mv.visitVarInsn(ALOAD, 2);
        //调用防抖方法实现 shouldDoClick，这回产生一个结果
        mv.visitMethodInsn(INVOKESTATIC,
                "com/smartdengg/clickdebounce/DebouncedPredictor",
                "shouldDoClick",
                "(Landroid/view/View;)Z",
                false);

        //构建一个代码块{}
        Label label = new Label();
        //语句 if(!结果)
        mv.visitJumpInsn(IFNE, label);
        //如果不满足条件就 return
        mv.visitInsn(RETURN);
        mv.visitLabel(label);
    }

}
