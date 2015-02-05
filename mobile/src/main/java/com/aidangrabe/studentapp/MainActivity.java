package com.aidangrabe.studentapp;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.aidangrabe.studentapp.fragments.MainMenuFragment;
import com.aidangrabe.studentapp.services.NewsDownloaderService;
import com.aidangrabe.studentapp.wearable.DataLayerListenerService;


public class MainActivity extends ActionBarActivity {

    private static final String PREF_NEWS_ALARM_ACTIVE = "news_alarm_active";

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

        setupAlarm(getApplication());

    }

    private void setupAlarm(Application context) {

        SharedPreferences prefs = getPreferences(MODE_PRIVATE);
        boolean alarmUp = prefs.getBoolean(PREF_NEWS_ALARM_ACTIVE, false);

        if (alarmUp) {
            Log.d("D", "Alarm is already active");
        } else {
            Log.d("D", "Alarm not already active");
            Intent intent = new Intent(context, NewsDownloaderService.class);
            PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, 0);

            AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
            alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), 1000 * 60 * 60, pendingIntent);

            // update the prefs
            prefs.edit().putBoolean(PREF_NEWS_ALARM_ACTIVE, true).apply();
        }

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
