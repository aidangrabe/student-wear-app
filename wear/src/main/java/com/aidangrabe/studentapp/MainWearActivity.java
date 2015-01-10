package com.aidangrabe.studentapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.support.wearable.view.WearableListView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aidangrabe.studentapp.activities.ToDoListActivity;

public class MainWearActivity extends Activity implements WearableListView.ClickListener {

    private WearableListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_menu);

        String[] menuOptions = getResources().getStringArray(R.array.main_menu_options);

        mListView = (WearableListView) findViewById(R.id.wearable_list);
        mListView.setAdapter(new Adapter(this, menuOptions));
        mListView.setClickListener(this);

    }

    @Override
    public void onClick(WearableListView.ViewHolder viewHolder) {

        // TODO: not all options should start the todo list activity! :P
        Intent intent = new Intent(this, ToDoListActivity.class);
        startActivity(intent);

    }

    @Override
    public void onTopEmptyRegionClick() {
        Log.d("DEBUG", "onTopEmptyRegionClick!");
    }

    private static class Adapter extends WearableListView.Adapter {

        private Context mContext;
        private LayoutInflater mInflater;
        private String[] mDataset;

        public Adapter(Context context, String[] dataset) {
            mContext = context;
            mInflater = LayoutInflater.from(context);
            mDataset = dataset;
        }

        // Provide a reference to the type of views you're using
        public static class ItemViewHolder extends WearableListView.ViewHolder {
            private TextView textView;
            public ItemViewHolder(View itemView) {
                super(itemView);
                // find the text view within the custom item's layout
                textView = (TextView) itemView.findViewById(R.id.name);
            }
        }
        // Create new views for list items
        // (invoked by the WearableListView's layout manager)
        @Override
        public WearableListView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                              int viewType) {
            // Inflate our custom layout for list items
            return new ItemViewHolder(mInflater.inflate(R.layout.list_item_main_menu, null));
        }

        // Replace the contents of a list item
        // Instead of creating new views, the list tries to recycle existing ones
        // (invoked by the WearableListView's layout manager)
        @Override
        public void onBindViewHolder(WearableListView.ViewHolder holder,
                                     int position) {
            // retrieve the text view
            ItemViewHolder itemHolder = (ItemViewHolder) holder;
            TextView view = itemHolder.textView;
            // replace text contents
            view.setText(mDataset[position]);
            // replace list item's metadata
            holder.itemView.setTag(position);
        }

        // Return the size of your dataset
        // (invoked by the WearableListView's layout manager)
        @Override
        public int getItemCount() {
            return mDataset.length;
        }
    }

}
