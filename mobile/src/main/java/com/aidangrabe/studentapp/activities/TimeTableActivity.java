package com.aidangrabe.studentapp.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aidangrabe.studentapp.R;
import com.aidangrabe.studentapp.fragments.TimeTableFragment;
import com.aidangrabe.studentapp.models.Lecture;
import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by aidan on 08/01/15.
 * Activity that shows the user's time table
 */
public class TimeTableActivity extends ActionBarActivity {

    private FragmentPagerAdapter mPagerAdapter;
    private ViewPager mViewPager;
    private List<Lecture> mLectures;

    private List<Integer> mDaysToShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_timetable);

        getLectures();

        createIndex();

        Collections.sort(mDaysToShow, new Comparator<Integer>() {
            @Override
            public int compare(Integer lhs, Integer rhs) {
                return lhs - rhs;
            }
        });

        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mPagerAdapter = new TimeTablePagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mPagerAdapter);

        setToCurrentDay();

        // make the tabs look like they're part of the ActionBar
        getSupportActionBar().setElevation(0);
        PagerTabStrip strip = (PagerTabStrip) findViewById(R.id.tab_strip);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            strip.setElevation(8);
        }

        addFabToView((ViewGroup) getWindow().getDecorView().findViewById(android.R.id.content));

    }

    private void getLectures() {
        try {
            mLectures = Lecture.listAll(Lecture.class);
        } catch (Exception e) {
            // error
            mLectures = new ArrayList<>();
        }
    }

    private void setToCurrentDay() {
        // open the current day
        int currentDay = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        Log.d("D", "Current Day: " + currentDay);
        if (mDaysToShow.contains(currentDay)) {
            Log.d("D", "Setting to today: " + getPageFromDay(currentDay));
            mViewPager.setCurrentItem(getPageFromDay(currentDay));
        }
    }

    private int getPageFromDay(int day) {
        return mDaysToShow.indexOf(day);
    }

    private int getDayIndex(int i) {
        return mDaysToShow.get(i);
    }

    private void createIndex() {
        mDaysToShow = new ArrayList<>();
        for (Lecture lecture : mLectures) {
            if (!mDaysToShow.contains(lecture.getDayOfWeek())) {
                mDaysToShow.add(lecture.getDayOfWeek());
            }
        }
    }

    private void addFabToView(ViewGroup viewGroup) {

        // inflate the FAB view and add it to the given ViewGroup
        View view = LayoutInflater.from(TimeTableActivity.this)
                .inflate(R.layout.fab_new_layout, viewGroup, true);
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);

        // get the day the user is currently viewing

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // start the NewClassActivity
                Intent intent = new Intent(TimeTableActivity.this, NewClassActivity.class);
                intent.putExtra(NewClassActivity.ARG_DEFAULT_DAY, getDayIndex(mViewPager.getCurrentItem()));
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        reloadTimetable();

    }

    public String getDayName(int i) {
        switch (i) {
            default:
            case 1:
                return "Sunday";
            case 2:
                return "Monday";
            case 3:
                return "Tuesday";
            case 4:
                return "Wednesday";
            case 5:
                return "Thursday";
            case 6:
                return "Friday";
            case 7:
                return "Saturday";
        }
    }

    private void reloadTimetable() {
        getLectures();
        createIndex();
        mPagerAdapter.notifyDataSetChanged();
    }

    public class TimeTablePagerAdapter extends FragmentPagerAdapter implements TimeTableFragment.Listener {

        public TimeTablePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            TimeTableFragment frag = TimeTableFragment.makeInstance(mLectures, getDayIndex(position));
            frag.setListener(this);
            return frag;
        }

        @Override
        public int getCount() {
            return mDaysToShow.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return getDayName(getDayIndex(position));
        }

        @Override
        public void onDeleteLecture(Lecture lecture, int remainingLectures) {
//            mLectures.remove(lecture);
            reloadTimetable();
        }
    }

}
