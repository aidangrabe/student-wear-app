package com.aidangrabe.studentapp.activities.games.minesweeper;

import android.os.Bundle;
import android.support.v4.util.Pair;
import android.widget.Toast;

import com.aidangrabe.studentapp.activities.GridActivity;

import java.util.ArrayList;

/**
 * Created by aidan on 01/11/14.
 * MineSweeper game
 */
public class MineSweeperActivity extends GridActivity {

    private MineSweeper mMineSweeperController;
    private MineSweeperButton.MineSweeperOnClickListener mButtonListener = new MineSweeperButton.MineSweeperOnClickListener() {
        @Override
        public void onSingleClick(MineSweeperButton button) {
            button.toggleFlag();
        }

        @Override
        public void onDoubleClick(MineSweeperButton button) {
            selectButton(button);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mCols = 5;
        mRows = 5;
        super.onCreate(savedInstanceState);

        mMineSweeperController = new MineSweeper(mCols, mRows, 4);
    }

    @Override
    protected void onButtonCreated(GridActivity.GridButton button) {
        MineSweeperButton msButton = (MineSweeperButton) button;
        msButton.setListener(mButtonListener);
    }

    @Override
    protected void onButtonClicked(GridButton button) {
        MineSweeperButton mb = (MineSweeperButton) button;
        mb.onClick();
    }

    public void selectButton(MineSweeperButton button) {
        Pair<Integer, Integer> pos = button.getPosition();

        MineSweeper.MineSweeperTile tile = mMineSweeperController.selectTile(pos.first, pos.second);
        switch (tile) {
            default:
            case EMPTY:
                button.setEnabled(false);
                ArrayList<MineSweeper.MineSweeperTile> neighbours = mMineSweeperController.getNeighbours(button.getGridX(), button.getGridY());
                int numTouching = button.numMinesTouching(neighbours);
                if (numTouching > 0) {
                    button.setText("" + numTouching);
                } else {
                    for (int i = -1; i < 2; i += 2) {
                        MineSweeperButton neighbourButtonX = (MineSweeperButton) getButton(button.getGridX() + i, button.getGridY());
                        MineSweeperButton neighbourButtonY = (MineSweeperButton) getButton(button.getGridX(), button.getGridY() + i);
                        if (neighbourButtonX != null && neighbourButtonX.isEnabled()) {
                            selectButton(neighbourButtonX);
                        }
                        if (neighbourButtonY != null && neighbourButtonY.isEnabled()) {
                            selectButton(neighbourButtonY);
                        }
                    }
                }
                break;
            case MINE:
                Toast.makeText(getApplicationContext(), "BOOM!", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    protected GridButton createButton(int x, int y) {
        return new MineSweeperButton(getApplicationContext(), x, y);
    }
}
