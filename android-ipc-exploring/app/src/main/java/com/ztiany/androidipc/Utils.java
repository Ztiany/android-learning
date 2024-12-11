package com.ztiany.androidipc;

import java.io.Closeable;
import java.io.IOException;

public class Utils {

    public static void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
