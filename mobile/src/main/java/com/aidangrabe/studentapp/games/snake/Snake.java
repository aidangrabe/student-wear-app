package com.aidangrabe.studentapp.games.snake;

import android.graphics.Point;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by aidan on 13/01/15.
 *
 */
public class Snake {

    // create a Map for determining the opposite directions
    private static final Map<Dir, Dir> OPPOSITE_DIRECTIONS = new HashMap<>();
    static {
        OPPOSITE_DIRECTIONS.put(Dir.LEFT, Dir.RIGHT);
        OPPOSITE_DIRECTIONS.put(Dir.RIGHT, Dir.LEFT);
        OPPOSITE_DIRECTIONS.put(Dir.DOWN, Dir.UP);
        OPPOSITE_DIRECTIONS.put(Dir.UP, Dir.DOWN);
    }

    // array used to buffer parts to remove so they can be removed in bulk
    private ArrayList<BodyPart> mPartsToRemove;
    private Point mPosition;
    private boolean alive;

    private int mLength;

    // size of the body segments
    private int mBodySize;
    private ArrayList<BodyPart> mBodyParts;

    private Dir mCurrentDir;

    private int mColor;

    public static enum Dir {
        LEFT, RIGHT, UP, DOWN
    }

    public static class BodyPart {
        public boolean alive;
        public Point position;
        private int life;

        public BodyPart(Point position, int life) {
            this.position = new Point(position);
            this.life = life;
            alive = true;
        }

        public void decay() {
            life--;
            if (life <= 0) {
                alive = false;
            }
        }

    }

    public Snake() {

        mPosition = new Point(16, 16);
        mLength = 10;
        mBodySize = 16;
        mBodyParts = new ArrayList<>();
        mPartsToRemove = new ArrayList<>();
        mCurrentDir = Dir.RIGHT;
        mColor = 0;
        alive = true;

    }

    /**
     * Check if the Snake collides with a given Snake
     * @param snake the snake to check for a collision with
     * @return true if a collision has occurred
     */
    public boolean collision(Snake snake) {

        for (BodyPart bodyPart : snake.getBodyParts()) {
            if (mPosition.x == bodyPart.position.x
                    && mPosition.y == bodyPart.position.y) {
                return true;
            }
        }
        return false;

    }

    public List<BodyPart> getBodyParts() {
        return mBodyParts;
    }

    public Point getPosition() {
        return mPosition;
    }

    public void grow() {
        grow(2);
    }

    /**
     * Increase the length of the Snake by a given amount
     * @param amount the amount to increase the Snake's length by
     */
    public void grow(int amount) {
        mLength += amount;
    }

    /**
     * Move the Snake in it's current direction
     */
    public void move() {
        move(mCurrentDir);
    }

    /**
     * Move the Snake in a given direction
     * @param direction the direction to move the Snake in
     */
    public void move(Dir direction) {

        switch (direction) {
            case LEFT:
                move(-mBodySize, 0);
                break;
            case RIGHT:
                move(mBodySize, 0);
                break;
            case UP:
                move(0, -mBodySize);
                break;
            case DOWN:
                move(0, mBodySize);
                break;
        }

        mCurrentDir = direction;

    }

    private void makeBodyPart() {

        BodyPart bodyPart = new BodyPart(mPosition, mLength);
        mBodyParts.add(bodyPart);

    }

    /**
     * Move the Snake's position by a given offset
     * @param x amount of pixels to move horizontally
     * @param y amount of pixels to move vertically
     */
    private void move(int x, int y) {

        removeDeadBodyParts();

        // don't move the Snake if it's dead
        if (!alive) {
            return;
        }
        makeBodyPart();
        mPosition.offset(x, y);

    }

    /**
     * Get rid of body parts which have expired
     */
    private void removeDeadBodyParts() {

        mPartsToRemove.clear();
        for (BodyPart bodyPart : mBodyParts) {
            bodyPart.decay();
            if (!bodyPart.alive) {
                mPartsToRemove.add(bodyPart);
            }
        }
        mBodyParts.removeAll(mPartsToRemove);

    }

    /**
     * Move the Snake to the opposite side of the screen if it is outside the screen
     * @param width the width of the screen
     * @param height the height of the screen
     */
    public void wrap(int width, int height) {

        // wrap the Snake
        if (mPosition.x > width - 16) {
            mPosition.x = 0;
        } else if (mPosition.x < 0) {
            mPosition.x = width - 16;
        } else if (mPosition.y > height) {
            mPosition.y = 16;
        } else if (mPosition.y < 16) {
            mPosition.y = height;
        }

    }

    public void setDirection(Dir newDirection) {

        // don't allow the Snake to move in the opposite direction
        Dir wrongDirection = OPPOSITE_DIRECTIONS.get(mCurrentDir);
        if (newDirection != wrongDirection) {
            mCurrentDir = newDirection;
        }

    }

    public void setColor(int color) {
        mColor = color;
    }

    public int getColor() {
        return mColor;
    }

    public void setPosition(Point position) {
        mPosition = position;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public boolean isAlive() {
        return alive;
    }

}
