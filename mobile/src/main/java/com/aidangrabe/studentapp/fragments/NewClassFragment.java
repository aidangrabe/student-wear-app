package com.aidangrabe.studentapp.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.aidangrabe.studentapp.R;
import com.aidangrabe.studentapp.ui.TimeEditText;

/**
 * Created by aidan on 07/01/15.
 *
 */
public class NewClassFragment extends Fragment {

    private TimeEditText mFromEditText, mToEditText;
    private Button mSaveButton, mCancelButton;

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

        mFromEditText = new TimeEditText(getActivity(), fromText, savedInstanceState);
        mToEditText = new TimeEditText(getActivity(), toText, savedInstanceState);

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
        Toast.makeText(getActivity(), "Saving!", Toast.LENGTH_SHORT).show();
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
}
