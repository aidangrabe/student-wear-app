package com.aidangrabe.studentapp.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.aidangrabe.common.SharedConstants;
import com.aidangrabe.common.model.todolist.ToDoItem;
import com.aidangrabe.common.model.todolist.ToDoItemManager;
import com.aidangrabe.studentapp.R;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.data.FreezableUtils;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataItemBuffer;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageEvent;
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
        AdapterView.OnItemClickListener, MessageApi.MessageListener {

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
//                .addOnConnectionFailedListener(this)
                .build();

        mItems = new ArrayList<>();
        mNodes = new HashSet<>();

        mAdapter = new ArrayAdapter<ToDoItem>(this, android.R.layout.simple_list_item_1) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);

                ToDoItem item = mAdapter.getItem(position);

                TextView tv = (TextView) view.findViewById(android.R.id.text1);
                tv.setTextColor(getResources().getColor(R.color.default_text_color));
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
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);

    }

    @Override
    protected void onStart() {
        super.onStart();

        mApiClient.connect();
        Wearable.DataApi.addListener(mApiClient, this);
        Wearable.MessageApi.addListener(mApiClient, this);

    }

    @Override
    protected void onStop() {
        super.onStop();

        Wearable.MessageApi.removeListener(mApiClient, this);
        Wearable.DataApi.removeListener(mApiClient, this);
        mApiClient.disconnect();

    }

    public void getToDoItems() {

        Logd("gettingToDoListItems");
        Wearable.DataApi.getDataItems(mApiClient).setResultCallback(new ResultCallback<DataItemBuffer>() {
            @Override
            public void onResult(DataItemBuffer dataItems) {

                for (Node node : mNodes) {
                    sendMessage(node, SharedConstants.Wearable.MESSAGE_REQUEST_TODO_ITEMS, null);
                }

                for (int i = 0; i < dataItems.getCount(); i++) {
                    mItems.add(ToDoItemManager.fromDataMap(DataMap.fromByteArray(dataItems.get(i).getData())));
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

        Logd("onDataChanged");

        final List<DataEvent> events = FreezableUtils.freezeIterable(dataEvents);

        dataEvents.close();

        for (DataEvent event : events) {
            DataItem dataItem = event.getDataItem();
            if (dataItem.getUri().getPath().equals(SharedConstants.Wearable.MESSAGE_REQUEST_TODO_ITEMS)) {

                // we don't want to add deleted items to the list
                if (event.getType() == DataEvent.TYPE_DELETED) {
                    Logd("Item deleted");
                    continue;
                }

                if (event.getType() == DataEvent.TYPE_CHANGED) {
                    Logd("Item changed");
                }

                DataMap dataMap = DataMap.fromByteArray(event.getDataItem().getData());
                ToDoItem item = ToDoItemManager.fromDataMap(dataMap);

                boolean wasAdded = false;
                for (int i = 0; i < mItems.size(); i++) {
                    if (mItems.get(i).getId() == item.getId()) {
                        // update
                        mItems.set(i, item);
                        wasAdded = true;
                        break;
                    }
                }

                // id can't be -1. This would mean the item has not been stored on the mobile side yet
                if (!wasAdded && item.getId() != -1) {
                    mItems.add(item);
                }

            }
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        if (position == 0) {
            // new item
            displaySpeechRecognizer();
        } else {
            ToDoItem item = mAdapter.getItem(position);
            item.complete();
            mAdapter.notifyDataSetChanged();

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

            if (spokenText.length() > 0) {
                ToDoItem item = new ToDoItem(spokenText);
                syncToDoItem(item);
//                mItems.add(item);
                setToDoListItems();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void syncToDoItem(ToDoItem item) {

        PutDataMapRequest putDataMapRequest = ToDoItemManager.toPutDataMapRequest(item, SharedConstants.Wearable.MESSAGE_REQUEST_TODO_ITEMS);
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

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {

        Logd("Message received: " + messageEvent.getPath());

        if (messageEvent.getPath().equals("/todolist/refresh-list")) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    setToDoListItems();
                }
            });
        }

    }
}
