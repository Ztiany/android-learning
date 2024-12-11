package com.enjoy.fix.patch;

import org.junit.Test;

import java.lang.reflect.Field;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    class A{
        String a;
    }
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);

        Field[] declaredFields = A.class.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            System.out.println(declaredField.getDeclaringClass().getCanonicalName());
        }
    }
}