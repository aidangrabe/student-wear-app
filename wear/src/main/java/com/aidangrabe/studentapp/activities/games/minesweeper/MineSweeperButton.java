package com.aidangrabe.studentapp.activities.games.minesweeper;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;

import com.aidangrabe.studentapp.R;
import com.aidangrabe.studentapp.activities.GridActivity;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by aidan on 01/11/14.
 *
 */
public class MineSweeperButton extends GridActivity.GridButton {
    private static final int DOUBLE_TAP_INTERVAL = 400;

    private MineSweeperOnClickListener mListener;
    private Timer mTimer;
    private boolean mTimerIsStarted;
    private MineSweeperButton mInstance;
    private boolean isFlagged;
    private Drawable defaultBackground;

    public interface MineSweeperOnClickListener {
        public void onSingleClick(MineSweeperButton button);
        public void onDoubleClick(MineSweeperButton button);
    }

    public MineSweeperButton(Context context, int x, int y) {
        super(context, x, y);
        mTimerIsStarted = false;
        mInstance = this;
        isFlagged = false;
        defaultBackground = getBackground();
    }

    public int numMinesTouching(ArrayList<MineSweeper.MineSweeperTile> neighbours) {
        int numMines = 0;
        for (MineSweeper.MineSweeperTile tile : neighbours) {
            if (tile == MineSweeper.MineSweeperTile.MINE) {
                numMines++;
            }
        }
        return numMines;
    }

    public void onClick() {
        if (mListener == null) {
            return;
        }

        if (!mTimerIsStarted) {
            mTimerIsStarted = true;
            mTimer = new Timer();
            mTimer.schedule(new DoubleTapTimerTask(), DOUBLE_TAP_INTERVAL);
        } else {
            mTimerIsStarted = false;
            mTimer.cancel();
            mTimer.purge();
            mListener.onDoubleClick(this);
        }
    }

    public void toggleFlag() {
        isFlagged = !isFlagged;
        if (isFlagged) {
            setBackgroundResource(R.drawable.ic_flag);
        } else {
            setBackground(defaultBackground);
        }
    }

    public void setListener(MineSweeperOnClickListener listener) {
        mListener = listener;
    }

    public class DoubleTapTimerTask extends TimerTask {
        @Override
        public void run() {
            mTimerIsStarted = false;
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    // in case the button is flagged, reset the background drawable
                    mInstance.setBackground(defaultBackground);
                    mListener.onSingleClick(mInstance);
                }
            });
        }
    }

}
