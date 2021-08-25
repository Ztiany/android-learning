package com.weishu.upf.dynamic_proxy_hook.app2;

import android.app.Application;
import android.content.Context;

import com.weishu.upf.dynamic_proxy_hook.app2.hook.HookHelper;

/**
 * @author Ztiany
 *         Email: 1169654504@qq.com
 *         Date : 2018-01-08 16:57
 */
public class AppContext extends Application {
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        try {
            HookHelper.attachContext();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
