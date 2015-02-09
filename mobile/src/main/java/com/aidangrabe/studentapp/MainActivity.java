package com.aidangrabe.studentapp;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.ActionBarActivity;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.aidangrabe.studentapp.fragments.MainMenuFragment;
import com.aidangrabe.studentapp.services.NewsDownloaderService;
import com.aidangrabe.studentapp.services.TimeTableService;
import com.aidangrabe.studentapp.wearable.DataLayerListenerService;


public class MainActivity extends ActionBarActivity {

    private static final String PREF_NEWS_ALARM_TIME = "alarm_news";
    private static final String PREF_TIMETABLE_ALARM_TIME = "alarm_timetable";

    private static final long UPDATE_INTERVAL_TIMETABLE = DateUtils.MINUTE_IN_MILLIS;
    private static final long UPDATE_INTERVAL_NEWS_DOWNLOADER = DateUtils.HOUR_IN_MILLIS * 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);


        Intent intent = new Intent(this, DataLayerListenerService.class);
        startService(intent);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new MainMenuFragment())
                    .commit();
        }

        setupAlarms(getApplication());

    }

    // setup the news downloading and lecture notification alarms
    private void setupAlarms(Application context) {

        long currentMilis = SystemClock.elapsedRealtime();

        SharedPreferences prefs = getPreferences(MODE_PRIVATE);
        long newsAlarmMillis = prefs.getLong(PREF_NEWS_ALARM_TIME, currentMilis);
        long timetableAlarmMillis = prefs.getLong(PREF_TIMETABLE_ALARM_TIME, currentMilis);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);

        if (alarmNeedsReset(newsAlarmMillis, UPDATE_INTERVAL_NEWS_DOWNLOADER)) {
            setAlarm(alarmManager, NewsDownloaderService.class, UPDATE_INTERVAL_NEWS_DOWNLOADER);
            prefs.edit().putLong(PREF_NEWS_ALARM_TIME, currentMilis).apply();
        }

        if (alarmNeedsReset(timetableAlarmMillis, UPDATE_INTERVAL_TIMETABLE)) {
            setAlarm(alarmManager, TimeTableService.class, UPDATE_INTERVAL_TIMETABLE);
            prefs.edit().putLong(PREF_TIMETABLE_ALARM_TIME, currentMilis).apply();
        }

    }

    /**
     * Check if a given alarm needs to be reset
     * @param lastStartMillis the last time (in millis) that the clock was set
     * @param interval the time for which the alarm waits before triggering
     * @return true if the alarm needs to be reset
     */
    private boolean alarmNeedsReset(long lastStartMillis, long interval) {
        return SystemClock.elapsedRealtime() - lastStartMillis < interval;
    }

    private void setAlarm(AlarmManager alarmManager, Class<? extends Service> serviceClass, long interval) {
        Log.d("D", "Setting alarm");
        Intent intent = new Intent(this, serviceClass);
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, intent, 0);

        alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + interval, interval, pendingIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
