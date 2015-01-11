package com.aidangrabe.studentapp.activities.games;

import android.app.Activity;
import android.os.Bundle;

import com.aidangrabe.common.games.minesweeper.MineSweeperButtonView;
import com.aidangrabe.common.games.minesweeper.MineSweeperController;

/**
 * Created by aidan on 11/01/15.
 * Activity for the Mine Sweeper game
 */
public class MineSweeperActivity extends Activity implements MineSweeperButtonView.GameClickListener, MineSweeperController.MineSweeperListener {

    private MineSweeperController game;
    private MineSweeperButtonView mGameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        game = new MineSweeperController(4, 4, 3);
        game.setListener(this);

        mGameView = new MineSweeperButtonView(this);
        mGameView.setGameSize(4, 4);
        mGameView.setGameClickListener(this);

        setContentView(mGameView);

    }

    @Override
    public void onGameButtonClick(MineSweeperButtonView.GameButton button) {
        game.mineTile(button.position.x, button.position.y);
    }

    @Override
    public void onBombFound(int x, int y) {
        mGameView.getButton(x, y).setText("X");
    }

    @Override
    public void onTileCleared(int x, int y) {
        mGameView.getButton(x, y).setEnabled(false);
    }

    @Override
    public void onBombProximityUpdated(int x, int y, int touchingBombs) {
        mGameView.getButton(x, y).setText(Integer.toString(touchingBombs));
    }
}
