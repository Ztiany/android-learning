package com.ztiany.clfix.demo;


import android.content.Context;
import android.widget.Toast;

class Bug {

    void testFix(Context context) {
        int i = 10;
        int a = 0;
        Toast.makeText(context, "shit:" + i / a, Toast.LENGTH_SHORT).show();
    }

}
