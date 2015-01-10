package com.aidangrabe.studentapp.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.speech.RecognizerIntent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.aidangrabe.common.SharedConstants;
import com.aidangrabe.common.model.todolist.ToDoItem;
import com.aidangrabe.common.model.todolist.ToDoItemManager;
import com.aidangrabe.common.wearable.WearUtil;
import com.aidangrabe.studentapp.R;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.Wearable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Created by aidan on 10/01/15.
 * Activity to show the current ToDoItems
 */
public class ToDoListActivity extends Activity implements GoogleApiClient.ConnectionCallbacks, DataApi.DataListener, AdapterView.OnItemClickListener{

    private static final int SPEECH_REQUEST_CODE = 0;

    private ListView mListView;
    private WearUtil mWearUtil;
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

        mWearUtil = new WearUtil(this)
                .setConnectionCallbacks(this)
                .setDataListener(this);

        mItems = new ArrayList<>();

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
        mWearUtil.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mWearUtil.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();

        mWearUtil.disconnect();

    }

    public void getToDoItems() {

        mAdapter.clear();
        mItems.clear();

        // add a new item row
        mAdapter.add(new ToDoItem(getResources().getString(R.string.todo_new_item)));

        mWearUtil.sendMessage(SharedConstants.Wearable.MESSAGE_REQUEST_TODO_ITEMS, "");

        Wearable.DataApi.getDataItems(mWearUtil.getGoogleApiClient());

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

        for (DataEvent event : dataEvents) {
            DataItem dataItem = event.getDataItem();
            if (dataItem.getUri().getPath().equals(SharedConstants.Wearable.MESSAGE_REQUEST_TODO_ITEMS)) {
                final ToDoItem item = ToDoItemManager.fromDataMap(DataMap.fromByteArray(event.getDataItem().getData()));
                if (!mItems.contains(item)) {
                    mItems.add(item);
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            mAdapter.notifyDataSetChanged();
                            mAdapter.add(item);
                            mAdapter.sort(mAdapterComparator);
                        }
                    });
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
            mWearUtil.sendMessage(SharedConstants.Wearable.MESSAGE_UPDATE_TODO_ITEM, Integer.toString(item.getId()));
            mAdapter.notifyDataSetChanged();
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
                createToDoItem(spokenText);
                mItems.add(item);
                mAdapter.insert(item, 1);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void createToDoItem(String title) {

        mWearUtil.sendMessage(SharedConstants.Wearable.MESSAGE_CREATE_TODO_ITEM, title);

    }

}
