package com.aidangrabe.studentapp.activities;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.View;

import com.aidangrabe.common.SharedConstants;
import com.aidangrabe.studentapp.R;
import com.aidangrabe.studentapp.fragments.TimeTableFragment;
import com.aidangrabe.studentapp.models.Lecture;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by aidan on 08/01/15.
 * Activity that shows the user's time table
 */
public class TimeTableActivity extends ActionBarActivity {

    private FragmentPagerAdapter mPagerAdapter;
    private ViewPager mViewPager;
    private Map<Integer, String> mDayNames;
    private List<Lecture> mLectures;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_timetable);

        try {
            mLectures = Lecture.getSavedLectures(this);
        } catch (Exception e) {
            // error
            mLectures = new ArrayList<>();
        }

        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mPagerAdapter = new TimeTablePagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mPagerAdapter);
        mDayNames = new HashMap<>();

        // TODO: localize day names
        mDayNames.put(SharedConstants.Day.MONDAY, "Monday");
        mDayNames.put(SharedConstants.Day.TUESDAY, "Tuesday");
        mDayNames.put(SharedConstants.Day.WEDNESDAY, "Wednesday");
        mDayNames.put(SharedConstants.Day.THURSDAY, "Thursday");
        mDayNames.put(SharedConstants.Day.FRIDAY, "Friday");
        mDayNames.put(SharedConstants.Day.SATURDAY, "Saturday");
        mDayNames.put(SharedConstants.Day.SUNDAY, "Sunday");

    }

    public class TimeTablePagerAdapter extends FragmentPagerAdapter {

        public TimeTablePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return TimeTableFragment.makeInstance(mLectures, position);
        }

        @Override
        public int getCount() {
            return 7;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mDayNames.get(position);
        }
    }

}
