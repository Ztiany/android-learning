package com.dongnao.dnskin.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;
import android.util.Log;

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2019-11-01 01:55
 */
public class MyButton extends AppCompatButton {

    public MyButton(Context context) {
        super(context);
    }

    public MyButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private static final String TAG = "MyButton";

    @Override
    public void setTypeface(@Nullable Typeface tf) {
        super.setTypeface(tf);
        Log.d(TAG, "setTypeface() called with: tf = [" + tf + "]");
    }

}
