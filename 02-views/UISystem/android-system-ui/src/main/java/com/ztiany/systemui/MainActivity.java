package com.ztiany.systemui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.ztiany.systemui.usecase.cutout.CutoutActivity;
import com.ztiany.systemui.usecase.edge2edge.Edge2EdgeNewActivity;
import com.ztiany.systemui.usecase.edge2edge.Edge2EdgeOldActivity;
import com.ztiany.systemui.usecase.insets.WindowInsetsActivity;
import com.ztiany.systemui.usecase.sadaptation.MaxAspectRatioActivity;
import com.ztiany.systemui.usecase.uimods.SystemUIModes;
import com.ztiany.systemui.usecase.uisapmle.FullscreenActivity;
import com.ztiany.systemui.usecase.uisapmle.FullscreenActivity2;
import com.ztiany.systemui.usecase.uisapmle.SystemUIActivity;
import com.ztiany.systemui.usecase.uisapmle.SystemUIWithFragmentActivity;
import com.ztiany.systemui.usecase.uisapmle.VisibilityFullscreenActivity;
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
        startActivity(new Intent(this, MaxAspectRatioActivity.class));
    }

    public void openEdge2EdgeNew(View view) {
        startActivity(new Intent(this, Edge2EdgeNewActivity.class));
    }

    public void openEdge2EdgeOld(View view) {
        startActivity(new Intent(this, Edge2EdgeOldActivity.class));
    }

}
