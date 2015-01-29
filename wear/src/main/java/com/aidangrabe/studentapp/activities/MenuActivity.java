package com.aidangrabe.studentapp.activities;

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

import com.aidangrabe.studentapp.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aidan on 29/01/15.
 *
 */
public class MenuActivity extends Activity implements WearableListView.ClickListener {

    private WearableListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_menu);

        mListView = (WearableListView) findViewById(R.id.wearable_list);
        mListView.setClickListener(this);
        mListView.setAdapter(new Adapter(this, onCreateMenu()));

    }

    public List<MenuItem> onCreateMenu() {
        return new ArrayList<>();
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

    public class MenuItem {

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

        public void setTitle(String mTitle) {
            this.mTitle = mTitle;
        }

        public void setActivityClass(Class<? extends Activity> mActivityClass) {
            this.mActivityClass = mActivityClass;
        }

        public void setIconDrawable(Drawable mIconDrawable) {
            this.mIconDrawable = mIconDrawable;
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
