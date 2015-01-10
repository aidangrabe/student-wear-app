package com.aidangrabe.studentapp.activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.aidangrabe.common.SharedConstants;
import com.aidangrabe.common.model.todolist.ToDoItem;
import com.aidangrabe.common.model.todolist.ToDoItemManager;
import com.aidangrabe.common.wearable.WearUtil;
import com.aidangrabe.studentapp.R;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataItemBuffer;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.Wearable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aidan on 10/01/15.
 * Activity to show the current ToDoItems
 */
public class ToDoListActivity extends Activity implements GoogleApiClient.ConnectionCallbacks, DataApi.DataListener {

    private static final String MESSAGE_REQUEST_TODO_ITEMS = "/request-todo-items";

    private ListView mListView;
    private WearUtil mWearUtil;
    private List<ToDoItem> mItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_todo_list);

        mListView = (ListView) findViewById(R.id.list_view);

        Log.d("DEBUG", "Create WearUtils");
        mWearUtil = new WearUtil(this)
                .setConnectionCallbacks(this)
                .setDataListener(this);

        mItems = new ArrayList<>();

    }

    @Override
    protected void onResume() {
        super.onResume();

        mWearUtil.connect();

    }

    @Override
    protected void onPause() {
        super.onPause();

        mWearUtil.disconnect();

    }

    public void getToDoItems() {

        Log.d("DEBUG", "Send Message");
        mWearUtil.sendMessage(MESSAGE_REQUEST_TODO_ITEMS, "");

        mItems.clear();
        PendingResult<DataItemBuffer> results = Wearable.DataApi.getDataItems(mWearUtil.getGoogleApiClient());

    }

    @Override
    public void onConnected(Bundle bundle) {

        getToDoItems();

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {

        Log.d("DEBUG", "onDataChanged");
        mItems.clear();
        for (DataEvent event : dataEvents) {
            DataItem dataItem = event.getDataItem();
            if (dataItem.getUri().getPath().equals(SharedConstants.Wearable.MESSAGE_REQUEST_TODO_ITEMS)) {
                ToDoItem item = ToDoItemManager.fromDataMap(DataMap.fromByteArray(event.getDataItem().getData()));
                mItems.add(item);
                Log.d("DEBUG", item.getTitle());
            }
        }

    }
}
