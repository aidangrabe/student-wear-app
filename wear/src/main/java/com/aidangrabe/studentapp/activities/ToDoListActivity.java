package com.aidangrabe.studentapp.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.aidangrabe.common.SharedConstants;
import com.aidangrabe.common.adapters.ToDoListAdapter;
import com.aidangrabe.common.model.todolist.ToDoItem;
import com.aidangrabe.common.model.todolist.ToDoItemManager;
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
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.Wearable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

/**
 * Created by aidan on 10/01/15.
 * Activity to show the current ToDoItems
 */
public class ToDoListActivity extends Activity implements GoogleApiClient.ConnectionCallbacks, DataApi.DataListener,
        AdapterView.OnItemClickListener {

    private static final int SPEECH_REQUEST_CODE = 0;

    private Collection<Node> mNodes;
    private ListView mListView;
    private GoogleApiClient mApiClient;
    private List<ToDoItem> mItems;
    private ArrayAdapter<ToDoItem> mAdapter;
    private final Comparator<ToDoItem> mAdapterComparator = new Comparator<ToDoItem>() {
        @Override
        public int compare(ToDoItem lhs, ToDoItem rhs) {
            return rhs.getCreationDate().compareTo(lhs.getCreationDate());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_todo_list);

        mListView = (ListView) findViewById(R.id.list_view);

        mApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .build();

        mItems = new ArrayList<>();
        mNodes = new HashSet<>();

        mAdapter = new ToDoListAdapter(this, android.R.layout.simple_list_item_1);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);

    }

    @Override
    protected void onStart() {
        super.onStart();

        mApiClient.connect();
        Wearable.DataApi.addListener(mApiClient, this);

    }

    @Override
    protected void onStop() {
        super.onStop();

        Wearable.DataApi.removeListener(mApiClient, this);
        mApiClient.disconnect();

    }

    public void getToDoItems() {

        Logd("gettingToDoListItems");

        Wearable.DataApi.getDataItems(mApiClient).setResultCallback(new ResultCallback<DataItemBuffer>() {
            @Override
            public void onResult(DataItemBuffer dataItems) {

                Logd("DataApi onResult");
                for (Node node : mNodes) {
                    sendMessage(node, SharedConstants.Wearable.MESSAGE_REQUEST_TODO_ITEMS, null);
                }

                for (int i = 0; i < dataItems.getCount(); i++) {

                    if (!dataItems.get(i).getUri().getPath().equals(SharedConstants.Wearable.MESSAGE_REQUEST_TODO_ITEMS)) {
                        continue;
                    }
                    
                    Logd("Found dataItem");
                    byte[] bytes = dataItems.get(i).getData();
                    if (bytes != null) {
                        List<ToDoItem> itemList = ToDoItemManager.listFromDataMap(DataMap.fromByteArray(bytes));
                        mItems = itemList == null ? mItems : itemList;
                    }
                }

                setToDoListItems();

                dataItems.release();
            }
        });

    }

    private void setToDoListItems() {
        Logd("Setting ToDoListItems");

        mAdapter.clear();

        // add a new item row
        mAdapter.add(new ToDoItem(getResources().getString(R.string.todo_new_item)));

        mAdapter.addAll(mItems);

        mAdapter.sort(mAdapterComparator);

        mAdapter.notifyDataSetChanged();

    }

    @Override
    public void onConnected(Bundle bundle) {

        Logd("onConnected");
        getNodes();

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {

        for (DataEvent event : dataEvents) {
            DataItem dataItem = event.getDataItem();

            // update the list
            if (dataItem.getUri().getPath().equals(SharedConstants.Wearable.MESSAGE_REQUEST_TODO_ITEMS)) {
                DataMap dataMap = DataMap.fromByteArray(dataItem.getData());
                List<ToDoItem> itemList = ToDoItemManager.listFromDataMap(dataMap);
                if (itemList != null) {
                    mItems = itemList;
                    Logd("onDataChanged: Got list: " + itemList.size());
                }
            }

        }

        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                setToDoListItems();
            }
        });


    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        if (position == 0) {
            // new item
            displaySpeechRecognizer();
        } else {
            ToDoItem item = mAdapter.getItem(position);

            // toggle the completion state of the ToDoItem
            item.setComplete(!item.isCompleted());

            syncToDoItem(item);
        }

    }

    // Create an intent that can start the Speech Recognizer activity
    private void displaySpeechRecognizer() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        // Start the activity, the intent will be populated with the speech text
        startActivityForResult(intent, SPEECH_REQUEST_CODE);
    }

    // This callback is invoked when the Speech Recognizer returns.
    // This is where you process the intent and extract the speech text from the intent.
    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        if (requestCode == SPEECH_REQUEST_CODE && resultCode == RESULT_OK) {
            List<String> results = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);
            String spokenText = results.get(0);

            // only create a new item if the string is non-empty
            if (spokenText.length() > 0) {
                ToDoItem item = new ToDoItem(spokenText);
                syncToDoItem(item);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * Sync a ToDoItem across to mobile app
     * The mobile app will take care of updating/saving the ToDoItem and will notify us
     * when the list is changed
     * @param item the ToDoItem to sync
     */
    private void syncToDoItem(ToDoItem item) {

        PutDataMapRequest putDataMapRequest = ToDoItemManager.toPutDataMapRequest(item, SharedConstants.Wearable.MESSAGE_CREATE_TODO_ITEM);
        Wearable.DataApi.putDataItem(mApiClient, putDataMapRequest.asPutDataRequest());

    }

    private void getNodes() {

        Logd("Getting nodes");
        Wearable.NodeApi.getConnectedNodes(mApiClient).setResultCallback(new ResultCallback<NodeApi.GetConnectedNodesResult>() {
            @Override
            public void onResult(NodeApi.GetConnectedNodesResult getConnectedNodesResult) {
                onNodesConnected(getConnectedNodesResult.getNodes());
            }
        });

    }

    private void onNodesConnected(Collection<Node> nodes) {
        Logd("Nodes connected");
        mNodes.addAll(nodes);
        getToDoItems();
    }

    private PendingResult<MessageApi.SendMessageResult> sendMessage(Node node, String path, byte[] data) {
        Logd("Sending message");
        return Wearable.MessageApi.sendMessage(mApiClient, node.getId(), path, data);
    }

    private void Logd(String message) {
        Log.d("D", message);
    }
}
