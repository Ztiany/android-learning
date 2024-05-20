package com.ztiany.ndk.two_libs;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText mEtA;
    private EditText mEtB;
    private JniUtils mJniUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mEtA = (EditText) findViewById(R.id.two_libs_et_1);
        mEtB = (EditText) findViewById(R.id.two_libs_et_2);
        mJniUtils = new JniUtils();
    }

    public void doAdd(View view) {
        int a = Integer.valueOf(mEtA.getText().toString());
        int b = Integer.valueOf(mEtB.getText().toString());
        Toast.makeText(this, "a+b=" + mJniUtils.add(a, b), Toast.LENGTH_SHORT).show();
    }

}
