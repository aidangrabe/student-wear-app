package com.aidangrabe.studentapp.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.aidangrabe.common.model.Module;
import com.aidangrabe.common.model.Result;
import com.aidangrabe.common.views.SimpleGraph;
import com.aidangrabe.studentapp.R;
import com.aidangrabe.studentapp.fragments.dialogs.NewResultDialogFragment;
import com.melnykov.fab.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by aidan on 03/02/15.
 *
 */
public class ModuleResultsActivity extends ActionBarActivity implements NewResultDialogFragment.OnSaveListener {

    public static final String ARG_MODULE_ID = "module_id";

    private ArrayAdapter<Result> mAdapter;
    private Module mModule;
    private NewResultDialogFragment mNewResultDialog;
    private ListView mListView;
    private SimpleGraph mGraph;
    private SimpleDateFormat mDateFormat;

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

        mDateFormat = new SimpleDateFormat("MMM d");
        final Date date = new Date();

        mAdapter = new ArrayAdapter<Result>(this, android.R.layout.simple_list_item_1) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
//                View view = super.getView(position, convertView, parent);
                if (convertView == null) {
                    convertView = LayoutInflater.from(ModuleResultsActivity.this).inflate(R.layout.listitem_result, parent, false);
                }

                Result result = getItem(position);
                date.setTime(result.getCreateTime());
                ((TextView) convertView.findViewById(R.id.date_text)).setText(mDateFormat.format(date));
                ((TextView) convertView.findViewById(R.id.grade_text)).setText(String.format("%.0f%%", result.getGrade() * 100));


                return convertView;

            }
        };

        setupListView();
        getResults();

        addFabToView((ViewGroup) getWindow().getDecorView().findViewById(android.R.id.content));

    }

    private void setupListView() {

        View graphView = LayoutInflater.from(this).inflate(R.layout.simple_graph, null);
        mGraph = (SimpleGraph) graphView.findViewById(R.id.simple_graph);

        mListView = (ListView) findViewById(R.id.list_view);
        mListView.addHeaderView(graphView);
        mListView.setAdapter(mAdapter);

    }

    private void getResults() {

        List<Result> results = mModule.listAllResults();

        mAdapter.clear();
        mAdapter.addAll(results);
        mAdapter.notifyDataSetChanged();

        mGraph.clearValues();
        for (Result result : results) {
            mGraph.addValue(result.getGrade());
        }
        mGraph.invalidate();

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

        getResults();

        if (mNewResultDialog != null) {
            mNewResultDialog.dismiss();
        }

    }
}
