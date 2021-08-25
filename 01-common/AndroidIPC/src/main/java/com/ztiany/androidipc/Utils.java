package com.ztiany.androidipc;

import java.io.Closeable;
import java.io.IOException;

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2020-06-17 17:15
 */
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
