package com.aidangrabe.studentapp.activities.games.lightsout;

import com.aidangrabe.studentapp.R;
import com.aidangrabe.studentapp.activities.MenuActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aidan on 30/01/15.
 * This Activity shows a list of levels and starts the LightsOutActivity with the that level
 */
public class LevelMenuActivity extends MenuActivity {

    @Override
    public List<MenuItem> onCreateMenu() {

        List<MenuItem> items = new ArrayList<>();

        items.add(new MenuItem("Level 1", LightsOutActivity.class, R.drawable.ic_games));
        items.add(new MenuItem("Level 2", LightsOutActivity.class, R.drawable.ic_games));
        items.add(new MenuItem("Level 3", LightsOutActivity.class, R.drawable.ic_games));
        items.add(new MenuItem("Level 4", LightsOutActivity.class, R.drawable.ic_games));
        items.add(new MenuItem("Level 5", LightsOutActivity.class, R.drawable.ic_games));

        return items;

    }
}
