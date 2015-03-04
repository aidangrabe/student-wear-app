package com.aidangrabe.studentapp.activities.games;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.aidangrabe.common.activities.BaseMobileActivity;
import com.aidangrabe.studentapp.R;
import com.aidangrabe.studentapp.fragments.GameMenuFragment;

/**
 * Created by aidan on 02/02/15.
 * This Activity shows a menu for selecting a game to play
 */
public class GameMenuActivity extends BaseMobileActivity {

    private final String FRAG_TAG = "game-menu-fragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        setTitle(getResources().getString(R.string.games_title));

        // try find the fragment first
        Fragment frag = getSupportFragmentManager().findFragmentByTag(FRAG_TAG);
        if (frag == null) {
            frag = new GameMenuFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.container, frag, FRAG_TAG).commit();
        }

    }
}
