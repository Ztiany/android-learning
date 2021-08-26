package com.smartdengg.compile;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

/**
 * 创建时间:  2018/03/27 14:49 <br>
 * 作者:  SmartDengg <br>
 * 描述: 还要注意，为了自动计算帧，有时需要计算两个给定类的公共超类。默认情况下，ClassWriter类会在getCommonSuperClass方法中进行这一计算，它会将两个类加载到JVM中，并使用反射API。如果我们正在生成几个相互引用的类，那可能会导致问题，因为被引用的类可能尚未存在。在这种情况下，可以重写getCommonSuperClass方法来解决这一问题。
 */
public class CompactClassWriter extends ClassWriter {

    public CompactClassWriter(ClassReader classReader, int flags) {
        super(classReader, flags);
    }

    @Override
    protected String getCommonSuperClass(final String type1, final String type2) {
        Class<?> c, d;
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        try {
            c = Class.forName(type1.replace('/', '.'), true, classLoader);
            d = Class.forName(type2.replace('/', '.'), true, classLoader);
        } catch (Exception e) {
            return "java/lang/Object";
        }
        if (c.isAssignableFrom(d)) {
            return type1;
        }
        if (d.isAssignableFrom(c)) {
            return type2;
        }
        if (c.isInterface() || d.isInterface()) {
            return "java/lang/Object";
        } else {
            do {
                c = c.getSuperclass();
            } while (!c.isAssignableFrom(d));
            return c.getName().replace('.', '/');
        }
    }

}