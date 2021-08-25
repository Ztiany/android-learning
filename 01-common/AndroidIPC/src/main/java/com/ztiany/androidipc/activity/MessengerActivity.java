package com.ztiany.androidipc.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.ztiany.androidipc.R;
import com.ztiany.androidipc.service.MessengerService;

public class MessengerActivity extends AppCompatActivity {


    private Messenger mServiceMessenger;//服务信使
    private Messenger mClientMessenger;//本地信使，给服务端
    public static final int CLIENT_MESSENGER = 12312;

    private void init() {
        mClientMessenger = new Messenger(new MessengerClientHandler());
        bindService(new Intent(this, MessengerService.class), mMessengerActivity, Context.BIND_AUTO_CREATE);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messenger);
        init();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mMessengerActivity);
    }


    private ServiceConnection mMessengerActivity = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mServiceMessenger = new android.os.Messenger(service);
            Message obtain = Message.obtain();
            obtain.what = CLIENT_MESSENGER;
            //在跨进程通信中，message的obj必须是安卓系统的parcelable类型对象
            Bundle bundle = new Bundle();
            bundle.putString("key", "this is client");
            obtain.setData(bundle);
            //通过replyTo传递messenger
            obtain.replyTo = mClientMessenger;
            try {
                mServiceMessenger.send(obtain);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d("MessengerActivity", "name:" + name);
        }
    };


    private static class MessengerClientHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.d("MessengerClientHandler", "msg.what:" + msg.what);
            Log.d("MessengerClientHandler", "msg.getData()" + msg.getData().get("key"));
        }
    }


    public void send(View view) {
        Message obtain = Message.obtain();
        obtain.what = 4234;
        Bundle bundle = new Bundle();
        bundle.putString("key", "打你");
        obtain.setData(bundle);
        try {
            mServiceMessenger.send(obtain);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
