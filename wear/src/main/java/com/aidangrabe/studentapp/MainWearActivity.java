package com.aidangrabe.studentapp;

import com.aidangrabe.studentapp.activities.FindMyPhoneActivity;
import com.aidangrabe.studentapp.activities.MapActivity;
import com.aidangrabe.studentapp.activities.MenuActivity;
import com.aidangrabe.studentapp.activities.ResultsActivity;
import com.aidangrabe.studentapp.activities.ToDoListActivity;
import com.aidangrabe.studentapp.activities.games.GameMenuActivity;

import java.util.ArrayList;
import java.util.List;

public class MainWearActivity extends MenuActivity {

    @Override
    public List<MenuActivity.MenuItem> onCreateMenu() {
        List<MenuItem> items = new ArrayList<>();
        items.add(new MenuItem(R.string.menu_todo_list, ToDoListActivity.class, R.drawable.ic_todo));
        items.add(new MenuItem(R.string.menu_results, ResultsActivity.class, R.drawable.ic_results));
        items.add(new MenuItem(R.string.menu_games, GameMenuActivity.class, R.drawable.ic_games));
        items.add(new MenuItem(R.string.menu_map, MapActivity.class, R.drawable.ic_map));
        items.add(new MenuItem(R.string.find_my_phone, FindMyPhoneActivity.class, R.drawable.ic_phone));
        return items;
    }

}
