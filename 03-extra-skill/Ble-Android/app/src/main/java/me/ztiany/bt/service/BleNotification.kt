package me.ztiany.bt.service

import android.content.pm.ServiceInfo
import android.os.Build
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

private const val CHANNEL = "gatt_server_channel"
private const val NOTIFICATION_ID = 100

internal fun BleService.startInForeground() {
    val channel = NotificationChannelCompat.Builder(
        CHANNEL,
        NotificationManagerCompat.IMPORTANCE_HIGH,
    )
        .setName("GATT Server channel")
        .setDescription("Channel for the GATT server sample")
        .build()

    NotificationManagerCompat.from(this).createNotificationChannel(channel)

    val notification = NotificationCompat.Builder(this, CHANNEL)
        .setSmallIcon(applicationInfo.icon)
        .setContentTitle("GATT Server")
        .setContentText("Running...")
        .build()

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        startForeground(
            NOTIFICATION_ID,
            notification,
            ServiceInfo.FOREGROUND_SERVICE_TYPE_CONNECTED_DEVICE,
        )
    } else {
        startForeground(NOTIFICATION_ID, notification)
    }
}