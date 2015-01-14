package com.aidangrabe.studentapp.games.snake.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.View;

import com.aidangrabe.studentapp.games.snake.Snake;
import com.aidangrabe.studentapp.games.snake.SnakeController;

/**
 * Created by aidan on 13/01/15.
 *
 */
public class SnakeCanvasView extends View implements SnakeController.GameListener {

    private SnakeController mGame;
    private Snake[] mSnakes;
    private Rect mSnakeRect;
    private Paint mSnakePaint;

    public SnakeCanvasView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mGame = new SnakeController(1);
        mGame.setGameListener(this);
        mGame.start();
        mSnakes = mGame.getSnakes();
        mSnakeRect = new Rect(0, 0, 16, 16);
        mSnakePaint = new Paint();
        mSnakePaint.setColor(Color.BLACK);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // clear the canvas
        canvas.drawColor(Color.WHITE);

        // draw the snakes
        for (Snake snake : mSnakes) {

            drawPart(canvas, snake.getPosition());
            for (Snake.BodyPart part : snake.getBodyParts()) {
                drawPart(canvas, part.position);
            }

        }

    }

    // draw a part of the snake's body at the given Point
    public void drawPart(Canvas canvas, Point point) {

        mSnakeRect.offsetTo(point.x, point.y);
        canvas.drawRect(mSnakeRect, mSnakePaint);

    }

    public Snake[] getSnakes() {
        return mSnakes;
    }

    // called every time the game logic is updated.
    @Override
    public void onGameTick(Snake[] snakes) {

        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                invalidate();
            }
        });

    }

}
