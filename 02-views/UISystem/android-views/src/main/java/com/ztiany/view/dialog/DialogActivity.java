package com.ztiany.view.dialog;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.ztiany.view.R;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


/**
 * 查看下面属性的区别
 * <pre>
 *      <item name="android:background">@color/colorPrimary</item>
 *      <item name="android:windowBackground">@color/colorAccent</item>
 * </pre>
 *
 * @author Ztiany
 *         Email: ztiany3@gmail.com
 *         Date : 2017-07-24 23:28
 */
public class DialogActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialogs_activity_dialog);

        WindowManager.LayoutParams attributes = getWindow().getAttributes();
        attributes.gravity = Gravity.BOTTOM;
        attributes.width = WindowManager.LayoutParams.MATCH_PARENT;
        getWindow().setAttributes(attributes);

        getWindow().getDecorView().postDelayed(new Runnable() {
            @Override
            public void run() {
                View parent = findViewById(R.id.dialog_view);
                while (parent != null) {
                    Drawable background = parent.getBackground();
                    Log.d("DialogActivity", parent + "---" + background);
                    if (background instanceof ColorDrawable) {
                        Log.d("DialogActivity", parent + "---" + Integer.toHexString(((ColorDrawable) background).getColor()));
                    }
                    if (parent.getParent() != null && parent.getParent() instanceof ViewGroup) {
                        parent = (ViewGroup) parent.getParent();
                    } else {
                        parent = null;
                    }
                }
            }
        }, 2000);
    }


}
