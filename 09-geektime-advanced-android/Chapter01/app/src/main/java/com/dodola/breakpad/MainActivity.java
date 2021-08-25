package com.dodola.breakpad;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.sample.breakpad.BreakpadInit;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.runtime.Permission;

import java.io.File;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestPermission();
    }

    private void requestPermission() {
        AndPermission.with(this)
                .runtime()
                .permission(Permission.Group.STORAGE)
                .onGranted(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> data) {
                        init();
                    }
                })
                .onDenied(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> data) {
                        supportFinishAfterTransition();
                    }
                })
                .start();
    }

    private void init() {
        iniJNI();

        findViewById(R.id.btn_make_crash).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeCrash();
            }
        });
    }

    private void iniJNI() {
        File crash = new File(getExternalCacheDir(), "crash");
        boolean mkdirs = crash.mkdirs();

        Log.d(TAG, "mkdirs = " + mkdirs);

        BreakpadInit.initBreakpad(crash.toString());

        System.loadLibrary("native-lib");
    }

    public native void makeCrash();

}