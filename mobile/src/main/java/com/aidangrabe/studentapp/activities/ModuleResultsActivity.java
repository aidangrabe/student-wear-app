package com.aidangrabe.studentapp.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.aidangrabe.common.model.Module;
import com.aidangrabe.common.model.Result;
import com.aidangrabe.studentapp.R;
import com.aidangrabe.studentapp.fragments.dialogs.NewResultDialogFragment;
import com.melnykov.fab.FloatingActionButton;

/**
 * Created by aidan on 03/02/15.
 *
 */
public class ModuleResultsActivity extends ActionBarActivity implements NewResultDialogFragment.OnSaveListener {

    public static final String ARG_MODULE_ID = "module_id";

    private Module mModule;
    private NewResultDialogFragment mNewResultDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_module_results);

        Bundle args = getIntent().getExtras();
        if (args != null) {
            long moduleId = args.getLong(ARG_MODULE_ID, -1);
            mModule = Module.findById(Module.class, moduleId);
        }

        // error out if no Module was found
        if (mModule == null) {
            Toast.makeText(this, "Module not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        addFabToView((ViewGroup) getWindow().getDecorView().findViewById(android.R.id.content));

    }

    private void addFabToView(ViewGroup viewGroup) {

        // inflate the FAB view and add it to the given ViewGroup
        View view = LayoutInflater.from(this)
                .inflate(R.layout.fab_new_layout, viewGroup, true);
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNewResultDialog = new NewResultDialogFragment();
                mNewResultDialog.setOnSaveListener(ModuleResultsActivity.this);
                mNewResultDialog.show(getFragmentManager(), "fragment");
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mNewResultDialog != null) {
            mNewResultDialog.dismiss();
        }

    }

    @Override
    public void onSaveResult(Result result) {

        result.setModule(mModule);
        result.save();

        if (mNewResultDialog != null) {
            mNewResultDialog.dismiss();
        }

    }
}
