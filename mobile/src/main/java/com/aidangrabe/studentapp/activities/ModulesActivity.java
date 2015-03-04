package com.aidangrabe.studentapp.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.aidangrabe.common.activities.BaseMobileActivity;
import com.aidangrabe.studentapp.R;
import com.aidangrabe.studentapp.fragments.ModulesFragment;

/**
 * Created by aidan on 31/01/15.
 *
 */
public class ModulesActivity extends BaseMobileActivity {

    public static final String FRAG_TAG = "modules-fragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        setTitle(getResources().getString(R.string.modules_title));

        // try find the fragment first
        Fragment frag = getSupportFragmentManager().findFragmentByTag(FRAG_TAG);
        if (frag == null) {
            frag = new ModulesFragment();
            frag.setRetainInstance(true);
            getSupportFragmentManager().beginTransaction().replace(R.id.container, frag, FRAG_TAG).commit();
        }

    }
}
