package com.ztiany.androidaspectj02;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.ztiany.library.LibActivity;

public class MainActivity extends BaseActivity {

    @Override
    protected int layoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void setupView(@Nullable Bundle savedInstanceState) {
        findViewById(R.id.app_btn_open)
                .setOnClickListener(v -> startActivity(new Intent(this, LibActivity.class)));
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

}
