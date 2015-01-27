package com.aidangrabe.studentapp.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.aidangrabe.common.SharedConstants;
import com.aidangrabe.common.adapters.ToDoListAdapter;
import com.aidangrabe.common.model.todolist.ToDoItem;
import com.aidangrabe.common.model.todolist.ToDoItemManager;
import com.aidangrabe.common.wearable.WearableFragment;
import com.aidangrabe.studentapp.R;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataMap;
import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aidan on 09/01/15.
 * A Fragment to display the ToDoList items from the Database
 */
public class ToDoListFragment extends WearableFragment implements AdapterView.OnItemClickListener {

    private ListView mListView;
    private List<ToDoItem> mItems;
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
        mItems = new ArrayList<>();

        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);
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
        mItems.clear();

        while (cursor.moveToNext()) {
            ToDoItem item = ToDoItemManager.instanceFromCursor(cursor);
            mAdapter.add(item);
            mItems.add(item);
        }

        cursor.close();
        mAdapter.notifyDataSetChanged();

        ToDoItemManager.sync(getGoogleApiClient(), mItems);

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

        TextView tv = (TextView) view.findViewById(R.id.tv_title);
        ImageView tickImage = (ImageView) view.findViewById(R.id.img_tick);

        // toggle the completion state of the ToDoItem
        item.setComplete(!item.isCompleted());

        manager.update(item);

        ToDoListAdapter.setViewComplete(tv, item.isCompleted());
        animateCheckmark(tickImage, item.isCompleted());

        getToDoList();

    }

    /**
     * Animate the checkmark ImageView
     * @param img the checkmark ImageView to animate
     * @param checked whether to use checked or unchecked animation
     */
    private void animateCheckmark(ImageView img, boolean checked) {

        AnimationSet animations = new AnimationSet(true);

        animations.setInterpolator(new DecelerateInterpolator(3f));
        animations.setDuration(700);

        if (checked) {
            animations.addAnimation(new RotateAnimation(0, 360, img.getPivotX(), img.getPivotY()));
            animations.addAnimation(new AlphaAnimation(0, 1));
        } else {
            animations.addAnimation(new RotateAnimation(360, 0, img.getPivotX(), img.getPivotY()));
            animations.addAnimation(new AlphaAnimation(1, 0));
        }
        img.startAnimation(animations);

    }

    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {

        for (DataEvent event : dataEvents) {

            DataItem dataItem = event.getDataItem();

            if (dataItem.getUri().getPath().equals(SharedConstants.Wearable.MESSAGE_REQUEST_TODO_ITEMS)) {
                DataMap dataMap = DataMap.fromByteArray(dataItem.getData());
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        getToDoList();
                    }
                });
            }

        }

    }
}
