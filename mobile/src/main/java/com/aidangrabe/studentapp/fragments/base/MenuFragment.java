package com.aidangrabe.studentapp.fragments.base;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by aidan on 31/01/15.
 *
 */
public class MenuFragment extends android.support.v4.app.ListFragment {

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
