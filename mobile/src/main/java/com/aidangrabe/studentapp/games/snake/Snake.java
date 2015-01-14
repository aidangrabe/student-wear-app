package com.aidangrabe.studentapp.games.snake;

import android.graphics.Point;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aidan on 13/01/15.
 *
 */
public class Snake {

    private Point mPosition;
    private int mLength;

    // size of the body segments
    private int mBodySize;

    private SnakeListener mListener;
    private ArrayList<BodyPart> mBodyParts;
    private Dir mCurrentDir;

    // array used to buffer parts to remove so they can be removed in bulk
    private ArrayList<BodyPart> mPartsToRemove;

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

    public interface SnakeListener {
        public void onMove(Snake snake);
        public void onCollision(Snake snake);
    }

    public Snake() {

        mPosition = new Point(16, 16);
        mLength = 10;
        mBodySize = 16;
        mBodyParts = new ArrayList<>();
        mPartsToRemove = new ArrayList<>();
        mCurrentDir = Dir.RIGHT;

        mListener = new SnakeListener() {
            @Override
            public void onMove(Snake snake) {}
            @Override
            public void onCollision(Snake snake) {}
        };

    }

    private boolean collision() {

        for (BodyPart bodyPart : mBodyParts) {
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

    public void grow(int amount) {
        mLength += amount;
    }

    public void move() {
        move(mCurrentDir);
    }

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

    private void move(int x, int y) {

        mPosition.offset(x, y);
        removeDeadBodyParts();

        if (collision()) {
            mListener.onCollision(this);
        } else {
            mListener.onMove(this);
        }

        makeBodyPart();

    }

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

    public void setPosition(Point position) {
        mPosition = position;
    }

    public void setSnakeListener(SnakeListener listener) {

        mListener = listener;

    }

}
