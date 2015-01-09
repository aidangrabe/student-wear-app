package com.aidangrabe.studentapp.fragments;

import android.app.ListFragment;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.aidangrabe.studentapp.R;
import com.aidangrabe.studentapp.models.todolist.ToDoItem;
import com.aidangrabe.studentapp.models.todolist.ToDoItemManager;

import java.util.ArrayList;

/**
 * Created by aidan on 09/01/15.
 * A Fragment to display the ToDoList items from the Database
 */
public class ToDoListFragment extends ListFragment {

    private ArrayAdapter<String> mAdapter;
    private ArrayList<ToDoItem> mItems;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);

                TextView tv = (TextView) view.findViewById(android.R.id.text1);
                tv.setTextColor(getResources().getColor(R.color.text_color));
                return view;

            }
        };

        setListAdapter(mAdapter);
        mItems = new ArrayList<>();
        getToDoList();

    }

    public void getToDoList() {

        Log.d("DEBUG", "Getting todolist items");
        ToDoItemManager manager = new ToDoItemManager(getActivity());
        Cursor cursor = manager.getAll();
        mItems.clear();
        mAdapter.clear();

        while (cursor.moveToNext()) {
            ToDoItem item = ToDoItemManager.instanceFromCursor(cursor);
            mItems.add(item);
            mAdapter.add(item.getTitle());
        }

        cursor.close();
        mAdapter.notifyDataSetChanged();

    }

}
