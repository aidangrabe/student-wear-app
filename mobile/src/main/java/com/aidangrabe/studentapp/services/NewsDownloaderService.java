package com.aidangrabe.studentapp.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import com.aidangrabe.studentapp.activities.NewsActivity;

/**
 * Created by aidan on 05/02/15.
 * This service downloads news articles and posts a Notification if new articles are found
 */
public class NewsDownloaderService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();

        downloadArticles();

        stopSelf();

    }

    private void downloadArticles() {

        // TODO: download articles
        // TODO: check downloaded articles for new items
        showNotification();

    }

    private void showNotification() {
        int notificationId = 001;

        Intent viewIntent = new Intent(this, NewsActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, viewIntent, 0);

        NotificationCompat.BigTextStyle bigStyle = new NotificationCompat.BigTextStyle();
        bigStyle.bigText("This is longer text that has a lot more information than the other smaller text");

        Notification notif = new NotificationCompat.Builder(this)
                .setContentTitle("Ucc News Title")
                .setContentText("This is a sample notification")
                .setSmallIcon(com.aidangrabe.common.R.drawable.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), com.aidangrabe.common.R.drawable.ic_globe))
                .setStyle(bigStyle)
                .setContentIntent(pendingIntent)
                .extend(new NotificationCompat.WearableExtender().setBackground(BitmapFactory.decodeResource(getResources(), com.aidangrabe.common.R.drawable.ic_globe)))
                .build();
        notif.flags = notif.flags | Notification.FLAG_AUTO_CANCEL;
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(notificationId, notif);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
