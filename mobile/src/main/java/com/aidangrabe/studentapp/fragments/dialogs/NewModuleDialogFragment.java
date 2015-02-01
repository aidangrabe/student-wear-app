package com.aidangrabe.studentapp.fragments.dialogs;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.aidangrabe.common.model.Module;
import com.aidangrabe.studentapp.R;

/**
 * Created by aidan on 01/02/15.
 * This DialogFragment shows a form for creating a new Module
 */
public class NewModuleDialogFragment extends DialogFragment implements View.OnClickListener {

    private EditText mTitleEditText;
    private OnSaveListener mListener;

    public interface OnSaveListener {
        public void onSaveNewModule(Module module);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_fragment_new_module, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mTitleEditText = (EditText) view.findViewById(R.id.title_text);

        Button saveButton = (Button) view.findViewById(R.id.save_button);
        saveButton.setOnClickListener(this);

        getDialog().setTitle("New Module");

    }

    @Override
    public void onClick(View v) {

        // save the new module
        if (v.getId() == R.id.save_button) {
            String moduleTitle = mTitleEditText.getText().toString().trim();
            if (moduleTitle.length() == 0) {
                Toast.makeText(getActivity(), "Module title must be set", Toast.LENGTH_SHORT).show();
                return;
            }
            Module module = new Module(moduleTitle);
            if (mListener != null) {
                mListener.onSaveNewModule(module);
            }
            dismiss();
        }

    }

    public void setOnSaveListener(OnSaveListener listener) {
        mListener = listener;
    }

}
