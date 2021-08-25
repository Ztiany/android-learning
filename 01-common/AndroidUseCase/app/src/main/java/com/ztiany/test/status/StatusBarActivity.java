package com.ztiany.test.status;

import android.os.Bundle;

import com.ztiany.test.R;
import com.ztiany.test.commom.SystemBarCompat;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


public class StatusBarActivity  extends AppCompatActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SystemBarCompat.setTranslucent(this, true, true);
        setContentView(R.layout.activity_status);

        findViewById(R.id.status_layout).setFitsSystemWindows(true);

    }
}
