package com.dn.deamon;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.dn.deamon.account.AccountHelper;
import com.dn.deamon.jobscheduler.MyJobService;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //账户同步拉活
        AccountHelper.addAccount(this);
        AccountHelper.autoSync();
        //JobScheduler拉活
        MyJobService.startJob(this);
    }

}