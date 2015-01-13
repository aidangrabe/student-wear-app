package com.aidangrabe.studentapp.games.snake;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

/**
 * Created by aidan on 13/01/15.
 *
 */
public class SnakeController {

    private Timer mTimer;
    private Snake[] mSnakes;
    private GameListener mListener;

    private final TimerTask mTimerTask = new TimerTask() {
        @Override
        public void run() {
            gameTick();
        }
    };

    public interface GameListener {
        public void onGameTick(Snake[] snakes);
    }

    public SnakeController(int numPlayers) {

        mTimer = new Timer();

        mSnakes = new Snake[numPlayers];
        for (int i = 0; i < numPlayers; i++) {
            mSnakes[i] = new Snake();
        }

    }

    public void gameTick() {

        // move the snakes
        for (int i = 0; i < mSnakes.length; i++) {
            mSnakes[i].move();
        }

        // notify the listener of the event
        if (mListener != null) {
            mListener.onGameTick(mSnakes);
        }

    }

    public Snake[] getSnakes() {
        return mSnakes;
    }

    public void setGameListener(GameListener listener) {
        mListener = listener;
    }

    public void start() {

        mTimer.schedule(mTimerTask, TimeUnit.SECONDS.toMillis(1), TimeUnit.SECONDS.toMillis(1));

    }

}
