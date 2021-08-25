package com.dn.keepliveprocess;

import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.dn.keepliveprocess.one_pixel.KeepLiveActivityManager;
import com.dn.keepliveprocess.one_pixel.ScreenListener;

public class LocalService extends Service {

    private static final String TAG = LocalService.class.getSimpleName();

    private MyBinder binder;
    private MyServiceConnection conn;

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (binder == null) {
            binder = new MyBinder();
        }
        conn = new MyServiceConnection();

        ScreenListener listener = new ScreenListener(this);
        listener.begin(new ScreenListener.ScreenStateListener() {

            @Override
            public void onUserPresent() {
            }

            @Override
            public void onScreenOn() {
                // 开屏---finish这个一个像素的Activity
                KeepLiveActivityManager.getInstance(LocalService.this).finishKeepLiveActivity();
            }

            @Override
            public void onScreenOff() {
                // 锁屏---启动一个像素的Activity
                KeepLiveActivityManager.getInstance(LocalService.this).startKeepLiveActivity();
            }
        });

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LocalService.this.bindService(new Intent(LocalService.this, RemoteService.class), conn, Context.BIND_IMPORTANT);

        PendingIntent contentIntent = PendingIntent.getService(this, 0, intent, 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setTicker("361")
                .setContentIntent(contentIntent)
                .setContentTitle("我是361，我怕谁!")
                .setAutoCancel(true)
                .setContentText("hehehe")
                .setWhen(System.currentTimeMillis());

        //把service设置为前台运行，避免手机系统自动杀掉改服务。
        startForeground(startId, builder.build());
        return START_STICKY;
    }


    private class MyBinder extends RemoteConnection.Stub {
        @Override
        public String getProcessName() throws RemoteException {
            return "LocalService";
        }
    }

    private class MyServiceConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.i(TAG, "建立连接成功！");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.i(TAG, "RemoteService服务被干掉了~~~~断开连接！");
            Toast.makeText(LocalService.this, "RemoteService服务被干掉了~~~~断开连接！", Toast.LENGTH_SHORT).show();
            //启动被干掉的
            LocalService.this.startService(new Intent(LocalService.this, RemoteService.class));
            LocalService.this.bindService(new Intent(LocalService.this, RemoteService.class), conn, Context.BIND_IMPORTANT);
        }

    }


}
