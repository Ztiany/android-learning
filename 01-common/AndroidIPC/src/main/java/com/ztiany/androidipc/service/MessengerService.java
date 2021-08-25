package com.ztiany.androidipc.service;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import com.ztiany.androidipc.activity.MessengerActivity;

import java.lang.ref.WeakReference;

public class MessengerService extends Service {

    public MessengerService() {
    }

    //本地信使，传递给客户端
    private Messenger mMessenger = new Messenger(new MessengerHandler(this));

    //客户端信使，用于向客户端发送消息
    private Messenger mClientMessenger;

    @Override
    public IBinder onBind(Intent intent) {
        return mMessenger.getBinder();
    }

    public void initClientMessenger(Messenger messenger) {
        mClientMessenger = messenger;
        Message message = Message.obtain();
        message.what = 2;
        Bundle bundle = new Bundle();
        bundle.putString("key", "你好，我是客服务端端");
        message.setData(bundle);
        try {
            mClientMessenger.send(message);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private static class MessengerHandler extends Handler {

        WeakReference<MessengerService> mServiceWeakReference;

        MessengerHandler(MessengerService messengerService) {
            mServiceWeakReference = new WeakReference<>(messengerService);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.d("MessengerHandler", "msg.what:" + msg.what);
            Log.d("MessengerHandler", "msg.obj:" + msg.getData().get("key"));
            switch (msg.what) {
                case MessengerActivity.CLIENT_MESSENGER:
                    if (mServiceWeakReference.get() != null) {
                        mServiceWeakReference.get().initClientMessenger(msg.replyTo);
                    }
                    break;
                case 4234:
                    if (mServiceWeakReference.get() != null) {
                        mServiceWeakReference.get().sendMessage();
                    }
                    break;
            }
        }
    }

    private void sendMessage() {
        Message message = Message.obtain();
        message.what = 2;
        Bundle bundle = new Bundle();
        bundle.putString("key", "有本事就来啊");
        message.setData(bundle);
        try {
            mClientMessenger.send(message);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

}