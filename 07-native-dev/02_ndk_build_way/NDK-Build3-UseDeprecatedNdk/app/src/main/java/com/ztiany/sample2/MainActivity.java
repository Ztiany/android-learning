package com.ztiany.sample2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void invokeC(View view) {
        JniUtils jniUtils = new JniUtils();
        Toast.makeText(this, jniUtils.getMessage(), Toast.LENGTH_SHORT).show();
    }
}
