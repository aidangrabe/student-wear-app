package com.aidangrabe.studentapp.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.aidangrabe.common.model.Module;
import com.aidangrabe.studentapp.R;

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
                View view =  super.getView(position, convertView, parent);

                Module module = getItem(position);

                TextView tv = (TextView) view.findViewById(android.R.id.text1);
                tv.setText(module.getName());

                return view;
            }
        };
        mListView.setAdapter(mAdapter);

        mAdapter.addAll(Module.listAll(Module.class));
        mAdapter.notifyDataSetChanged();

    }

}
