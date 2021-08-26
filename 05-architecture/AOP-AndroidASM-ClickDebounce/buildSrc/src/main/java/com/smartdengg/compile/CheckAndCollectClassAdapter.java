package com.smartdengg.compile;


import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

/**
 * 创建时间:  2019/01/04 15:43 <br>
 * 作者:  SmartDengg <br>
 * 描述: 用于收集一个类中需要编织的方法
 */
public class CheckAndCollectClassAdapter extends ClassVisitor implements Opcodes {

    private String className;
    private Map<String, List<MethodDescriptor>> unWeavedClassMap = new HashMap<>();
    private Map<String, List<String>> exclusion;
    private List<String> exclusionMethods;

    public CheckAndCollectClassAdapter(Map<String, List<String>> exclusion) {
        super(Opcodes.ASM6);
        this.exclusion = exclusion;
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces);
        this.className = name;
        this.exclusionMethods = exclusion.get(name);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        /*收集需要编织的方法*/
        if (exclusionMethods == null || !exclusionMethods.contains(name + desc)) {
            if (Utils.isViewOnclickMethod(access, name, desc) || Utils.isListViewOnItemOnclickMethod(access, name, desc)) {
                return new MethodNodeAdapter(api, access, name, desc, signature, exceptions, className, unWeavedClassMap);
            }
        }
        return null;
    }

    public Map<String, List<MethodDescriptor>> getUnWeavedClassMap() {
        return unWeavedClassMap;
    }

    static class MethodNodeAdapter extends MethodNode {

        private String className;

        private Map<String, List<MethodDescriptor>> classNameToMethodDescriptorMap;

        /**
         * 受否标注了 Debounced 注解
         */
        private boolean hasDebouncedAnnotation;

        MethodNodeAdapter(
                int api, int access, String name, String desc, String signature, String[] exceptions, String className,
                Map<String, List<MethodDescriptor>> classNameToMethodDescriptorMap
        ) {
            super(api, access, name, desc, signature, exceptions);
            this.className = className;
            this.classNameToMethodDescriptorMap = classNameToMethodDescriptorMap;
            System.out.println("CheckAndCollectClassAdapter-->className = " + className);
            System.out.println("CheckAndCollectClassAdapter-->classNameToMethodDescriptorMap = " + classNameToMethodDescriptorMap);
        }

        @Override
        public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
            /*Lcom/smartdengg/clickdebounce/Debounced;*/
            hasDebouncedAnnotation |= desc.equals("Lcom/smartdengg/clickdebounce/Debounced;");
            return null;
        }

        @Override
        public void visitEnd() {
            /*如果加了注解，无论如何都编织*/
            if (hasDebouncedAnnotation) {
                return;
            }
            /*该方法是如果有调用其他方法的操作，才需要进行编织*/
            if (hasInvokeOperation()) {
                System.out.println("CheckAndCollectClassAdapter-->hasInvokeOperation: " + className + "-->" + name);
                List<MethodDescriptor> methodDescriptors = classNameToMethodDescriptorMap.getOrDefault(className, new ArrayList<>());
                methodDescriptors.add(new MethodDescriptor(access, name, desc));
                classNameToMethodDescriptorMap.put(className, methodDescriptors);
            } else {
                System.err.println("CheckAndCollectClassAdapter-->no hasInvokeOperation: " + className + "-->" + name);
            }
        }

        /**
         * 该方法是否有调用其他方法的操作
         */
        private boolean hasInvokeOperation() {
            for (ListIterator<AbstractInsnNode> iterator = instructions.iterator(); iterator.hasNext(); ) {
                AbstractInsnNode node = iterator.next();
                int opcode = node.getOpcode();
                if (opcode == -1) {
                    continue;
                }
                if (opcode >= INVOKEVIRTUAL && opcode <= INVOKEDYNAMIC) {
                    return true;
                }
            }
            return false;
        }

    }

}
