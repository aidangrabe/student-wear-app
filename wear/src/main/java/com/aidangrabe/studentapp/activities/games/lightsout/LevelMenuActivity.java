package com.aidangrabe.studentapp.activities.games.lightsout;

import android.content.Intent;

import com.aidangrabe.studentapp.R;
import com.aidangrabe.studentapp.activities.MenuActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aidan on 30/01/15.
 * This Activity shows a list of levels and starts the LightsOutActivity with the that level
 */
public class LevelMenuActivity extends MenuActivity {

    private static final String LEVEL_1 =
              "XOOOX"
            + "OOOOO"
            + "OOXOO"
            + "OOOOO"
            + "XOOOX";

    private static final String LEVEL_2 =
              "XOXOX"
            + "OOOOO"
            + "XOOOX"
            + "OOOOO"
            + "XOXOX";

    private static final String LEVEL_3 =
              "OXOXO"
            + "OOOOO"
            + "OXXXO"
            + "OOOOO"
            + "OXOXO";

    private static final String LEVEL_4 =
              "OOXOO"
            + "OXOXO"
            + "OOXOO"
            + "XOOOX"
            + "OOXOO";

    private static final String LEVEL_5 =
              "XXOXX"
            + "OOOOO"
            + "OXOXO"
            + "OXXXO"
            + "XXOXX";

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

    @Override
    protected void onIntentCreated(MenuItem item, Intent intent) {

        String levelCode = LEVEL_1;
        if (item.getTitle().equals("Level 2")) {
            levelCode = LEVEL_2;
        } else if (item.getTitle().equals("Level 3")) {
            levelCode = LEVEL_3;
        } else if (item.getTitle().equals("Level 4")) {
            levelCode = LEVEL_4;
        } else if (item.getTitle().equals("Level 5")) {
            levelCode = LEVEL_5;
        }
        intent.putExtra(LightsOutActivity.ARG_LEVEL, levelCode);

    }
}
