package com.aidangrabe.studentapp.fragments;

import android.app.ListFragment;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.aidangrabe.studentapp.R;
import com.aidangrabe.studentapp.models.todolist.ToDoItem;
import com.aidangrabe.studentapp.models.todolist.ToDoItemManager;
import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;

/**
 * Created by aidan on 09/01/15.
 * A Fragment to display the ToDoList items from the Database
 */
public class ToDoListFragment extends ListFragment {

    private ArrayAdapter<String> mAdapter;
    private ArrayList<ToDoItem> mItems;
    private NewToDoItemFragment mNewItemFragment;

    private final View.OnClickListener mSaveClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Toast.makeText(getActivity(), "Save button clicked", Toast.LENGTH_SHORT).show();
        }
    };

    private final View.OnClickListener mNewToDoItemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            showNewItemDialog();
        }
    };

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

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View v = inflater.inflate(R.layout.fab_new_layout, (ViewGroup) view);

        FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.fab);
        fab.setOnClickListener(mNewToDoItemClickListener);

        fab.attachToListView(getListView());

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

    private void showNewItemDialog() {

        mNewItemFragment = new NewToDoItemFragment();
        mNewItemFragment.setSaveListener(mSaveClickListener);
        mNewItemFragment.show(getFragmentManager(), "dialog");

    }

}
