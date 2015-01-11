package com.aidangrabe.studentapp.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aidangrabe.common.SharedConstants;
import com.aidangrabe.studentapp.R;

/**
 * Created by aidan on 11/01/15.
 *
 */
public class TimeTableFragment extends Fragment {

    public static final String ARG_DAY = "day";
    private int mDay;

    public static TimeTableFragment makeInstance(int day) {

        Bundle args = new Bundle();
        args.putInt(ARG_DAY, day);

        TimeTableFragment fragment = new TimeTableFragment();
        fragment.setArguments(args);

        return fragment;

    }

    public TimeTableFragment() {
        mDay = 0;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_timetable, container, false);

        Bundle args = getArguments();
        mDay = args.getInt(ARG_DAY, SharedConstants.Day.MONDAY);

        return view;

    }
}
