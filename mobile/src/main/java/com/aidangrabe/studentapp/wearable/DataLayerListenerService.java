package com.aidangrabe.studentapp.wearable;

import android.database.Cursor;
import android.util.Log;

import com.aidangrabe.studentapp.models.todolist.ToDoItem;
import com.aidangrabe.studentapp.models.todolist.ToDoItemManager;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by aidan on 09/01/15.
 * Service for listening for Wearable connections and messages on the Data Layer
 */
public class DataLayerListenerService extends WearableListenerService {

    private static final String MESSAGE_REQUEST_TODO_ITEMS = "/request-todo-items";
    private static final String DATA_ITEM_PREFIX = "/item";

    private GoogleApiClient mGoogleApiClient;

    @Override
    public void onCreate() {
        super.onCreate();

        mGoogleApiClient = new GoogleApiClient.Builder(this).addApi(Wearable.API).build();
        mGoogleApiClient.connect();
        Wearable.MessageApi.addListener(mGoogleApiClient, this);
        Wearable.DataApi.addListener(mGoogleApiClient, this);

    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {

        Log.d("DEBUG", "Message Received: " + messageEvent.getPath());
        if (messageEvent.getPath().endsWith(MESSAGE_REQUEST_TODO_ITEMS)) {
            sendToDoItems();
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Wearable.MessageApi.removeListener(mGoogleApiClient, this);
        Wearable.DataApi.removeListener(mGoogleApiClient, this);
        mGoogleApiClient.disconnect();

    }

    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {
        Log.d("DEBUG", "onDataChanged");
    }

    @Override
    public void onPeerConnected(Node peer) {
        super.onPeerConnected(peer);
    }

    private void sendToDoItems() {

//        mGoogleApiClient.blockingConnect(100, TimeUnit.MILLISECONDS);
        Log.d("DEBUG", "Retrieving TodoItems");

        if (!mGoogleApiClient.isConnected()) {
            // error
            Log.d("DEBUG", "NOT CONNECTED");
            return;
        }

        List<ToDoItem> items = queryToDoItems();
        for (ToDoItem item : items) {
            Log.d("DEBUG", "Sending ToDoItem");
            final PutDataMapRequest putDataMapRequest = ToDoItemManager.toPutDataMapRequest(item, DATA_ITEM_PREFIX + item.getId());
            PendingResult<DataApi.DataItemResult> pendingResult = Wearable.DataApi.putDataItem(mGoogleApiClient,
                    putDataMapRequest.asPutDataRequest());
            pendingResult.setResultCallback(new ResultCallback<DataApi.DataItemResult>() {
                @Override
                public void onResult(DataApi.DataItemResult dataItemResult) {
                    Log.d("DEBUG", "Data sent");
                }
            });
        }


    }

    private List<ToDoItem> queryToDoItems() {

        ToDoItemManager manager = new ToDoItemManager(this);
        Cursor cursor = manager.getAll();
        List<ToDoItem> items = new ArrayList<>();
        while (cursor.moveToNext()) {
            items.add(ToDoItemManager.instanceFromCursor(cursor));
        }
        cursor.close();
        return items;

    }

}
