package com.aidangrabe.studentapp.games.snake;

import android.graphics.Color;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by aidan on 13/01/15.
 * This class controls the Snake game. It updates the game's logic and controls
 * all the Snakes and Food for the game
 */
public class SnakeController {

    // how frequently to update the game logic
    private static final int MILLIS_PER_FRAME = 250;

    // the game logic Timer
    private Timer mTimer;
    private Snake[] mSnakes;
    private GameListener mListener;

    // the dimensions of the playing area. This should be set by the view displaying the game
    private int mHeight, mWidth;

    // the food instance that the Snakes will try to eat
    private Food mFood;

    // whether the game has started or not
    private boolean mStarted;

    // the colors for each Snake
    private final int[] mSnakeColors = new int[] {
            Color.BLUE,
            Color.GREEN,
            Color.MAGENTA,
            Color.YELLOW
    };

    // the game logic timer task
    private final TimerTask mTimerTask = new TimerTask() {
        @Override
        public void run() {
            gameTick();
        }
    };

    /**
     * A callback interface for listening to the game's events
     */
    public interface GameListener {
        /**
         * Called when the game logic is updated
         * @param snakes the game's snakes
         */
        public void onGameTick(Snake[] snakes);

        /**
         * Called when a Snake eats a Food
         * @param snake the Snake that ate the Food
         * @param food the Food that was eaten
         */
        public void onSnakeFeed(Snake snake, Food food);
    }

    public SnakeController() {

        mTimer = new Timer();
        mFood = new Food();
        mSnakes = new Snake[0];
        mStarted = false;
        mWidth = 0;
        mHeight = 0;

    }

    /**
     * Update the game logic
     */
    public void gameTick() {

        // move the snakes
        for (Snake snake : mSnakes) {
            snake.move();

            // check for snake feeding
            if (snake.getPosition().x == mFood.position.x && snake.getPosition().y == mFood.position.y) {
                snake.grow();

                mListener.onSnakeFeed(snake, mFood);
            }

            // wrap the snake if it's off the screen
            snake.wrap(mWidth, mHeight);

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

    /**
     * Get a player's snake
     * @param player the number of the player
     * @return the snake owned by the given player
     */
    public Snake getSnake(int player) {
        if (player >= 0 && player < mSnakes.length) {
            return mSnakes[player];
        }
        return null;
    }

    /**
     * Move a player/snake in a given direction
     * @param player the player/snake to move
     * @param dir the direction to move the snake in
     */
    public void move(int player, Snake.Dir dir) {
        if (player > 0 && player < mSnakes.length) {
            mSnakes[player].setDirection(dir);
        }
    }

    /**
     * Set the listener who will receive the game events
     * @param listener the listener who will receive the game's events
     */
    public void setGameListener(GameListener listener) {
        mListener = listener;
    }

    /**
     * Start the game
     * @param numPlayers the number of players, ie. the number of snakes to make
     */
    public void start(int numPlayers) {

        // don't start the game multiple times
        if (mStarted) {
            return;
        }

        mStarted = true;
        mTimer.schedule(mTimerTask, MILLIS_PER_FRAME, MILLIS_PER_FRAME);

        // create the snakes
        mSnakes = new Snake[numPlayers];
        for (int i = 0; i < numPlayers; i++) {
            mSnakes[i] = new Snake();
            mSnakes[i].setColor(mSnakeColors[i]);
        }

    }

    /**
     * Set the size of the playing area
     * @param width the width of the playing area
     * @param height the height of the playing area
     */
    public void setSize(int width, int height) {
        mWidth = width;
        mHeight = height;
    }

}
