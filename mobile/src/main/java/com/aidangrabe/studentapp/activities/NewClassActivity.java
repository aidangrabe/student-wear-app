package com.aidangrabe.studentapp.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.aidangrabe.studentapp.R;
import com.aidangrabe.studentapp.fragments.NewClassFragment;

/**
 * Created by aidan on 07/01/15.
 *
 */
public class NewClassActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle(R.string.new_class);

        setContentView(R.layout.activity_main);

        getFragmentManager().beginTransaction().replace(R.id.container, new NewClassFragment()).commit();

    }



}
