package com.aidangrabe.studentapp.ui;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TimePicker;

import java.util.Calendar;

/**
 * Created by aidan on 07/01/15.
 * Class to make an EditText show a TimePicker when clicked
 */
public class TimeEditText implements View.OnFocusChangeListener, DialogInterface.OnDismissListener {

    private Boolean mPickerShowing;
    private EditText mEditText;
    private Context mContext;
    private TimePickerDialog mTimePickerDialog;
    private int mHourOfDay, mMinutes;
    private TimeEditTextListener mListener;

    /**
     * Simple interface to mimic OnTimeSetListener, but is passed a TimeEditText instead of
     * a TimePicker
     */
    public interface TimeEditTextListener {
        public void onTimeSet(TimeEditText editText, int hourOfDay, int minute);
    }

    private TimePickerDialog.OnTimeSetListener mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            setTime(hourOfDay, minute);
            if (mListener != null) {
                mListener.onTimeSet(TimeEditText.this, hourOfDay, minute);
            }
        }
    };

    public TimeEditText(Context context, EditText editText) {

        mContext = context;
        mEditText = editText;
        mEditText.setOnFocusChangeListener(this);
        mPickerShowing = false;

        mEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePicker();
            }
        });

    }

    public EditText getEditText() {
        return mEditText;
    }

    public void showTimePicker() {

        parseTime();
        mTimePickerDialog = new TimePickerDialog(mContext, mTimeSetListener, mHourOfDay,
                mMinutes, false);
        mTimePickerDialog.setTitle("Pick Time");
        mTimePickerDialog.setOnDismissListener(this);
        mTimePickerDialog.show();

        mPickerShowing = true;

    }

    // extract the hour and minutes from the EditText
    public void parseTime() {

        String timeText = mEditText.getText().toString();
        String[] parts = timeText.split(":");
        try {
            mHourOfDay = Integer.parseInt(parts[0]);
            mMinutes = Integer.parseInt(parts[1]);
        } catch (Exception e) {
            Calendar cal = Calendar.getInstance();
            mHourOfDay = cal.get(Calendar.HOUR_OF_DAY);
            mMinutes = cal.get(Calendar.MINUTE);
        }

    }

    public int getHourOfDay() {
        return mHourOfDay;
    }

    public int getMinutes() {
        return mMinutes;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {

        if (hasFocus) {
            showTimePicker();
        }

    }

    public void onSaveInstanceState(Bundle outState) {

        if (mPickerShowing && mTimePickerDialog != null) {
            mTimePickerDialog.dismiss();
        }

    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        mPickerShowing = false;
    }

    /**
     * The listener for when the TimePickerDialog's OnTimeSetListener gets notified
     * @param listener
     */
    public void setListener(TimeEditTextListener listener) {
        this.mListener = listener;
    }

    /**
     * Set the TimeEditText's time
     * This is the time that will be populated into the TimePickerDialog
     * @param hourOfDay the hour of the day (24-hour)
     * @param minute the minute value
     */
    public void setTime(int hourOfDay, int minute) {
        mMinutes = minute;
        mHourOfDay = hourOfDay;
        mEditText.setText(String.format("%d:%02d", hourOfDay, minute));
    }

}
