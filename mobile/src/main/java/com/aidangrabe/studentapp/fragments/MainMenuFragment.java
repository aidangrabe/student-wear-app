package com.aidangrabe.studentapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.aidangrabe.studentapp.R;
import com.aidangrabe.studentapp.activities.NewClassActivity;
import com.aidangrabe.studentapp.activities.TimeTableActivity;
import com.aidangrabe.studentapp.models.Lecture;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;


/**
 * Created by aidan on 07/01/15.
 *
 */
public class MainMenuFragment extends ListFragment {

    // the list's adapter
    private ArrayAdapter<String> mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAdapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_list_item_1) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);

                TextView tv = (TextView) view.findViewById(android.R.id.text1);
                tv.setTextColor(getResources().getColor(R.color.DefaultTextColor));

                return view;
            }
        };

        setListAdapter(mAdapter);
        refreshAdapter();

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        String item = mAdapter.getItem(position);

        // Add class
        if (item.equals(getResources().getString(R.string.menu_add_class))) {
            startActivity(NewClassActivity.class);
        }
        // Timetable
        else if (item.equals(getResources().getString(R.string.menu_timetable))) {
            startActivity(TimeTableActivity.class);
        }

    }

    public void startActivity(Class c) {
        Intent intent = new Intent(getActivity(), c);
        startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d("DEBUG", String.format("ActivityResult: request: %d, resultCode: %d, data: %s", requestCode, resultCode, data.toString()));

    }

    // reload the data in the adapter
    public void refreshAdapter() {

        mAdapter.clear();

        mAdapter.add(getResources().getString(R.string.menu_add_class));
        mAdapter.add(getResources().getString(R.string.menu_timetable));

        mAdapter.notifyDataSetChanged();

    }

}
