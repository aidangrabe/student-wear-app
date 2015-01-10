package com.aidangrabe.studentapp.activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.aidangrabe.studentapp.R;
import com.aidangrabe.studentapp.util.wearable.WearUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Wearable;

/**
 * Created by aidan on 10/01/15.
 * Activity to show the current ToDoItems
 */
public class ToDoListActivity extends Activity implements GoogleApiClient.ConnectionCallbacks, DataApi.DataListener {

    private static final String MESSAGE_REQUEST_TODO_ITEMS = "/request-todo-items";

    private ListView mListView;
    private WearUtil mWearUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_todo_list);

        mListView = (ListView) findViewById(R.id.list_view);

        Log.d("DEBUG", "Create WearUtils");
        mWearUtil = new WearUtil(this);

        getToDoItems();

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
        mWearUtil.sendMessage(MESSAGE_REQUEST_TODO_ITEMS, "Hello World!");

    }

    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {
        Log.d("DEBUG", "Received a data change");
    }
}
