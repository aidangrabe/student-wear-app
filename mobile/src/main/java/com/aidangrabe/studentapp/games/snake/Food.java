package com.aidangrabe.studentapp.games.snake;

import android.graphics.Point;

/**
 * Created by aidan on 14/01/15.
 * Food for the Snake to eat
 */
public class Food {
    public Point position;
    private int mSize;

    public Food() {
        position = new Point(0, 0);
        mSize = 16;
    }

    /**
     * Make the Food jump to a random position within the given boundary of
     * width x height
     * @param width the max width of the new position
     * @param height the max height of the new position
     */
    public void jumpRandomly(int width, int height) {

        position.set((int) (Math.random() * width), (int) (Math.random() * height));
        snap();

    }

    // snap the food into a grid of it's size
    public void snap() {
        // set x and y to the nearest (rounded down) multiple of mSize
        // this looks pointless but works because we are dividing by an integer, not a float
        position.x = position.x / mSize * mSize;
        position.y = position.y / mSize * mSize;
    }

    public int getSize() {
        return mSize;
    }

    public void setSize(int size) {
        mSize = size;
    }

}
