package com.aidangrabe.common.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;

import com.aidangrabe.common.R;

/**
 * Created by aidan on 05/02/15.
 * This is a simple Activity that contains one Fragment
 */
public abstract class FragContainerActivity extends ActionBarActivity {

    public static final String FRAG_TAG = "main-fragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_frag_container);

        // try find the fragment first
        Fragment frag = getSupportFragmentManager().findFragmentByTag(FRAG_TAG);
        if (frag == null) {
            frag = onCreateFragment();
            frag.setRetainInstance(true);
            getSupportFragmentManager().beginTransaction().replace(R.id.container, frag, FRAG_TAG).commit();
        }

    }

    protected abstract Fragment onCreateFragment();

}
