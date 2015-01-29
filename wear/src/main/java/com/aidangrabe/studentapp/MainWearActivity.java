package com.aidangrabe.studentapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.wearable.view.WearableListView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aidangrabe.studentapp.activities.FindMyPhoneActivity;
import com.aidangrabe.studentapp.activities.MapActivity;
import com.aidangrabe.studentapp.activities.ToDoListActivity;
import com.aidangrabe.studentapp.activities.games.GameMenuActivity;

import java.util.ArrayList;
import java.util.List;

public class MainWearActivity extends Activity implements WearableListView.ClickListener {

    private WearableListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_menu);

        mListView = (WearableListView) findViewById(R.id.wearable_list);
        mListView.setAdapter(new Adapter(this, getMenuOptions()));
        mListView.setClickListener(this);

    }

    // create the menu
    private List<MenuItem> getMenuOptions() {
        List<MenuItem> items = new ArrayList<>();
        items.add(new MenuItem(R.string.menu_todo_list, ToDoListActivity.class, R.drawable.ic_todo));
        items.add(new MenuItem(R.string.menu_games, GameMenuActivity.class, R.drawable.ic_games));
        items.add(new MenuItem(R.string.menu_map, MapActivity.class, R.drawable.ic_map));
        items.add(new MenuItem(R.string.find_my_phone, FindMyPhoneActivity.class, R.drawable.ic_phone));
        return items;
    }

    @Override
    public void onClick(WearableListView.ViewHolder viewHolder) {

        Adapter.ItemViewHolder itemHolder = (Adapter.ItemViewHolder) viewHolder;

        Class<? extends Activity> newActivityClass = itemHolder.menuItem.getActivityClass();

        if (newActivityClass != null) {
            Intent intent = new Intent(this, newActivityClass);
            startActivity(intent);
        }

    }

    @Override
    public void onTopEmptyRegionClick() {}

    private class MenuItem {

        private String mTitle;
        private Class<? extends Activity> mActivityClass;
        private Drawable mIconDrawable;

        public MenuItem(int titleResource, Class<? extends Activity> activityClass, int iconResource) {
            mTitle = getResources().getString(titleResource);
            mActivityClass = activityClass;
            mIconDrawable = getResources().getDrawable(iconResource);
        }

        public String getTitle() {
            return mTitle;
        }

        public Class<? extends Activity> getActivityClass() {
            return mActivityClass;
        }

        public Drawable getIconDrawable() {
            return mIconDrawable;
        }
    }

    private static class Adapter extends WearableListView.Adapter {

        private Context mContext;
        private LayoutInflater mInflater;
        private List<MenuItem> mMenuItems;

        public Adapter(Context context, List<MenuItem> menuItems) {
            mContext = context;
            mInflater = LayoutInflater.from(context);
            mMenuItems = menuItems;
        }

        // Provide a reference to the type of views you're using
        public static class ItemViewHolder extends WearableListView.ViewHolder {
            private TextView textView;
            private ImageView iconView;
            private MenuItem menuItem;
            public ItemViewHolder(View itemView) {
                super(itemView);
                // find the text view within the custom item's layout
                textView = (TextView) itemView.findViewById(R.id.name);
                iconView = (ImageView) itemView.findViewById(R.id.item_icon);
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
            MenuItem menuItem = mMenuItems.get(position);

            ItemViewHolder itemHolder = (ItemViewHolder) holder;

            itemHolder.textView.setText(menuItem.getTitle());
            itemHolder.iconView.setImageDrawable(menuItem.getIconDrawable());
            itemHolder.menuItem = menuItem;
        }

        // Return the size of your dataset
        // (invoked by the WearableListView's layout manager)
        @Override
        public int getItemCount() {
            return mMenuItems.size();
        }
    }

}
