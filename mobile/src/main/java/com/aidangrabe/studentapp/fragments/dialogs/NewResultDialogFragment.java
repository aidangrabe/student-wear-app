package com.aidangrabe.studentapp.fragments.dialogs;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.aidangrabe.common.model.Result;
import com.aidangrabe.studentapp.R;

/**
 * Created by aidan on 03/02/15.
 *
 */
public class NewResultDialogFragment extends DialogFragment implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    private SeekBar mGradeSeekBar;
    private TextView mPercentText;
    private OnSaveListener mListener;


    public interface OnSaveListener {
        public void onSaveResult(Result result);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_fragment_new_result, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mGradeSeekBar = (SeekBar) view.findViewById(R.id.grade_seek_bar);
        mGradeSeekBar.setOnSeekBarChangeListener(this);

        Button saveButton = (Button) view.findViewById(R.id.save_button);
        saveButton.setOnClickListener(this);

        mPercentText = (TextView) view.findViewById(R.id.percent_text);
        updatePercenText(mGradeSeekBar.getProgress());

        getDialog().setTitle("New Result");

    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.save_button) {
            // save
            Result result = new Result(mGradeSeekBar.getProgress() / 100.0f);
            mListener.onSaveResult(result);

        }

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        // show the user the seek bar value as a percentage
        if (fromUser) {
            updatePercenText(progress);
        }

    }

    private void updatePercenText(int progress) {
        mPercentText.setText(String.format("%d%%", progress));
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    public void setOnSaveListener(OnSaveListener listener) {
        mListener = listener;
    }

}
