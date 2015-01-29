package com.aidangrabe.studentapp.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
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
    private Bitmap mArrowLeft, mArrowRight, mArrowDown, mArrowUp;
    private Rect mArrowRectSrc, mArrowRectDst;

    private final OnTouchListener mOnTouchListener = new OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            Dir dir = getDirection(event.getX(), event.getY());
            if (event.getAction() == MotionEvent.ACTION_DOWN && mListener != null) {
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

        setOnTouchListener(mOnTouchListener);

        mLinePaint = new Paint();
        mLinePaint.setColor(Color.WHITE);
        mLinePaint.setStrokeWidth(3);

        // create one Bitmap from resources, the rest are just rotated
        mArrowDown = BitmapFactory.decodeResource(getResources(), android.R.drawable.arrow_down_float);
        mArrowLeft = rotateBitmap(mArrowDown, 90);
        mArrowUp = rotateBitmap(mArrowLeft, 90);
        mArrowRight = rotateBitmap(mArrowUp, 90);

        int size = Math.max(mArrowDown.getWidth(), mArrowDown.getHeight());
        mArrowRectSrc = new Rect(0, 0, size, size);
        mArrowRectDst = new Rect(0, 0, 64, 64);

    }

    private Bitmap rotateBitmap(Bitmap orig, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(orig, 0, 0, orig.getWidth(), orig.getHeight(), matrix, true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawColor(Color.BLACK);

        // diagonal lines
        canvas.drawLine(0, 0, canvas.getWidth(), canvas.getHeight(), mLinePaint);
        canvas.drawLine(canvas.getWidth(), 0, 0, canvas.getHeight(), mLinePaint);

        int w = mArrowRectDst.width();
        int h = mArrowRectDst.height();

        // draw the arrows
        int margin = 16;

        // left arrow
        mArrowRectDst.offsetTo(margin, getHeight() / 2 - h / 2);
        canvas.drawBitmap(mArrowLeft, mArrowRectSrc, mArrowRectDst, mLinePaint);

        // right arrow
        mArrowRectDst.offsetTo(getWidth() - w - margin, getHeight() / 2 - h / 2);
        canvas.drawBitmap(mArrowRight, mArrowRectSrc, mArrowRectDst, mLinePaint);

        // up arrow
        mArrowRectDst.offsetTo(getWidth() / 2 - mArrowRectDst.width() / 2, margin);
        canvas.drawBitmap(mArrowUp, mArrowRectSrc, mArrowRectDst, mLinePaint);

        // down arrow
        mArrowRectDst.offsetTo(getWidth() / 2 - mArrowRectDst.width() / 2, getHeight() - h - margin);
        canvas.drawBitmap(mArrowDown, mArrowRectSrc, mArrowRectDst, mLinePaint);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mWidth = w;
        mHeight = h;

    }

    private Dir getDirection(float x, float y) {

        float deltaX, deltaY;

        deltaX = x > mWidth / 2 ? mWidth - x : - x;
        deltaY = y > mHeight / 2 ? mHeight - y : - y;

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
