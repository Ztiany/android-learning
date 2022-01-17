package com.ztiany.testlibrary;

import android.content.Context;
import android.widget.Toast;


public class ToastUtils {

    private ToastUtils() {

    }

    public static void showToast(Context context, CharSequence message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
