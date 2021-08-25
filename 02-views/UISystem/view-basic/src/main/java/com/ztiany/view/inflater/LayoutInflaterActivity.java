package com.ztiany.view.inflater;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.ztiany.view.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.view.LayoutInflaterCompat;

/**
 * 参考：http://blog.csdn.net/lmj623565791/article/details/51503977
 */
public class LayoutInflaterActivity extends AppCompatActivity {

    private static final String TAG = LayoutInflaterActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        LayoutInflaterCompat.setFactory(LayoutInflater.from(this), (parent, name, context, attrs) -> {
            //你可以在这里直接new自定义View
            //你可以在这里将系统类替换为自定义View
            //appcompat 创建view代码
            AppCompatDelegate delegate = getDelegate();
            View view = delegate.createView(parent, name, context, attrs);
            Log.d(TAG, "view:" + view);
            return view;
        });
        super.onCreate(savedInstanceState);

        setContentView(R.layout.inflater_activity_main);
    }

}
