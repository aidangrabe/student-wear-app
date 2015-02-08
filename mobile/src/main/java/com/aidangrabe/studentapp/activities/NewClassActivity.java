package com.aidangrabe.studentapp.activities;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.aidangrabe.studentapp.R;
import com.aidangrabe.studentapp.fragments.NewClassFragment;

/**
 * Created by aidan on 07/01/15.
 *
 */
public class NewClassActivity extends ActionBarActivity {

    public static final String ARG_DEFAULT_DAY = "default-day";

    private static final String FRAG_TAG = "new-class-fragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle(R.string.new_class);

        setContentView(R.layout.activity_main);

        int defaultDay = getIntent().getIntExtra(ARG_DEFAULT_DAY, 2);

        // try find the fragment first
        Fragment frag = getFragmentManager().findFragmentByTag(FRAG_TAG);
        if (frag == null) {
            Bundle args = new Bundle();
            args.putInt(ARG_DEFAULT_DAY, defaultDay);

            frag = new NewClassFragment();
            frag.setArguments(args);
            frag.setRetainInstance(true);
            getFragmentManager().beginTransaction().replace(R.id.container, frag, FRAG_TAG).commit();
        }

    }



}
