package com.maniu.notchscreenmaniu.screen;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.maniu.notchscreenmaniu.R;

public class BaseActivity  extends AppCompatActivity {
    /**
     * 主内容区
     */
    private FrameLayout mContentContainer;
    /**
     * 刘海容器
     */
    private FrameLayout mNotchContainer;

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(R.layout.activity_base);
        mNotchContainer = findViewById(R.id.notch_container);
        mNotchContainer.setTag("notch_container");
        mContentContainer = findViewById(R.id.content_container);
        LayoutInflater.from(this).inflate(layoutResID, mContentContainer, true);
    }
}
