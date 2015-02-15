package com.aidangrabe.studentapp.activities;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import com.aidangrabe.studentapp.R;

/**
 * Created by aidan on 15/02/15.
 * This Activity displays the apps settings
 */
public class SettingsActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_frag_container);

        getFragmentManager()
                .beginTransaction()
                .replace(R.id.container, new SettingsFragment())
                .commit();

    }

    public static class SettingsFragment extends PreferenceFragment {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            Log.d("D", "PreferenceFragment onCreate");
            addPreferencesFromResource(R.xml.preferences);

        }
    }

}
