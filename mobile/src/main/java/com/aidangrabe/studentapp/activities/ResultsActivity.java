package com.aidangrabe.studentapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.aidangrabe.common.activities.BaseMobileActivity;
import com.aidangrabe.common.model.Module;
import com.aidangrabe.common.views.ProgressBar;
import com.aidangrabe.studentapp.R;

/**
 * Created by aidan on 02/02/15.
 *
 */
public class ResultsActivity extends BaseMobileActivity implements AdapterView.OnItemClickListener {

    private ArrayAdapter<Module> mAdapter;
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_results);
        setTitle("Results");

        mListView = (ListView) findViewById(R.id.list_view);

        mAdapter = new ArrayAdapter<Module>(this, android.R.layout.simple_list_item_1) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = convertView;
                if (view == null) {
                    view = LayoutInflater.from(getContext()).inflate(R.layout.list_item_module_average, parent, false);
                }

                Module module = getItem(position);

                TextView tv = (TextView) view.findViewById(R.id.title_text);
                tv.setText(module.getName());

                ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
                progressBar.setFillColor(getResources().getColor(R.color.progress_bar_fill));
                progressBar.setEmptyColor(getResources().getColor(R.color.progress_bar_empty));
                progressBar.setProgress(0, false);
                progressBar.setProgress(module.getResultAverage(), true);

                return view;
            }
        };
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);

        mAdapter.addAll(Module.listAll(Module.class));
        mAdapter.notifyDataSetChanged();

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Module module = mAdapter.getItem(position);
        Intent intent = new Intent(this, ModuleResultsActivity.class);
        intent.putExtra(ModuleResultsActivity.ARG_MODULE_ID, module.getId());
        startActivity(intent);

    }
}
