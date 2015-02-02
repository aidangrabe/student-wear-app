package com.aidangrabe.studentapp.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.aidangrabe.common.model.Module;
import com.aidangrabe.common.views.ProgressBar;
import com.aidangrabe.studentapp.R;
import com.melnykov.fab.FloatingActionButton;

/**
 * Created by aidan on 02/02/15.
 *
 */
public class ResultsActivity extends ActionBarActivity {

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
                progressBar.setProgress(module.getResultAverage(), true);

                return view;
            }
        };
        mListView.setAdapter(mAdapter);

        mAdapter.addAll(Module.listAll(Module.class));
        mAdapter.notifyDataSetChanged();

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
                // start the NewClassActivity
                // TODO a dialog/screen to create a new Result
//                Intent intent = new Intent(this, NewClassActivity.class);
//                startActivity(intent);
            }
        });

    }

}
