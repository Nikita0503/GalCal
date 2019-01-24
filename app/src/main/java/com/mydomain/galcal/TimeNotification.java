package com.mydomain.galcal;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by Nikita on 24.01.2019.
 */

public class TimeNotification extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String userName = intent.getStringExtra("userName");
        String event = intent.getStringExtra("event");
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context, "id")
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(userName)
                        .setContentText(event)
                        .setSound(uri);
        Notification notification = builder.build();

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(1, notification);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("id", "GalCal",
                    NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("GalCal Notifications");
            channel.enableLights(true);
            channel.setLightColor(Color.GREEN);
            channel.enableVibration(false);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
