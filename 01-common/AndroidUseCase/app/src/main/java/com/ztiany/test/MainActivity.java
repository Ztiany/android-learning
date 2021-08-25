package com.ztiany.test;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.ztiany.test.animation.AnimActivity;
import com.ztiany.test.anr.ANRActivity;
import com.ztiany.test.deviceid.DeviceIDActivity;
import com.ztiany.test.fragments.FragmentsActivity;
import com.ztiany.test.fragments.ViewPagerActivity;
import com.ztiany.test.intent.IntentAActivity;
import com.ztiany.test.status.StatusBarActivity;
import com.ztiany.test.time.TimeActivity;
import com.ztiany.test.tint.TintActivity;

public class MainActivity extends AbstractActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void recreate(View view) {
        recreate();
    }

    public void opActivityAnim(View view) {
        startActivity(new Intent(this, AnimActivity.class));
    }

    public void openTint(View view) {
        startActivity(new Intent(this, TintActivity.class));
    }

    public void openTime(View view) {
        startActivity(new Intent(this, TimeActivity.class));
    }

    public void openStatus(View view) {
        startActivity(new Intent(this, StatusBarActivity.class));
    }

    public void fragments(View view) {
        startActivity(new Intent(this, FragmentsActivity.class));
    }

    public void fragmentsViewPager(View view) {
        startActivity(new Intent(this, ViewPagerActivity.class));
    }

    public void makeANR(View view) {
        startActivity(new Intent(this, ANRActivity.class));
    }

    public void openIntentTest(View view) {
        startActivity(new Intent(this, IntentAActivity.class));
    }

    public void openDeviceActivity(View view) {
        startActivity(new Intent(this, DeviceIDActivity.class));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult() called with: requestCode = [" + requestCode + "], resultCode = [" + resultCode + "], data = [" + data + "]");
    }

}