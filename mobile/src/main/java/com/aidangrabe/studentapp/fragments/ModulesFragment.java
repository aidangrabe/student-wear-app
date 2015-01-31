package com.aidangrabe.studentapp.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.aidangrabe.common.model.Module;
import com.aidangrabe.studentapp.fragments.base.MenuFragment;

import java.util.ArrayList;

/**
 * Created by aidan on 31/01/15.
 *
 */
public class ModulesFragment extends MenuFragment {

    private static final String TAG_NEW_MODULE = "add-module";

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

        getAllModules();
        setListAdapter(mAdapter);
        refreshList();

    }

    private void showConfirmationDialog(DialogInterface.OnClickListener okListener) {

        new AlertDialog.Builder(getActivity())
                .setMessage("Are you sure you want to delete this module?")
                .setPositiveButton("Yes", okListener)
                .setNegativeButton("No", null)
                .show();

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View view,
                                           int position, long id) {
                MenuItem item = mAdapter.getItem(position);
                if (!TAG_NEW_MODULE.equals(item.getTag())) {
                    final Module module = mModules.get(position - 1);
                    showConfirmationDialog( new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            module.delete();
                            getAllModules();
                            refreshList();
                        }
                    });
                }
                return true;
            }
        });

    }

    private void getAllModules() {

        mModules.clear();
        mModules.addAll(Module.listAll(Module.class));

    }

    private void refreshList() {
        mAdapter.clear();

        MenuItem addModuleMenuItem = new MenuItem("+ New Module", null, null, null);
        addModuleMenuItem.setTag(TAG_NEW_MODULE);
        mAdapter.add(addModuleMenuItem);

        for (Module module : mModules) {
            mAdapter.add(new MenuItem(module.getName(), null, null, null));
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        MenuItem item = mAdapter.getItem(position);

        if (TAG_NEW_MODULE.equals(item.getTag())) {
            Log.d("D", "Adding a new item");
            Module module = new Module("CS4614");
            module.save();
            getAllModules();
            refreshList();
        }

    }
}
