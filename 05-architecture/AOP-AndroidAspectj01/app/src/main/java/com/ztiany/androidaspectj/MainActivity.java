package com.ztiany.androidaspectj;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.squareup.leakcanary.LeakCanary;
import com.ztiany.androidaspectj.aspect.DebugTools;
import com.ztiany.mylibrary.Main2Activity;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        LeakCanary.install(getApplication());
        getWindow().getDecorView().findViewById(android.R.id.content).setOnClickListener(v -> Log.d(TAG, "onClick() called with: v = [" + v + "]"));

    }


    public void testAopAround(View view) {
        Toast.makeText(this, "testAopAround", Toast.LENGTH_SHORT).show();
    }

    @DebugTools
    public void testAopPointcuts(View view) {
        Toast.makeText(this, "testAopPointcuts", Toast.LENGTH_SHORT).show();
    }

    public void testCall(View view) {
        testAopCall(view);
    }

    public void testAopCall(View view) {
        Toast.makeText(this, "testAopCall", Toast.LENGTH_SHORT).show();
    }

    public void testAopWithCode(View view) {
        testAopWithCode1(view);
        testAopWithCode2(view);
    }

    private void testAopWithCode1(View view) {
        withCodeRun(view);
    }

    private void testAopWithCode2(View view) {
        withCodeRun(view);
    }

    private void withCodeRun(View view) {
        Toast.makeText(this, "withCodeRun", Toast.LENGTH_SHORT).show();
    }


    public void testErrorCatch1(View view) {
        error1();
    }

    private void error1() {
        int a = 1 / 0;
    }

    public void testErrorCatch2(View view) {
        error2();
    }

    //此时无法切入代码
    private void error2() {
        try {
            String a = null;
            a.charAt(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void open2(View view) {
        startActivity(new Intent(this, Main2Activity.class));

    }
}

