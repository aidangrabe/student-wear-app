package com.aidangrabe.studentapp.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.aidangrabe.studentapp.R;
import com.aidangrabe.studentapp.activities.TimeTableActivity;
import com.aidangrabe.studentapp.activities.ToDoListActivity;
import com.aidangrabe.studentapp.activities.games.SnakeActivity;


/**
 * Created by aidan on 07/01/15.
 *
 */
public class MainMenuFragment extends ListFragment {

    // the list's adapter
    private ArrayAdapter<MenuItem> mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAdapter = new ArrayAdapter<MenuItem>(getActivity().getApplicationContext(), android.R.layout.simple_list_item_1) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = convertView;
                if (view == null) {
                    view = LayoutInflater.from(getActivity()).inflate(R.layout.list_item_main_menu, parent, false);
                }

                MenuItem menuItem = getItem(position);
                TextView tv = (TextView) view.findViewById(R.id.title_text);
                TextView tvDescription = (TextView) view.findViewById(R.id.description_text);
                ImageView iconView = (ImageView) view.findViewById(R.id.icon);

                tv.setText(menuItem.getTitle());
                tvDescription.setText(menuItem.getDescription());
                iconView.setImageBitmap(menuItem.getIcon());

                return view;
            }
        };

        createMenu();
        setListAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        MenuItem item = mAdapter.getItem(position);
        Intent intent = new Intent(getActivity(), item.getActivityClass());
        startActivity(intent);

    }

    private void createMenu() {

        mAdapter.add(new MenuItem(R.string.menu_modules, R.string.menu_modules_description, R.drawable.ic_plus, ToDoListActivity.class));
        mAdapter.add(new MenuItem(R.string.menu_todo_list, R.string.menu_todo_list_description, R.drawable.ic_todo, ToDoListActivity.class));
        mAdapter.add(new MenuItem(R.string.menu_timetable, R.string.menu_timetable_description, R.drawable.ic_timetable, TimeTableActivity.class));
        mAdapter.add(new MenuItem(R.string.menu_games, R.string.menu_games_description, R.drawable.ic_games, SnakeActivity.class));

    }

    /**
     * This class represents a single menu item in a list of menu options
     * Each menu item contains a title, description, icon and Activity class to start when selected
     */
    protected class MenuItem {

        private String mTitle, mDescription;
        private Bitmap mIcon;
        private Class<? extends Activity> mActivityClass;

        public MenuItem(int titleRes, int descRes, int iconRes, Class<? extends Activity> activityClass) {
            this(getResources().getString(titleRes), getResources().getString(descRes),
                    BitmapFactory.decodeResource(getResources(), iconRes), activityClass);
        }

        public MenuItem(String title, String description, Bitmap iconBitmap, Class<? extends Activity> activityClass) {
            mTitle = title;
            mDescription = description;
            mIcon = iconBitmap;
            mActivityClass = activityClass;
        }

        public String getTitle() {
            return mTitle;
        }

        public void setTitle(String mTitle) {
            this.mTitle = mTitle;
        }

        public String getDescription() {
            return mDescription;
        }

        public void setDescription(String mDescription) {
            this.mDescription = mDescription;
        }

        public Bitmap getIcon() {
            return mIcon;
        }

        public void setIcon(Bitmap mIcon) {
            this.mIcon = mIcon;
        }

        public Class<? extends Activity> getActivityClass() {
            return mActivityClass;
        }

        public void setActivityClass(Class<? extends Activity> mActivityClass) {
            this.mActivityClass = mActivityClass;
        }
    }

}
