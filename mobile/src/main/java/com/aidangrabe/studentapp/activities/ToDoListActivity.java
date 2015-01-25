package com.aidangrabe.studentapp.activities;

import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.aidangrabe.common.SharedConstants;
import com.aidangrabe.common.model.todolist.ToDoItem;
import com.aidangrabe.common.model.todolist.ToDoItemManager;
import com.aidangrabe.studentapp.R;
import com.aidangrabe.studentapp.fragments.ToDoListFragment;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

import static android.util.Log.d;

/**
 * Created by aidan on 09/01/15.
 * Activity to show current ToDoList items
 */
public class ToDoListActivity extends ActionBarActivity {

    private static final String TAG_FRAGMENT = "ToDoListFragment";
    private ToDoListFragment mFragment;
    private GoogleApiClient mApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        mFragment = new ToDoListFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.container, mFragment, TAG_FRAGMENT).commit();

        mApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .build();

    }

    @Override
    protected void onStart() {
        super.onStart();

        mApiClient.connect();

    }

    @Override
    protected void onStop() {
        super.onStop();

        mApiClient.disconnect();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getTitle().equals("Remove complete")) {
            removeComplete();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        menu.add("Remove complete");
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Remove the complete ToDoItems from the database, and DataLayer
     */
    private void removeComplete() {
        d("D", "removing complete items");
        ToDoItemManager itemManager = new ToDoItemManager(this);
        Cursor cursor = itemManager.getComplete();
        while (cursor.moveToNext()) {
            ToDoItem toDoItem = ToDoItemManager.instanceFromCursor(cursor);
            itemManager.delete(toDoItem);
        }
        cursor.close();

        // refresh the list
        mFragment.getToDoList();

        // clear the data layer for the ToDoItems
        new ClearDataLayerTask().execute();

    }

    private class ClearDataLayerTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            Log.d("D", "Deleting items in background");
            // clear the current DataLayer
            String nodeId = Wearable.NodeApi.getLocalNode(mApiClient).await().getNode().getId();
            Uri itemUri = new Uri.Builder().scheme(PutDataRequest.WEAR_URI_SCHEME)
//                    .authority(nodeId)
                    .path(SharedConstants.Wearable.MESSAGE_REQUEST_TODO_ITEMS)
                    .build();
            Wearable.DataApi.deleteDataItems(mApiClient, itemUri);
            Log.d("D", "Uri: " + itemUri.toString());

            return null;
        }

    }

}
