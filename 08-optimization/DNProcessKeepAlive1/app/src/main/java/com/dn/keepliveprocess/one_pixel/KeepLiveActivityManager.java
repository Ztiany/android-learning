package com.dn.keepliveprocess.one_pixel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import java.lang.ref.WeakReference;

public class KeepLiveActivityManager {

    private static KeepLiveActivityManager instance;
    private Context context;
    private WeakReference<Activity> activityInstance;

    public static KeepLiveActivityManager getInstance(Context context) {
        if (instance == null) {
            instance = new KeepLiveActivityManager(context.getApplicationContext());
        }
        return instance;
    }

    private KeepLiveActivityManager(Context context) {
        this.context = context;
    }

    void setKeepLiveActivity(Activity activity) {
        activityInstance = new WeakReference<>(activity);
    }

    public void startKeepLiveActivity() {
        Intent intent = new Intent(context, KeepLiveActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public void finishKeepLiveActivity() {
        if (activityInstance != null && activityInstance.get() != null) {
            Activity activity = activityInstance.get();
            activity.finish();
        }
    }

}
