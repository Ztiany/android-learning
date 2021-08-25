package com.ztiany.androidaspectj03;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.ztiany.androidaspectj03.sample2.BaseTarget1;
import com.ztiany.androidaspectj03.sample2.BaseTarget2;
import com.ztiany.androidaspectj03.sample2.BaseTarget3;
import com.ztiany.androidaspectj03.sample3.TraceTarget1;
import com.ztiany.androidaspectj03.sample3.TraceTarget2;
import com.ztiany.androidaspectj03.sample4.CflowTarget;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setListener();
    }

    private void setListener() {
        findViewById(R.id.btn_sample_1).setOnClickListener(mOnClickListener);
        findViewById(R.id.btn_sample_2).setOnClickListener(mOnClickListener);
        findViewById(R.id.btn_sample_3).setOnClickListener(mOnClickListener);
        findViewById(R.id.btn_sample_4).setOnClickListener(mOnClickListener);
        findViewById(R.id.btn_sample_4).setOnClickListener(mOnClickListener);
        findViewById(R.id.btn_sample_5).setOnClickListener(mOnClickListener);
        findViewById(R.id.btn_sample_6).setOnClickListener(mOnClickListener);
        findViewById(R.id.btn_sample_7).setOnClickListener(mOnClickListener);
        findViewById(R.id.btn_sample_8).setOnClickListener(mOnClickListener);
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_sample_1:
                    new BaseTarget1().testAround();
                    break;
                case R.id.btn_sample_2:
                    new BaseTarget2().throwError();
                    break;
                case R.id.btn_sample_3:
                    new BaseTarget2().print1();
                    new BaseTarget2().print2();
                    break;
                case R.id.btn_sample_4:
                    new BaseTarget1().withInSample();
                    new BaseTarget2().withInSample();
                    break;
                case R.id.btn_sample_5:
                    new BaseTarget2().diffCallAndExecution();
                    break;
                case R.id.btn_sample_6:
                    BaseTarget3 baseTarget3 = new BaseTarget3();
                    baseTarget3.age = 18;
                    baseTarget3.name = "Ztiany";
                    baseTarget3.birthday = "1990-11-21";
                    baseTarget3.address = "深圳";
                    Log.d(TAG, String.format("baseTarget3.age:%d、name:%s、birthday:%s、address:%s", baseTarget3.age, baseTarget3.name, baseTarget3.birthday, baseTarget3.address));
                    break;
                case R.id.btn_sample_7:
                    new TraceTarget1().run();
                    new TraceTarget2().run();
                    break;
                case R.id.btn_sample_8:
                    new CflowTarget().testCflow();
                    break;
            }
        }
    };
}
