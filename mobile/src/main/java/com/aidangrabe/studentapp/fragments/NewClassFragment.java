package com.aidangrabe.studentapp.fragments;

import android.app.Fragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.aidangrabe.studentapp.R;
import com.aidangrabe.studentapp.ui.TimeEditText;

/**
 * Created by aidan on 07/01/15.
 *
 */
public class NewClassFragment extends Fragment {

    private TimeEditText mFromEditText, mToEditText;

    private final TimePickerDialog.OnTimeSetListener mFromTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

        }
    };

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EditText fromText = (EditText) view.findViewById(R.id.time_from);
        EditText toText = (EditText) view.findViewById(R.id.time_to);

        mFromEditText = new TimeEditText(getActivity(), fromText, savedInstanceState);
        mToEditText = new TimeEditText(getActivity(), toText, savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_new_class, container, false);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        mFromEditText.onSaveInstanceState(outState);
        mToEditText.onSaveInstanceState(outState);

        super.onSaveInstanceState(outState);

    }
}
