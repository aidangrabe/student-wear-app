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
    private Food mFood;

    private final TimerTask mTimerTask = new TimerTask() {
        @Override
        public void run() {
            gameTick();
        }
    };

    public interface GameListener {
        public void onGameTick(Snake[] snakes);
        public void onSnakeFeed(Snake snake, Food food);
    }

    public SnakeController() {

        mTimer = new Timer();
        mFood = new Food();
        mSnakes = new Snake[0];

    }

    public void gameTick() {

        // move the snakes
        for (int i = 0; i < mSnakes.length; i++) {
            Snake snake = mSnakes[i];
            snake.move();

            // check for snake feeding
            if (snake.getPosition().x == mFood.position.x && snake.getPosition().y == mFood.position.y) {
                // TODO: handle multiple players
                mSnakes[0].grow();

                mListener.onSnakeFeed(snake, mFood);
            }

        }

        // notify the listener of the event
        if (mListener != null) {
            mListener.onGameTick(mSnakes);
        }

    }

    public Food getFood() {
        return mFood;
    }

    public Snake[] getSnakes() {
        return mSnakes;
    }

    public Snake getSnake(int player) {
        if (player >= 0 && player < mSnakes.length) {
            return mSnakes[player];
        }
        return null;
    }

    public void move(int player, Snake.Dir dir) {
        if (player > 0 && player < mSnakes.length) {
            mSnakes[player].move(dir);
        }
    }

    public void setGameListener(GameListener listener) {
        mListener = listener;
    }

    public void start(int numPlayers) {

        mTimer.schedule(mTimerTask, TimeUnit.SECONDS.toMillis(1), 250);

        mSnakes = new Snake[numPlayers];
        for (int i = 0; i < numPlayers; i++) {
            mSnakes[i] = new Snake();
        }

    }

}
