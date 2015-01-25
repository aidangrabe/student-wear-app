package com.aidangrabe.studentapp.fragments;

import android.app.ListFragment;
import android.database.Cursor;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.aidangrabe.common.model.todolist.ToDoItem;
import com.aidangrabe.common.model.todolist.ToDoItemManager;
import com.aidangrabe.studentapp.R;
import com.melnykov.fab.FloatingActionButton;

/**
 * Created by aidan on 09/01/15.
 * A Fragment to display the ToDoList items from the Database
 */
public class ToDoListFragment extends ListFragment {

    private ArrayAdapter<ToDoItem> mAdapter;
    private NewToDoItemFragment mNewItemFragment;

    private final View.OnClickListener mSaveClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onSaveNewItem();
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

        mAdapter = new ArrayAdapter<ToDoItem>(getActivity(), android.R.layout.simple_list_item_1) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);

                ToDoItem item = getItem(position);

                TextView tv = (TextView) view.findViewById(android.R.id.text1);
                tv.setTextColor(getResources().getColor(R.color.text_color));
                tv.setText(item.getTitle());

                // toggle strike through
                if (item.isCompleted()) {
                    tv.setPaintFlags(tv.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                } else {
                    tv.setPaintFlags(tv.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
                }

                return view;

            }
        };

        setListAdapter(mAdapter);
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
        mAdapter.clear();

        while (cursor.moveToNext()) {
            ToDoItem item = ToDoItemManager.instanceFromCursor(cursor);

//            if (!item.isCompleted()) {
                mAdapter.add(item);
//            }
        }

        cursor.close();
        mAdapter.notifyDataSetChanged();

    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        ToDoItemManager manager = new ToDoItemManager(getActivity());
        ToDoItem item = mAdapter.getItem(position);

        TextView tv = (TextView) v.findViewById(android.R.id.text1);

        // toggle strike through
        item.complete();
        manager.update(item);

        tv.setPaintFlags(tv.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);


    }

    /**
     * Called when the save button has been pressed on the NewToDoItemFragment
     */
    private void onSaveNewItem() {

        ToDoItem newItem = new ToDoItem(mNewItemFragment.getTitle());
        ToDoItemManager manager = new ToDoItemManager(getActivity());
        manager.save(newItem);
        mNewItemFragment.dismiss();
        getToDoList();

    }

    private void showNewItemDialog() {

        mNewItemFragment = new NewToDoItemFragment();
        mNewItemFragment.setSaveListener(mSaveClickListener);
        mNewItemFragment.show(getFragmentManager(), "dialog");

    }

}
