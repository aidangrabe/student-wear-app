package com.aidangrabe.studentapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aidangrabe.common.SharedConstants;
import com.aidangrabe.studentapp.R;
import com.aidangrabe.studentapp.fragments.TimeTableFragment;
import com.aidangrabe.studentapp.models.Lecture;
import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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

    // the index of the days to show in the tab view
    private List<Integer> mDaysToShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_timetable);

        try {
            mLectures = Lecture.listAll(Lecture.class);
        } catch (Exception e) {
            // error
            mLectures = new ArrayList<>();
        }

        mDaysToShow = new ArrayList<>();
        for (Lecture lecture : mLectures) {
            if (!mDaysToShow.contains(lecture.getDayOfWeek())) {
                mDaysToShow.add(lecture.getDayOfWeek());
            }
        }

        Collections.sort(mDaysToShow, new Comparator<Integer>() {
            @Override
            public int compare(Integer lhs, Integer rhs) {
                return lhs - rhs;
            }
        });

        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mPagerAdapter = new TimeTablePagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mPagerAdapter);
        mDayNames = new HashMap<>();

        mDayNames.put(SharedConstants.Day.MONDAY, "Monday");
        mDayNames.put(SharedConstants.Day.TUESDAY, "Tuesday");
        mDayNames.put(SharedConstants.Day.WEDNESDAY, "Wednesday");
        mDayNames.put(SharedConstants.Day.THURSDAY, "Thursday");
        mDayNames.put(SharedConstants.Day.FRIDAY, "Friday");
        mDayNames.put(SharedConstants.Day.SATURDAY, "Saturday");
        mDayNames.put(SharedConstants.Day.SUNDAY, "Sunday");

        addFabToView((ViewGroup) getWindow().getDecorView().findViewById(android.R.id.content));

    }

    private void addFabToView(ViewGroup viewGroup) {

        // inflate the FAB view and add it to the given ViewGroup
        View view = LayoutInflater.from(TimeTableActivity.this)
                .inflate(R.layout.fab_new_layout, viewGroup, true);
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // start the NewClassActivity
                Intent intent = new Intent(TimeTableActivity.this, NewClassActivity.class);
                startActivity(intent);
            }
        });

    }

    public class TimeTablePagerAdapter extends FragmentPagerAdapter {

        public TimeTablePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return TimeTableFragment.makeInstance(mLectures, mDaysToShow.get(position));
        }

        @Override
        public int getCount() {
            return mDaysToShow.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mDayNames.get(mDaysToShow.get(position));
        }
    }

}
