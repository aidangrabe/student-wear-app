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
import com.aidangrabe.studentapp.activities.ToDoListActivity;
import com.aidangrabe.studentapp.activities.games.SnakeActivity;

import java.util.LinkedHashMap;
import java.util.Map;


/**
 * Created by aidan on 07/01/15.
 *
 */
public class MainMenuFragment extends ListFragment {

    // the list's adapter
    private ArrayAdapter<String> mAdapter;
    private Map<String, Class> mMenuMap;

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

        createMenu();
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

        if (mMenuMap.containsKey(item)) {
            startActivity(mMenuMap.get(item));
        }

    }

    public void startActivity(Class c) {
        Intent intent = new Intent(getActivity(), c);
        startActivity(intent);
    }

    private void createMenu() {

        mMenuMap = new LinkedHashMap<>();
        mMenuMap.put(getResources().getString(R.string.menu_add_class), NewClassActivity.class);
        mMenuMap.put(getResources().getString(R.string.menu_todo_list), ToDoListActivity.class);
        mMenuMap.put(getResources().getString(R.string.menu_timetable), TimeTableActivity.class);
        mMenuMap.put(getResources().getString(R.string.menu_games), SnakeActivity.class);

    }

    // reload the data in the adapter
    public void refreshAdapter() {

        mAdapter.clear();
        mAdapter.addAll(mMenuMap.keySet());
        mAdapter.notifyDataSetChanged();

    }

}
