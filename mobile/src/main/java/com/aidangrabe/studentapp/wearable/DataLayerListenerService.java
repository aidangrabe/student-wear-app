package com.aidangrabe.studentapp.wearable;

import android.database.Cursor;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Parcel;
import android.util.Log;

import com.aidangrabe.common.SharedConstants;
import com.aidangrabe.common.wearable.WearUtil;
import com.aidangrabe.common.model.todolist.ToDoItem;
import com.aidangrabe.common.model.todolist.ToDoItemManager;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.tagmanager.DataLayer;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItemBuffer;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aidan on 09/01/15.
 * Service for listening for Wearable connections and messages on the Data Layer
 */
public class DataLayerListenerService extends WearableListenerService {

    private static final String DATA_ITEM_PREFIX = "/item";

    private GoogleApiClient mGoogleApiClient;
    private WearUtil mWearUtil;

    @Override
    public void onCreate() {
        super.onCreate();

        mGoogleApiClient = new GoogleApiClient.Builder(this).addApi(Wearable.API).build();
        mGoogleApiClient.connect();
        Wearable.MessageApi.addListener(mGoogleApiClient, this);
        Wearable.DataApi.addListener(mGoogleApiClient, this);
        mWearUtil = new WearUtil(this);
        mWearUtil.connect();

    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {

        String path = messageEvent.getPath();

        // request todoitems
        if (path.endsWith(SharedConstants.Wearable.MESSAGE_REQUEST_TODO_ITEMS)) {
            sendToDoItems(messageEvent.getSourceNodeId());
        }
        // update a todoitem
        else if (path.equals(SharedConstants.Wearable.MESSAGE_UPDATE_TODO_ITEM)) {
            int id = Integer.valueOf(new String(messageEvent.getData()));
            updateToDoItem(id);
        }
        // create new todoitem
        else if (path.equals(SharedConstants.Wearable.MESSAGE_CREATE_TODO_ITEM)) {
            ToDoItem item = new ToDoItem(new String(messageEvent.getData()));
            ToDoItemManager manager = new ToDoItemManager(this);
            manager.save(item);
        }
        // find my phone
        else if (path.equals(SharedConstants.Wearable.MESSAGE_FIND_MY_PHONE)) {
            findMyPhone();
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Wearable.MessageApi.removeListener(mGoogleApiClient, this);
        Wearable.DataApi.removeListener(mGoogleApiClient, this);
        mGoogleApiClient.disconnect();
        mWearUtil.disconnect();

    }

    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {
        Log.d("DEBUG", "onDataChanged");

        for (DataEvent event : dataEvents) {
            DataMap dataMap = DataMap.fromByteArray(event.getDataItem().getData());
            ToDoItem item = ToDoItemManager.fromDataMap(dataMap);
            ToDoItemManager itemManager = new ToDoItemManager(this);
            if (item.getId() == -1) {
                Log.d("D", "Saving new ToDoItem");
                itemManager.save(item);
            } else {
                Log.d("D", "Updating existing ToDoItem");
                itemManager.update(item);
            }
        }

    }

    @Override
    public void onPeerConnected(Node peer) {
        super.onPeerConnected(peer);
    }

    private void sendToDoItems(String nodeId) {

        Log.d("DEBUG", "Retrieving TodoItems");

        if (!mGoogleApiClient.isConnected()) {
            // error
            Log.d("DEBUG", "NOT CONNECTED");
            return;
        }

        List<ToDoItem> items = queryToDoItems();
//        items.add(new ToDoItem("Hello!"));

        // putDataMapRequest
//        final PutDataMapRequest putDataMapRequest = PutDataMapRequest.create(SharedConstants.Wearable.MESSAGE_REQUEST_TODO_ITEMS);
//        DataMap dataMap = putDataMapRequest.getDataMap();
//        Parcel parcel = Parcel.obtain();
//        parcel.writeTypedList(items);
//        dataMap.putByteArray("itemlist", parcel.marshall());
//        parcel.recycle();
//
//        // sync
//        Log.d("DEBUG", "Sending ToDoItem");
//        PendingResult<DataApi.DataItemResult> pendingResult = Wearable.DataApi.putDataItem(mGoogleApiClient,
//                putDataMapRequest.asPutDataRequest());
//        pendingResult.setResultCallback(new ResultCallback<DataApi.DataItemResult>() {
//            @Override
//            public void onResult(DataApi.DataItemResult dataItemResult) {
//                Log.d("DEBUG", "Data sent");
//            }
//        });


        for (ToDoItem item : items) {
            Log.d("DEBUG", "Sending ToDoItem");
            final PutDataMapRequest putDataMapRequest = ToDoItemManager.toPutDataMapRequest(item, SharedConstants.Wearable.MESSAGE_REQUEST_TODO_ITEMS);
            Wearable.DataApi.putDataItem(mGoogleApiClient, putDataMapRequest.asPutDataRequest()).await();
//            .setResultCallback(new ResultCallback<DataApi.DataItemResult>() {
//                @Override
//                public void onResult(DataApi.DataItemResult dataItemResult) {
//                    Log.d("DEBUG", "Data sent");
//                }
//            });
        }

        Log.d("D", "Sending refresh list message");
        Wearable.MessageApi.sendMessage(mGoogleApiClient, nodeId, "/todolist/refresh-list", null);


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

    /**
     * Complete a given ToDoItem by it's id
     * @param id the id of the ToDoItem to complete
     */
    private void updateToDoItem(int id) {

        ToDoItemManager manager = new ToDoItemManager(this);

        ToDoItem item = manager.get(id);
        if (item != null) {
            item.complete();
        }
        manager.update(item);

    }

    // plays a noise so the user can find their phone
    private void findMyPhone() {

        AudioManager manager = (AudioManager) getSystemService(AUDIO_SERVICE);
        int currentVolume = manager.getStreamVolume(AudioManager.STREAM_RING);

        // set max volume
        manager.setStreamVolume(AudioManager.STREAM_RING, manager.getStreamMaxVolume(AudioManager.STREAM_RING), 0);

        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
