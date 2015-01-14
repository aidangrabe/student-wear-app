package com.aidangrabe.studentapp.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by aidan on 13/01/15.
 *
 */
public class DirectionalControllerView extends View {

    private Paint mLinePaint;
    private int mWidth, mHeight;
    private DirectionalControllerListener mListener;
    private final OnTouchListener mOnClickListener = new OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            Dir dir = getDirection(event.getX(), event.getY());
            if (mListener != null) {
                mListener.onDirectionPressed(dir);
            }
            return true;
        }
    };

    public static enum Dir {
        UP, DOWN, LEFT, RIGHT
    }

    public interface DirectionalControllerListener {
        public void onDirectionPressed(Dir direction);
    }

    public DirectionalControllerView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mLinePaint = new Paint();
        mLinePaint.setColor(Color.WHITE);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawColor(Color.BLACK);

        // diagonal lines
        canvas.drawLine(0, 0, canvas.getWidth(), canvas.getHeight(), mLinePaint);
        canvas.drawLine(canvas.getWidth(), 0, 0, canvas.getHeight(), mLinePaint);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mWidth = w;
        mHeight = h;

    }

    private Dir getDirection(float x, float y) {

        float deltaX = mWidth / 2 - x;
        float deltaY = mHeight / 2 - y;

        if (Math.abs(deltaX) < Math.abs(deltaY)) {
            // horizontal
            return deltaX > 0 ? Dir.RIGHT : Dir.LEFT;
        } else {
            // vertical
            return deltaY > 0 ? Dir.DOWN : Dir.UP;
        }

    }

    public void setDirectionalControllerListener(DirectionalControllerListener listener) {
        mListener = listener;
    }

}
