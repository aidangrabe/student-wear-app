package com.aidangrabe.studentapp.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.aidangrabe.studentapp.R;
import com.aidangrabe.studentapp.activities.games.SnakeActivity;
import com.aidangrabe.studentapp.fragments.base.MenuFragment;

/**
 * Created by aidan on 02/02/15.
 * This Fragment displays a menu for selecting and launching a game
 */
public class GameMenuFragment extends MenuFragment {

    private ArrayAdapter<MenuItem> mAdapter;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAdapter = new ArrayAdapter<MenuItem>(getActivity(), android.R.layout.simple_list_item_1) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                MenuItem menuItem = getItem(position);

                View view = convertView;
                if (view == null) {
                    view = LayoutInflater.from(getActivity()).inflate(R.layout.list_item_main_menu, parent, false);
                }

                ((TextView) view.findViewById(R.id.title_text)).setText(menuItem.getTitle());
                ((TextView) view.findViewById(R.id.description_text)).setText(menuItem.getDescription());
                ((ImageView) view.findViewById(R.id.icon)).setImageBitmap(menuItem.getIcon());

                return view;

            }
        };

        setListAdapter(mAdapter);
        createMenu();

    }

    private void createMenu() {

        mAdapter.add(new MenuItem(R.string.game_menu_snake, R.string.game_menu_snake_description, R.drawable.ic_games, SnakeActivity.class));

        mAdapter.notifyDataSetChanged();

    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        MenuItem menuItem = mAdapter.getItem(position);

        // start the MenuItem's Activity
        Class<? extends Activity> newActivityClass = menuItem.getActivityClass();
        if (newActivityClass != null) {
            Intent intent = new Intent(getActivity(), newActivityClass);
            startActivity(intent);
        }

    }
}
