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

import com.aidangrabe.common.SharedConstants;
import com.aidangrabe.studentapp.R;
import com.aidangrabe.studentapp.models.Lecture;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aidan on 11/01/15.
 *
 */
public class TimeTableFragment extends Fragment {

    public static final String ARG_COUNT = "day";
    private int mDay;
    private List<Lecture> mLectures;
    private ArrayAdapter<Lecture> mAdapter;
    private ListView mListView;

    public static TimeTableFragment makeInstance(List<Lecture> lectures, int day) {

        Bundle args = new Bundle();
        int count = 0;

        for (Lecture lecture : lectures) {
            if (lecture.getDayOfWeek() == day) {
                args.putBundle("lecture_" + count, lecture.toBundle());
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
            mLectures.add(Lecture.instanceFromBundle(args.getBundle("lecture_" + i)));
        }

        mAdapter = new ArrayAdapter<Lecture>(getActivity(), android.R.layout.simple_list_item_1) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);

                Lecture lecture = getItem(position);
                ((TextView) view.findViewById(android.R.id.text1)).setText(lecture.getName());

                return view;
            }
        };
        mAdapter.addAll(mLectures);
        mListView = (ListView) view.findViewById(R.id.list_view);
        mListView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

        return view;

    }
}
