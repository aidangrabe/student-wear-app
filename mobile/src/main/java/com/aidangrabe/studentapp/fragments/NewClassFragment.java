package com.aidangrabe.studentapp.fragments;

import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.aidangrabe.studentapp.R;
import com.aidangrabe.studentapp.models.Lecture;
import com.aidangrabe.studentapp.ui.TimeEditText;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by aidan on 07/01/15.
 * Class to handle adding a New Class/Lecture
 */
public class NewClassFragment extends Fragment implements TimeEditText.TimeEditTextListener {

    private int mDayOfWeek;
    private ArrayAdapter<String> mDaySpinnerAdapter;
    private EditText mNameEditText, mLocationEditText;
    private TimeEditText mFromEditText, mToEditText;
    private Button mSaveButton, mCancelButton;

    private final DialogInterface.OnDismissListener mOnSuccessDismissListener = new DialogInterface.OnDismissListener() {
        @Override
        public void onDismiss(DialogInterface dialog) {
            getActivity().finish();
        }
    };

    private final View.OnClickListener mSaveCancelClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.equals(mSaveButton)) {
                onSavePressed();
            } else {
                onCancelPressed();
            }
        }
    };

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EditText fromText = (EditText) view.findViewById(R.id.time_from);
        EditText toText = (EditText) view.findViewById(R.id.time_to);

        mFromEditText = new TimeEditText(getActivity(), fromText);
        mToEditText = new TimeEditText(getActivity(), toText);

        mFromEditText.setListener(this);
        mToEditText.setListener(this);

        mNameEditText = (EditText) view.findViewById(R.id.class_title);
        mLocationEditText = (EditText) view.findViewById(R.id.class_room);

        mDaySpinnerAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item);
        mDaySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner spinner = (Spinner) view.findViewById(R.id.day_spinner);
        spinner.setAdapter(mDaySpinnerAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mDayOfWeek = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // do nothing
            }
        });
        mDaySpinnerAdapter.add("Monday");
        mDaySpinnerAdapter.add("Tuesday");
        mDaySpinnerAdapter.add("Wednesday");
        mDaySpinnerAdapter.add("Thursday");
        mDaySpinnerAdapter.add("Friday");
        mDaySpinnerAdapter.add("Saturday");
        mDaySpinnerAdapter.add("Sunday");
        mDaySpinnerAdapter.notifyDataSetChanged();

        mCancelButton = (Button) view.findViewById(R.id.cancel_button);
        mSaveButton = (Button) view.findViewById(R.id.save_button);

        mSaveButton.setOnClickListener(mSaveCancelClickListener);
        mCancelButton.setOnClickListener(mSaveCancelClickListener);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_new_class, container, false);
    }

    // called when the save button is pressed
    public void onSavePressed() {

        // ensure that getHourOfDay and getMinutes will return the most recent values
        mFromEditText.parseTime();
        mToEditText.parseTime();

        // create a new Lecture
        Lecture lecture = new Lecture(mNameEditText.getText().toString(), mLocationEditText.getText().toString(), mDayOfWeek,
                mFromEditText.getHourOfDay(), mFromEditText.getMinutes(), mToEditText.getHourOfDay(), mToEditText.getMinutes());

        try {
            // read all lectures, add our new one and write the new list back
            List<Lecture> lectures = Lecture.getSavedLectures(getActivity());
            lectures.add(lecture);
            Lecture.saveLectures(getActivity(), lectures);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), "An error occurred while saving", Toast.LENGTH_SHORT).show();
            return;
        }

        SweetAlertDialog dialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE);
        dialog.setContentText(getResources().getString(R.string.new_class_success));
        dialog.setOnDismissListener(mOnSuccessDismissListener);
        dialog.show();

    }

    // called when the cancel button is pressed
    public void onCancelPressed() {
        getActivity().finish();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        mFromEditText.onSaveInstanceState(outState);
        mToEditText.onSaveInstanceState(outState);

        super.onSaveInstanceState(outState);

    }

    // called when one of the TimeEditText's TimePickerDialogs has been selected
    @Override
    public void onTimeSet(TimeEditText editText, int hourOfDay, int minute) {

        if (editText == mFromEditText) {
            // set the to-time to one hour ahead
            int nextHour = (hourOfDay + 1)  % 24;
            mToEditText.setTime(nextHour, minute);
        }

    }
}
