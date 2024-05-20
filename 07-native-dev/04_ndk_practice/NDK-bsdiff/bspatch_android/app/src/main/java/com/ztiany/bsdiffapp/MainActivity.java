package com.ztiany.bsdiffapp;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.ztiany.bsdiffapp.utils.AppUpdater;

public class MainActivity extends AppCompatActivity {

    private AppUpdater mAppUpdater;


    static {
        System.loadLibrary("native-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        View btn = findViewById(R.id.btn_update);
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            int versionCode = packageInfo.versionCode;
            if (versionCode != 1) {
                btn.setVisibility(View.GONE);
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        mAppUpdater = new AppUpdater(this);
    }

    public void checkVersion(View view) {
        mAppUpdater.doUpdate();
    }


}
