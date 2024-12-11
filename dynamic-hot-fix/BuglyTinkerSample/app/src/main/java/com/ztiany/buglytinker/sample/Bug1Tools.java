package com.ztiany.buglytinker.sample;

import android.content.Context;
import android.widget.Toast;

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2018-04-20 15:51
 */
public class Bug1Tools {

    final void doBug1(Context context) {
        int a = 8;
        int b = 8;
//        int b = 0;
        try {
            int c = a / b;
            Toast.makeText(context, "java c = " + c, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(context, "java c = " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
