package com.aidangrabe.studentapp.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.aidangrabe.studentapp.R;
import com.aidangrabe.studentapp.models.Lecture;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by aidan on 11/01/15.
 *
 */
public class TimeTableFragment extends Fragment {

    public static final String ARG_COUNT = "day";
    public static final String ARG_LECTURE_PREFIX = "lecture_";

    private int mDay;
    private List<Lecture> mLectures;
    private ArrayAdapter<Lecture> mAdapter;
    private ListView mListView;

    // compare 2 Lectures by their start time
    private final Comparator<Lecture> mLectureComparator = new Comparator<Lecture>() {
        @Override
        public int compare(Lecture lhs, Lecture rhs) {
            return lhs.getStartHour() * 60 + lhs.getStartMinute() > rhs.getStartHour() * 60 + rhs.getStartMinute() ? 0 : 1;
        }
    };

    public static TimeTableFragment makeInstance(List<Lecture> lectures, int day) {

        Bundle args = new Bundle();
        int count = 0;

        for (Lecture lecture : lectures) {
            if (lecture.getDayOfWeek() == day) {
                args.putBundle(ARG_LECTURE_PREFIX + count, lecture.toBundle());
                count++;
            }
        }

        args.putInt(ARG_COUNT, count);

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

        mLectures = new ArrayList<>();
        Bundle args = getArguments();
        int count = args.getInt(ARG_COUNT, 0);

        for (int i = 0; i < count; i++) {
            mLectures.add(Lecture.instanceFromBundle(args.getBundle(ARG_LECTURE_PREFIX + i)));
        }

        Collections.sort(mLectures, mLectureComparator);

        mAdapter = new ArrayAdapter<Lecture>(getActivity(), 0) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                LayoutInflater inflater = getActivity().getLayoutInflater();
                View view = inflater.inflate(R.layout.list_item_timetable_lecture, parent, false);

                Lecture lecture = getItem(position);

                ((TextView) view.findViewById(R.id.title)).setText(lecture.getName());
                ((TextView) view.findViewById(R.id.time)).setText(String.format("%d:%02d", lecture.getStartHour(), lecture.getStartMinute()));
                ((TextView) view.findViewById(R.id.duration)).setText(getDuration(lecture));

                return view;
            }
        };
        mAdapter.addAll(mLectures);
        mListView = (ListView) view.findViewById(R.id.list_view);
        mListView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

        return view;

    }

    private String getDuration(Lecture lecture) {

        NumberFormat df = DecimalFormat.getInstance();
        df.setMaximumFractionDigits(2);
        df.setRoundingMode(RoundingMode.DOWN);

        int startMins = lecture.getStartHour() * 60 + lecture.getStartMinute();
        int endMins = lecture.getEndHour() * 60 + lecture.getEndMinute();
        int delta = endMins - startMins;
        String time = Integer.toString(delta) + " mins";
        if (delta >= 60) {
            time = df.format(delta / 60f) + " hrs";
        }
        return time;

    }

}
