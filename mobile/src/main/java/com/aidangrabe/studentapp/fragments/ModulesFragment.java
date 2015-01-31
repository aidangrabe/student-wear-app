package com.aidangrabe.studentapp.fragments;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.aidangrabe.common.model.Module;
import com.aidangrabe.studentapp.fragments.base.MenuFragment;

import java.util.ArrayList;

/**
 * Created by aidan on 31/01/15.
 *
 */
public class ModulesFragment extends MenuFragment {

    private ArrayAdapter<MenuItem> mAdapter;
    private ArrayList<Module> mModules;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mModules = new ArrayList<>();
        mAdapter = new ArrayAdapter<MenuItem>(getActivity(), android.R.layout.simple_list_item_1) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);

                MenuItem item = getItem(position);

                TextView tv = (TextView) view.findViewById(android.R.id.text1);
                tv.setText(item.getTitle());

                return view;
            }
        };

        setListAdapter(mAdapter);
        refreshList();

    }

    private void refreshList() {
        mAdapter.clear();
        mAdapter.add(new MenuItem("+ New Module", null, null, null));
        for (Module module : mModules) {
            mAdapter.add(new MenuItem(module.getName(), null, null, null));
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



    }
}
