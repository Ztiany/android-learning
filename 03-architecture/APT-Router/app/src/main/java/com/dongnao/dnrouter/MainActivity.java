package com.dongnao.dnrouter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.dongnao.base.TestService;
import com.dongnao.dnrouter.parcelable.TestParcelable;
import com.dongnao.router.core.DNRouter;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            DNRouter.init(getApplication());
        } catch (Exception e) {
            e.printStackTrace();
        }

        /**
         * 组件服务共享 通信
         */
        TestService testService = (TestService) DNRouter.getInstance().build("/main/service1")
                .navigation();
        testService.test();

        TestService testService1 = (TestService) DNRouter.getInstance().build("/main/service2")
                .navigation();
        testService1.test();

        TestService testService2 = (TestService) DNRouter.getInstance().build("/module1/service")
                .navigation();
        testService2.test();

        TestService testService3 = (TestService) DNRouter.getInstance().build("/module2/service")
                .navigation();
        testService3.test();
    }


    public void innerJump(View view) {
        ArrayList<Integer> integers = new ArrayList<Integer>();
        integers.add(1);
        integers.add(2);

        ArrayList<String> strings = new ArrayList<String>();
        strings.add("1");
        strings.add("2");

        ArrayList<TestParcelable> ps = new ArrayList<TestParcelable>();

        TestParcelable testParcelable = new TestParcelable(1, "a");
        TestParcelable testParcelable2 = new TestParcelable(2, "d");
        ps.add(testParcelable);
        ps.add(testParcelable2);

        DNRouter.getInstance().build("/main/test").withString("a",
                "从MainActivity").withInt("b", 1).withShort("c", (short) 2).withLong("d", 3)
                .withFloat("e", 1.0f).withDouble("f", 1.1).withByte("g", (byte) 1).withBoolean
                ("h", true).withChar("i", '好').withParcelable("j", testParcelable)
                .withStringArray("aa",
                        new String[]{"1", "2"}).withIntArray("bb", new int[]{1, 2}).withShortArray
                ("cc", new short[]{(short) 2, (short) 2}).withLongArray("dd", new long[]{1, 2})
                .withFloatArray("ee", new float[]{1.0f, 1.0f}).withDoubleArray("ff", new
                double[]{1.1, 1.1}).withByteArray("gg",
                new byte[]{(byte) 1, (byte) 1}).withBooleanArray
                ("hh", new boolean[]{true, true}).withCharArray("ii", new char[]{'好', '好'})
                .withParcelableArray("jj", new TestParcelable[]{testParcelable, testParcelable2})
                .withParcelableArrayList("k1", ps).withParcelableArrayList("k2", ps)
                .withStringArrayList("k3", strings).withIntegerArrayList("k4", integers)
                .withInt("hhhhhh", 1)
                .navigation(this, 100);
    }

    public void module1Jump(View view) {
        DNRouter.getInstance().build("/module1/test").withString("msg",
                "从MainActivity").navigation();
    }

    public void module2Jump(View view) {
        DNRouter.getInstance().build("/module2/test").withString("msg",
                "从MainActivity").navigation();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("Main", requestCode + ":" + resultCode + ":" + data);
    }
}
