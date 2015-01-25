package com.aidangrabe.studentapp.fragments;

import android.database.Cursor;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.aidangrabe.common.adapters.ToDoListAdapter;
import com.aidangrabe.common.model.todolist.ToDoItem;
import com.aidangrabe.common.model.todolist.ToDoItemManager;
import com.aidangrabe.common.wearable.WearableFragment;
import com.aidangrabe.studentapp.R;
import com.melnykov.fab.FloatingActionButton;

/**
 * Created by aidan on 09/01/15.
 * A Fragment to display the ToDoList items from the Database
 */
public class ToDoListFragment extends WearableFragment implements AdapterView.OnItemClickListener {

    private ListView mListView;
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
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_todo_list, container, false);

        mListView = (ListView) view.findViewById(R.id.list_view);

        mAdapter = new ToDoListAdapter(getActivity(), android.R.layout.simple_list_item_1);

        mListView.setAdapter(mAdapter);
        getToDoList();

        return view;

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View v = inflater.inflate(R.layout.fab_new_layout, (ViewGroup) view);

        FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.fab);
        fab.setOnClickListener(mNewToDoItemClickListener);

        fab.attachToListView(mListView);

    }

    public void getToDoList() {

        Log.d("DEBUG", "Getting todolist items");
        ToDoItemManager manager = new ToDoItemManager(getActivity());
        Cursor cursor = manager.getAll();
        mAdapter.clear();

        while (cursor.moveToNext()) {
            ToDoItem item = ToDoItemManager.instanceFromCursor(cursor);
            mAdapter.add(item);
        }

        cursor.close();
        mAdapter.notifyDataSetChanged();

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
        mNewItemFragment.show(getActivity().getFragmentManager(), "dialog");

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        ToDoItemManager manager = new ToDoItemManager(getActivity());
        ToDoItem item = mAdapter.getItem(position);

        TextView tv = (TextView) view.findViewById(android.R.id.text1);

        // toggle strike through
        item.complete();
        manager.update(item);

        tv.setPaintFlags(tv.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
    }
}
