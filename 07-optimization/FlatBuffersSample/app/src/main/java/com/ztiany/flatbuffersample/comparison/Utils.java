package com.ztiany.flatbuffersample.comparison;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;

public class Utils {

    public static byte[] readRawResource(Context context, String name) {
        InputStream stream = null;
        byte[] buffer = null;
        try {
            stream = context.getAssets().open(name);
            buffer = new byte[stream.available()];
            while (stream.read(buffer) != -1) ;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return buffer;
    }
}
