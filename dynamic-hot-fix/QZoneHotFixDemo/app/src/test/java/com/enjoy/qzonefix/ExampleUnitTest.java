package com.enjoy.qzonefix;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

//    Class.forName除了将类的.class文件加载到jvm中之外，还会对类进行解释，执行类中的static块。
//
//    而classloader只干一件事情，就是将.class文件加载到jvm中，不会执行static中的内容
    @Test
    public void addition_isCorrect() throws IOException, ClassNotFoundException {

        assertEquals(4, 2 + 2);
    }
}