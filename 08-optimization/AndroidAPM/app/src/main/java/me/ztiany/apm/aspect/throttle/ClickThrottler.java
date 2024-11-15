package me.ztiany.apm.aspect.throttle;


import android.view.View;

import me.ztiany.apm.R;
import timber.log.Timber;

public class ClickThrottler {

    private static final long DEFAULT_THROTTLE_TIME = 500;

    private static long sThrottleTime = DEFAULT_THROTTLE_TIME;

    public static void setThrottleTime(long throttleTime) {
        sThrottleTime = throttleTime;
    }

    public static boolean check(View view) {
        long currentTimeMillis = System.currentTimeMillis();
        long lastClickTimeStamp = view.getTag(R.id.id_view_throttle) == null ? 0 : (long) view.getTag(R.id.id_view_throttle);
        long offset = currentTimeMillis - lastClickTimeStamp;
        boolean pass = offset > sThrottleTime;
        view.setTag(R.id.id_view_throttle, currentTimeMillis);
        Timber.d("check for %s. offset = %d pass %b", view, offset, pass);
        return pass;
    }


}
