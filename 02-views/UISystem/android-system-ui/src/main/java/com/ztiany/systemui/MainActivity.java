package com.ztiany.systemui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.ztiany.systemui.cutout.CutoutActivity;
import com.ztiany.systemui.insets.WindowInsetsActivity;
import com.ztiany.systemui.ratio.RatioActivity;
import com.ztiany.systemui.uimods.SystemUIModes;
import com.ztiany.systemui.uisapmle.FullscreenActivity;
import com.ztiany.systemui.uisapmle.FullscreenActivity2;
import com.ztiany.systemui.uisapmle.SystemUIActivity;
import com.ztiany.systemui.uisapmle.SystemUIWithFragmentActivity;
import com.ztiany.systemui.uisapmle.VisibilityFullscreenActivity;
import com.ztiany.systemui.utils.Utils;

import timber.log.Timber;

/**
 * UI mode 示例
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Timber.plant(new Timber.DebugTree());
        Utils.printSize(this);
        Utils.printSystemInfo();
    }

    public void openSystemUI(View view) {
        startActivity(new Intent(this, SystemUIActivity.class));
    }

    @SuppressLint("NewApi")
    public void openSystemModes(View view) {
        startActivity(new Intent(this, SystemUIModes.class));
    }

    public void openVisibilityFullscreen(View view) {
        startActivity(new Intent(this, VisibilityFullscreenActivity.class));
    }

    public void openFullscreen(View view) {
        startActivity(new Intent(this, FullscreenActivity.class));
    }

    public void openFullscreen2(View view) {
        startActivity(new Intent(this, FullscreenActivity2.class));
    }

    public void openFullscreen3(View view) {
        startActivity(new Intent(this, SystemUIWithFragmentActivity.class));
    }

    public void openWindowInsets(View view) {
        startActivity(new Intent(this, WindowInsetsActivity.class));
    }

    public void openCutout(View view) {
        startActivity(new Intent(this, CutoutActivity.class));
    }

    public void openRatio(View view) {
        startActivity(new Intent(this, RatioActivity.class));
    }

}
