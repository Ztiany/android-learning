package com.ztiany.mediaselector;

import android.app.Application;
import android.os.StrictMode;

/**
 * @author Ztiany
 *         Email: ztiany3@gmail.com
 *         Date : 2017-08-09 12:04
 */
public class AppContext extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
//        startStrictMode();
    }

    public final void startStrictMode() {

        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectAll()
                .penaltyLog()
                .build());

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectAll()
                .penaltyLog()
                .penaltyDeathOnNetwork()
                .build());
    }
}
