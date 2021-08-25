package com.ztiany.androidipc.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.ztiany.androidipc.R;
import com.ztiany.androidipc.config.Const;
import com.ztiany.androidipc.service.IPCService;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initService();
    }

    public void opMessenger(View view) {
        startActivity(new Intent(this, MessengerActivity.class));
    }

    public void openBookManagerAct(View view) {
        startActivity(new Intent(this, BookManagerActivity.class));
    }

    public void openBinderPool(View view) {
        startActivity(new Intent(this, BinderPollActivity.class));
    }

    public void startSelf(View view) {
        startActivity(new Intent(this, this.getClass()));
    }

    public void openSocket(View view) {
        startActivity(new Intent(this, SocketActivity.class));
    }

    private void initService() {
        Const.ConsName = "MainActivity";
        startService(new Intent(this, IPCService.class));
    }
}
