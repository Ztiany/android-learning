package com.example.administrator.lsn_11_demo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


public class MyBroadCastReciver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("jett", "reciver:" + context);
        Log.i("jett","reciver:" + context.getApplicationContext());
        Log.i("jett","reciver:" + context.getApplicationInfo().className);

    }
}
