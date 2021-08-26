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

public class RemoteService extends Service {

    private static final String TAG = RemoteService.class.getSimpleName();

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
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        RemoteService.this.bindService(new Intent(RemoteService.this, LocalService.class), conn, Context.BIND_IMPORTANT);

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
            Log.i(TAG, "LocalService服务被干掉了~~~~断开连接！");
            Toast.makeText(RemoteService.this, "LocalService服务被干掉了~~~~断开连接！", Toast.LENGTH_SHORT).show();
            //启动被干掉的
            RemoteService.this.startService(new Intent(RemoteService.this, LocalService.class));
            RemoteService.this.bindService(new Intent(RemoteService.this, LocalService.class), conn, Context.BIND_IMPORTANT);
        }

    }

}
