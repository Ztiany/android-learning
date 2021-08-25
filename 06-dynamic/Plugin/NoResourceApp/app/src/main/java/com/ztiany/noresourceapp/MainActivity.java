package com.ztiany.noresourceapp;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        installView();
        registerBroadcast();
    }

    private void registerBroadcast() {
        IntentFilter filter = new IntentFilter("com.ztiany.noresource.test");
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Toast.makeText(MainActivity.this, "动态注册广播接收者收到消息", Toast.LENGTH_LONG).show();
            }
        }, filter);
    }

    private void installView() {
        TextView textView = new TextView(this);
        textView.setText("我是插件");
        textView.setTextSize(38);
        textView.setTextColor(Color.RED);
        textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        textView.setGravity(Gravity.CENTER);
        setContentView(textView);
    }
}
