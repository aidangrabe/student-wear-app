package com.aidangrabe.studentapp.activities.games;

import com.aidangrabe.studentapp.R;
import com.aidangrabe.studentapp.activities.MenuActivity;
import com.aidangrabe.studentapp.activities.games.lightsout.LightsOutActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aidan on 14/01/15.
 * An Activity to show the list of games
 */
public class GameMenuActivity extends MenuActivity {

    @Override
    public List<MenuItem> onCreateMenu() {
        List<MenuItem> items = new ArrayList<>();
        items.add(new MenuItem(R.string.game_menu_controller, DirectionalControllerActivity.class, R.drawable.ic_games));
        items.add(new MenuItem(R.string.game_menu_minesweeper, MineSweeperActivity.class, R.drawable.ic_mine));
        items.add(new MenuItem(R.string.game_menu_lightsout, LightsOutActivity.class, R.drawable.ic_launcher));
        return items;
    }
}
