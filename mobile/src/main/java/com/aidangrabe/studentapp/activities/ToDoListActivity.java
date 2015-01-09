package com.aidangrabe.studentapp.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.aidangrabe.studentapp.R;
import com.aidangrabe.studentapp.fragments.ToDoListFragment;

/**
 * Created by aidan on 09/01/15.
 * Activity to show current ToDoList items
 */
public class ToDoListActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        getFragmentManager().beginTransaction().replace(R.id.container, new ToDoListFragment()).commit();

    }

}
