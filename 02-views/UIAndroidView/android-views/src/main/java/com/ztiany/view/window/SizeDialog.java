package com.ztiany.view.window;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatDialog;

/**
 * @author Ztiany
 *         Email: ztiany3@gmail.com
 *         Date : 2018-01-22 23:25
 */
class SizeDialog extends AppCompatDialog {

    private static final String TAG = SizeDialog.class.getSimpleName();

    SizeDialog(Context context) {
        super(context);
        TextView textView = new TextView(context);
        textView.setPadding(20, 20, 20, 20);
        textView.setTextSize(20);
        textView.setTextColor(Color.RED);
        textView.setText("我的对话框");
        setContentView(textView);
    }

    @Override
    public void show() {
        super.show();
        Window window = getWindow();
        if (window != null) {
            View decorView = window.getDecorView();
            decorView.post(new Runnable() {
                @Override
                public void run() {
                    printWindowSize();
                }
            });
        }
    }

    private void printWindowSize() {
        Window window = getWindow();
        assert window != null;
        View decorView = window.getDecorView();
        Log.d(TAG, "decorView.getMeasuredWidth():" + decorView.getMeasuredWidth());
        Log.d(TAG, "decorView.getMeasuredHeight():" + decorView.getMeasuredHeight());
    }

}
