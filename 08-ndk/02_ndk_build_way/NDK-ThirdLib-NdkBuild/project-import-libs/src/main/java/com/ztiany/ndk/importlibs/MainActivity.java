package com.ztiany.ndk.importlibs;

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

    public void showJniText(View view) {
        String stringFromJNI = new JniUtils().stringFromJNI();
        Toast.makeText(this, stringFromJNI, Toast.LENGTH_SHORT).show();
    }
}
