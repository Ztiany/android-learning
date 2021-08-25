package com.ztiany.view.window;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewParent;
import android.widget.TextView;

import com.ztiany.view.R;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Window的真实尺寸始终是屏幕的大小，系统会有一个视图设置一个padding值，让开发者的视图在status_bar的下方
 */
public class RealWindowSizeActivity extends AppCompatActivity {

    private static final String TAG = RealWindowSizeActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.window_activity_main);

        getWindow().getDecorView().post(() -> {
            StringBuilder stringBuilder = new StringBuilder();
            printWindowSize(stringBuilder);
            printAllPadding(stringBuilder);
            TextView tv = findViewById(R.id.tv_size);
            tv.setText(stringBuilder.toString());
        });
    }

    private void printAllPadding(StringBuilder stringBuilder) {
        View button = findViewById(R.id.btn_show);
        ViewParent parent = button.getParent();
        while (parent != null) {
            Log.d(TAG, "parent:" + parent);
            if (parent instanceof View) {
                Log.d(TAG, "((View) parent).getPaddingTop():" + ((View) parent).getPaddingTop());
                stringBuilder.append(parent.toString()).append(" getPaddingTop = ").append(((View) parent).getPaddingTop());
                stringBuilder.append("\n");
                stringBuilder.append("\n");
            }
            parent = parent.getParent();
        }
    }

    private void printWindowSize(StringBuilder stringBuilder) {
        View decorView = getWindow().getDecorView();
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        Log.d(TAG, "displayMetrics.widthPixels:" + displayMetrics.widthPixels);
        Log.d(TAG, "displayMetrics.heightPixels:" + displayMetrics.heightPixels);
        Log.d(TAG, "decorView.getMeasuredWidth():" + decorView.getMeasuredWidth());
        Log.d(TAG, "decorView.getMeasuredHeight():" + decorView.getMeasuredHeight());

        stringBuilder.append("displayMetrics.widthPixels:").append(displayMetrics.widthPixels);
        stringBuilder.append("\n");
        stringBuilder.append("displayMetrics.heightPixels:").append(displayMetrics.heightPixels);
        stringBuilder.append("\n");
        stringBuilder.append("decorView.getMeasuredWidth():").append(decorView.getMeasuredWidth());
        stringBuilder.append("\n");
        stringBuilder.append("decorView.getMeasuredHeight():").append(decorView.getMeasuredHeight());
        stringBuilder.append("\n");
        stringBuilder.append("\n");

    }

    public void showDialog(View view) {
        new SizeDialog(this).show();
    }

}
