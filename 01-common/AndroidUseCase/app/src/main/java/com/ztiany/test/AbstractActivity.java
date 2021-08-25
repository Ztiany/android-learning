package com.ztiany.test;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * <pre>
 *       Activity生命周期执行顺序：
 *                  onCrate,
 *                  onStart,
 *                  onRestoreInstanceState(maybe),
 *                  onPostCreate,
 *                  onResume,
 *                  onPause,
 *                  onSaveInstanceState,
 *                  onStop(在内存不足而导致系统无法保留此进程的情况下onStop可能都不会被执行)。
 *                  onDestroy.
 *  </pre>
 *
 * @author Ztiany
 *         Date : 2016-03-18 15:22
 *         Email: ztiany3@gmail.com
 */
abstract class AbstractActivity extends AppCompatActivity {

    public AbstractActivity() {
        Log.d(tag(),"---->     new Instance    ");
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        Log.d(tag(),"---->onCreate before call super    ");

        super.onCreate(savedInstanceState);

        Log.d(tag(),"---->onCreate after call super  " + "bundle = " + savedInstanceState);

    }

    @Override
    protected void onRestart() {

        Log.d(tag(),"---->onRestart before call super    ");

        super.onRestart();

        Log.d(tag(),"---->onRestart after call super  ");

    }

    @Override
    protected void onStart() {

        Log.d(tag(),"---->onStart before call super    ");

        super.onStart();

        Log.d(tag(),"---->onStart after call super  ");

    }

    @Override
    protected void onResume() {

        Log.d(tag(),"---->onResume before call super    ");

        super.onResume();

        Log.d(tag(),"---->onResume after call super  ");

    }

    @Override
    protected void onPause() {

        Log.d(tag(),"---->onPause before call super    ");

        super.onPause();

        Log.d(tag(),"---->onPause after call super  ");

    }

    @Override
    protected void onStop() {

        Log.d(tag(),"---->onStop before call super    ");

        super.onStop();

        Log.d(tag(),"---->onStop after call super  ");

    }

    @Override
    protected void onDestroy() {

        Log.d(tag(),"---->onDestroy before call super    ");

        super.onDestroy();

        Log.d(tag(),"---->onDestroy after call super  ");

    }

    private String tag() {
        return this.getClass().getSimpleName();
    }

}