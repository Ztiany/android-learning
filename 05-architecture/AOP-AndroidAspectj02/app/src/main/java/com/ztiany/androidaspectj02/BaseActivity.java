package com.ztiany.androidaspectj02;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * <pre>
 *          1，封装一般流程
 *          2，onBackPressed事件分发，优先交给Fragment处理。
 *  </pre>
 *
 * @author Ztiany
 * Date : 2016-05-04 15:40
 * Email: 1169654504@qq.com
 */
public abstract class BaseActivity extends AppCompatActivity {

    ///////////////////////////////////////////////////////////////////////////
    // Lifecycle
    ///////////////////////////////////////////////////////////////////////////

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        initialize(savedInstanceState);
        super.onCreate(savedInstanceState);
        int layoutId = layoutId();
        setContentView(layoutId);
        setupView(savedInstanceState);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    /**
     * Before call super.onCreate and setContentView
     *
     * @param savedInstanceState state
     */
    protected void initialize(@Nullable Bundle savedInstanceState) {
    }

    /**
     * provide a layoutId
     *
     * @return layoutId
     */
    protected abstract int layoutId();


    /**
     * after setContentView
     */
    protected abstract void setupView(@Nullable Bundle savedInstanceState);

    public <T extends View> T findView(@IdRes int viewId) {
        return findViewById(viewId);
    }

}
