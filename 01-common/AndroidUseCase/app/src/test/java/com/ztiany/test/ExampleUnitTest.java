package com.ztiany.test;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    private BeanA mBeanA;
    private Activity mActivity;
    @Before
    public void init() {
        mBeanA = new BeanA();
        mBeanA.setName("Z");
        mBeanA.setPhone("110");

        mActivity = new Activity();
    }

    @Test
    public void test1() throws Exception {
        assertNotNull(mBeanA);
    }

    @Test
    public void test2() throws Exception {
        assertEquals("Z", mBeanA.getName());
    }

    @Test
    public void test3() throws Exception {
        System.out.println(Arrays.toString(mBeanA.getClass().getInterfaces()));
        System.out.println(BeanA.sClass);
        assertNotNull( BeanA.sClass);
    }

    @Test
    public void test4() throws Exception {
        assertNotEquals(BeanA.class, Context.class);
        mActivity.setIntent(new Intent());
    }

}