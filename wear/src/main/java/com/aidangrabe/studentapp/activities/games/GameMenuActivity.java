package com.aidangrabe.studentapp.activities.games;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.aidangrabe.studentapp.R;

/**
 * Created by aidan on 14/01/15.
 *
 */
public class GameMenuActivity extends Activity implements AdapterView.OnItemClickListener {

    private ListView mListView;
    private ArrayAdapter<String> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mListView = new ListView(this);
        setContentView(mListView);

        mListView.setBackgroundColor(Color.WHITE);
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                ((TextView) view.findViewById(android.R.id.text1)).setTextColor(Color.BLACK);
                return view;
            }
        };
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);

        mAdapter.add(getResources().getString(R.string.game_menu_controller));
        mAdapter.add(getResources().getString(R.string.game_menu_minesweeper));
        mAdapter.notifyDataSetChanged();

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        String item = mAdapter.getItem(position);
        Class<? extends Activity> newActivityClass = null;

        if (item.equals(getResources().getString(R.string.game_menu_controller))) {
            newActivityClass = DirectionalControllerActivity.class;
        } else if (item.equals(getResources().getString(R.string.game_menu_minesweeper))) {
            newActivityClass = MineSweeperActivity.class;
        }

        if (newActivityClass != null) {
            Intent intent = new Intent(this, newActivityClass);
            startActivity(intent);
        }

    }
}
