package com.aidangrabe.studentapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.aidangrabe.studentapp.activities.NewClassActivity;


/**
 * Created by aidan on 07/01/15.
 *
 */
public class MainMenuFragment extends ListFragment {

    private ArrayAdapter<String> mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAdapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_list_item_1);
        mAdapter.add("+ Add Class");
        setListAdapter(mAdapter);

        mAdapter.notifyDataSetChanged();

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        if (position == 0) {
            createClass();
        }

    }

    public void createClass() {
        Intent intent = new Intent(getActivity(), NewClassActivity.class);
        startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d("DEBUG", String.format("ActivityResult: request: %d, resultCode: %d, data: %s", requestCode, resultCode, data.toString()));

    }
}
