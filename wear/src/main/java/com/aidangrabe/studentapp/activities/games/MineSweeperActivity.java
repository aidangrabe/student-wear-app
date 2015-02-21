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

    // the size of the board
    private static final int BOARD_COLS = 5;
    private static final int BOARD_ROWS = 5;

    private static final int NUM_BOMBS = 3;

    private MineSweeperController game;
    private MineSweeperButtonView mGameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        game = new MineSweeperController(BOARD_COLS, BOARD_ROWS, NUM_BOMBS);
        game.setListener(this);

        mGameView = new MineSweeperButtonView(this);
        mGameView.setGameSize(BOARD_COLS, BOARD_ROWS);
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
        if (touchingBombs == 0) {
            // no need to show 0 if no bombs nearby
            mGameView.getButton(x, y).setText("");
        } else {
            mGameView.getButton(x, y).setText(Integer.toString(touchingBombs));
        }
    }
}
