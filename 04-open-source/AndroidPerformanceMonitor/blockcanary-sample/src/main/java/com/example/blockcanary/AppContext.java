/*
 * Copyright (C) 2016 MarkZhai (http://zhaiyifan.cn).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.blockcanary;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import com.github.moduth.blockcanary.BlockCanaryContext;

import java.util.List;

public class AppContext extends BlockCanaryContext {

    private static final String TAG = "AppContext";

    /**
     * Qualifier which can specify this installation, like version + flavor.
     */
    @Override
    public String provideQualifier() {
        String qualifier = "";
        try {
            PackageInfo info = DemoApplication.getAppContext().getPackageManager().getPackageInfo(DemoApplication.getAppContext().getPackageName(), 0);
            qualifier += info.versionCode + "_" + info.versionName + "_YYB";
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "provideQualifier exception", e);
        }
        return qualifier;
    }

    /**
     * 用户 ID，细化归因。
     */
    @Override
    public String provideUid() {
        return "87224330";
    }

    /**
     * 提供当前的网络类型。
     */
    @Override
    public String provideNetworkType() {
        return "4G";
    }

    /**
     * 提供当前的网络类型。
     */
    @Override
    public int provideMonitorDuration() {
        return 9999;
    }

    /**
     * 消息卡顿的阈值，即一个消息耗时超过多久就认为发生了卡顿。
     */
    @Override
    public int provideBlockThreshold() {
        return 100;
    }

    @Override
    public boolean displayNotification() {
        return BuildConfig.DEBUG;
    }

    @Override
    public List<String> concernPackages() {
        List<String> list = super.provideWhiteList();
        list.add("com.example");
        return list;
    }

    @Override
    public List<String> provideWhiteList() {
        List<String> list = super.provideWhiteList();
        list.add("com.whitelist");
        return list;
    }

    @Override
    public boolean stopWhenDebugging() {
        return true;
    }

}