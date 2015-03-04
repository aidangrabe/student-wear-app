package com.aidangrabe.studentapp.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.aidangrabe.common.SharedConstants;
import com.aidangrabe.common.activities.BaseMobileActivity;
import com.aidangrabe.common.model.Module;
import com.aidangrabe.common.model.Result;
import com.aidangrabe.common.util.MyAnimations;
import com.aidangrabe.common.util.WearUtils;
import com.aidangrabe.common.views.SimpleGraph;
import com.aidangrabe.studentapp.R;
import com.aidangrabe.studentapp.fragments.dialogs.NewResultDialogFragment;
import com.google.android.gms.common.api.GoogleApiClient;
import com.melnykov.fab.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * Created by aidan on 03/02/15.
 * This Activity displays the results for a given module
 */
public class ModuleResultsActivity extends BaseMobileActivity implements NewResultDialogFragment.OnSaveListener, AdapterView.OnItemLongClickListener {

    // argument/extras keys
    public static final String ARG_MODULE_ID = "module_id";

    // multipliers for sorting
    private static final int ORDER_ASC = 1;
    private static final int ORDER_DESC = -1;

    // order for the results to be listed
    // asc = oldest first, desc = newest first
    private static final int RESULT_ORDER = ORDER_ASC;

    private ArrayAdapter<Result> mAdapter;
    private Module mModule;
    private NewResultDialogFragment mNewResultDialog;
    private ListView mListView;
    private SimpleGraph mGraph;
    private SimpleDateFormat mDateFormat;
    private AlertDialog mConfirmationDialog;

    // for sorting the results
    private final Comparator<Result> mResultComparator = new Comparator<Result>() {
        @Override
        public int compare(Result lhs, Result rhs) {
            return (int) (rhs.getCreateTime() - lhs.getCreateTime() * RESULT_ORDER);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_module_results);

        parseArgs();

        // error out if no Module was found
        if (mModule == null) {
            Toast.makeText(this, "Module not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        mDateFormat = new SimpleDateFormat("MMM d");
        final Date date = new Date();

        // create the list's adapter
        mAdapter = new ArrayAdapter<Result>(this, android.R.layout.simple_list_item_1) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = LayoutInflater.from(ModuleResultsActivity.this).inflate(R.layout.listitem_result, parent, false);
                }

                Result result = getItem(position);
                date.setTime(result.getCreateTime());
                ((TextView) convertView.findViewById(R.id.date_text)).setText(
                        mDateFormat.format(date));
                ((TextView) convertView.findViewById(R.id.grade_text)).setText(
                        String.format("%.0f%%", result.getGrade() * 100));

                return convertView;

            }
        };

        setupListView();
        getResults();

        addFabToView((ViewGroup) getWindow().getDecorView().findViewById(android.R.id.content));

    }

    // parse the arguments for this Activity
    private void parseArgs() {

        Bundle args = getIntent().getExtras();
        if (args != null) {
            long moduleId = args.getLong(ARG_MODULE_ID, -1);
            mModule = Module.findById(Module.class, moduleId);
        }

    }

    private void setupListView() {

        View graphView = LayoutInflater.from(this).inflate(R.layout.simple_graph, null);
        mGraph = (SimpleGraph) graphView.findViewById(R.id.simple_graph);

        mListView = (ListView) findViewById(R.id.list_view);
        mListView.addHeaderView(graphView);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemLongClickListener(this);

    }

    private void getResults() {

        List<Result> results = mModule.listAllResults();

        Collections.sort(results, mResultComparator);

        mAdapter.clear();
        mAdapter.addAll(results);
        mAdapter.notifyDataSetChanged();

        mGraph.clearValues();
        for (Result result : results) {
            mGraph.addValue(result.getGrade());
        }

        mGraph.animateValues();

    }

    private void addFabToView(ViewGroup viewGroup) {

        // inflate the FAB view and add it to the given ViewGroup
        View view = LayoutInflater.from(this)
                .inflate(R.layout.fab_new_layout, viewGroup, true);
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.attachToListView(mListView);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNewResultDialog = new NewResultDialogFragment();
                mNewResultDialog.setOnSaveListener(ModuleResultsActivity.this);
                mNewResultDialog.show(getFragmentManager(), "fragment");
            }
        });
        MyAnimations.animateFab(this, fab);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // make sure the dialogs are properly dismissed to prevent leaking views
        if (mNewResultDialog != null) {
            mNewResultDialog.dismiss();
        }
        if (mConfirmationDialog != null) {
            mConfirmationDialog.dismiss();
            mConfirmationDialog.cancel();
        }

    }

    @Override
    public void onSaveResult(Result result) {

        result.setModule(mModule);
        result.save();

        syncResults();

        getResults();

        if (mNewResultDialog != null) {
            mNewResultDialog.dismiss();
        }

    }

    private void syncResults() {
        // sync the new list of results for the Wearable
        GoogleApiClient apiClient = WearUtils.makeClient(this, null, null);
        WearUtils.putDataItem(apiClient, WearUtils.listToDataMap("results", mModule.listAllResults()),
                SharedConstants.Wearable.DATA_PATH_RESULTS(mModule));
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

        // delete the long clicked result
        final Result result = (Result) mListView.getItemAtPosition(position);
        if (result == null) {
            return false;
        }
        mConfirmationDialog = new AlertDialog.Builder(this).setMessage("Are you sure you want to delete this result?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        result.delete();
                        getResults();
                        syncResults();
                    }
                })
                .setNegativeButton("No", null)
                .show();

        return false;
    }
}
