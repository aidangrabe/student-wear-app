package com.aidangrabe.studentapp.fragments;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.aidangrabe.studentapp.R;

/**
 * Created by aidan on 09/01/15.
 * Fragment to create a new ToDoItem
 */
public class NewToDoItemFragment extends DialogFragment {

    private EditText mTitleEditText;
    private Button mSaveButton;
    private View.OnClickListener mSaveOnClickListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_new_todo_item, container, false);

        mTitleEditText = (EditText) view.findViewById(R.id.new_todo_item);
        mSaveButton = (Button) view.findViewById(R.id.new_todo_save_button);

        if (mSaveOnClickListener != null) {
            mSaveButton.setOnClickListener(mSaveOnClickListener);
        }

        return view;

    }

    public String getTitle() {
        return mTitleEditText.getText().toString();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getDialog().setTitle(R.string.new_todo_item);

    }

    public void setSaveListener(View.OnClickListener onSaveListener) {
        mSaveOnClickListener = onSaveListener;
    }

}
