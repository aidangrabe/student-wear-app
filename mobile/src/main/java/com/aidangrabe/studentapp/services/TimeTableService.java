package com.aidangrabe.studentapp.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import com.aidangrabe.studentapp.R;
import com.aidangrabe.studentapp.activities.TimeTableActivity;
import com.aidangrabe.studentapp.models.Lecture;

import java.util.Calendar;
import java.util.List;

/**
 * Created by aidan on 07/02/15.
 * This class creates notifications if a given Lecture is starting soon
 */
public class TimeTableService extends Service {

    // the time in minutes before a lecture to show the notification
    private static int DELTA_TIME = 10;

    @Override
    public void onCreate() {
        super.onCreate();

        List<Lecture> lectures = Lecture.listAll(Lecture.class);

        if (lectures.size() == 0) {
            finish();
        }

        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minutes = calendar.get(Calendar.MINUTE);

        Lecture nextLecture = null;

        for (Lecture lecture : lectures) {
            long delta = toMinutes(lecture) - toMinutes(hour, minutes);
            if (delta > 0 && delta < DELTA_TIME) {
                nextLecture = lecture;
            }
        }

        if (nextLecture != null) {
            showNotification(nextLecture);
        }

        finish();

    }

    private long toMinutes(Lecture lecture) {
        return toMinutes(lecture.getStartHour(), lecture.getStartMinute());
    }

    private long toMinutes(int hours, int minutes) {
        return hours * 60 + minutes;
    }

    private void showNotification(Lecture lecture) {

        Intent intent = new Intent(this, TimeTableActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        Notification notif = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_stat_social_school)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher))
                .setContentTitle(String.format("%d:%01d - %s", lecture.getStartHour(), lecture.getStartMinute(),
                        lecture.getName()))
                .setContentText(lecture.getLocation())
                .setContentIntent(pendingIntent)
                .build();

        notif.flags = notif.flags | Notification.FLAG_AUTO_CANCEL;

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(001, notif);

    }

    private void finish() {
        stopSelf();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


}
