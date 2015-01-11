package com.aidangrabe.common.games.minesweeper;

import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

/**
 * Created by aidan on 11/01/15.
 *
 */
public class MineSweeperButtonView extends LinearLayout {

    private GameButton[][] mButtons;
    private LinearLayout[] mRows;
    private ButtonMaker mButtonMaker;
    private GameClickListener mGameClickListener;

    public interface ButtonMaker {
        public GameButton makeButton();
        public String getMineText();
    }

    public interface GameClickListener {
        public void onGameButtonClick(GameButton button);
    }

    public MineSweeperButtonView(Context context) {
        this(context, null);
    }

    public MineSweeperButtonView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mButtonMaker = new ButtonMaker() {
            @Override
            public GameButton makeButton() {
                return new GameButton(getContext());
            }

            @Override
            public String getMineText() {
                return "X";
            }
        };
        mGameClickListener = new GameClickListener() {
            @Override
            public void onGameButtonClick(GameButton button) {}
        };
    }

    public final OnClickListener mOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            GameButton button = (GameButton) v;
            mGameClickListener.onGameButtonClick(button);
        }
    };

    public class GameButton extends Button {
        public Point position;
        public GameButton(Context context) {
            super(context);
            position = new Point(0, 0);
        }
    }

    public void setGameSize(int width, int height) {

        setOrientation(VERTICAL);

        mButtons = new GameButton[height][width];
        mRows = new LinearLayout[height];
        for (int y = 0; y < height; y++) {

            LinearLayout ll = new LinearLayout(getContext());
            ll.setOrientation(HORIZONTAL);

            for (int x = 0; x < width; x++) {
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT);
                lp.weight = 1;

                GameButton button = mButtonMaker.makeButton();
                button.position.set(x, y);
                button.setText(" ");
                button.setOnClickListener(mOnClickListener);
                mButtons[x][y] = button;

                ll.addView(button);
                button.setLayoutParams(lp);
            }

            mRows[y] = ll;
            addView(ll);
        }

    }

    public GameButton getButton(int x, int y) {
        return mButtons[x][y];
    }

    public void setButtonMaker(ButtonMaker maker) {
        mButtonMaker = maker;
    }

    public void setGameClickListener(GameClickListener listener) {
        mGameClickListener = listener;
    }

}
