package com.ztiany.fmod;

import android.app.Application;

/**
 * @author Ztiany
 *         Email: ztiany3@gmail.com
 *         Date : 2018-03-07 00:18
 */
public class App extends Application {


    static {

        try {
            // Try debug libraries...
            //System.loadLibrary("fmodD");
            //System.loadLibrary("fmodstudioD");

            // Try logging libraries...
            System.loadLibrary("fmodL");
            //System.loadLibrary("fmodstudioL");

            // Try release libraries...
            System.loadLibrary("fmod");
            //System.loadLibrary("fmodstudio");

            //System.loadLibrary("stlport_shared");

            System.loadLibrary("native-lib");
            //System.loadLibrary("example");
        } catch (UnsatisfiedLinkError e) {
            e.printStackTrace();
        }
    }

}
