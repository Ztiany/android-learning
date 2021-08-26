package com.dn.keepliveprocess.one_pixel;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager.LayoutParams;

public class KeepLiveActivity extends Activity {


    private static final String TAG = KeepLiveActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "KeepLiveActivity----onCreate!!!");
        Window window = getWindow();
        window.setGravity(Gravity.START | Gravity.TOP);
        LayoutParams params = window.getAttributes();
        params.height = 1;
        params.width = 1;
        params.x = 0;
        params.y = 0;
        window.setAttributes(params);
        KeepLiveActivityManager.getInstance(this).setKeepLiveActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "KeepLiveActivity----onDestroy!!!");
    }

}
