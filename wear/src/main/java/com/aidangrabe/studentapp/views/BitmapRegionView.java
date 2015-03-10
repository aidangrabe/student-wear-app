package com.aidangrabe.studentapp.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.support.v4.view.GestureDetectorCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by aidan on 10/03/15.
 * A View that shows a large Bitmap that can be scrolled
 */
public class BitmapRegionView extends View implements View.OnTouchListener {

    private Bitmap mBitmap;
    private int mChunkWidth, mChunkHeight;
    private int mNumChunksX, mNumChunksY;
    private Paint mPaint;
    private Point mCurrentPosition;
    private Rect mSrcRect, mDstRect;
    private GestureDetectorCompat mGestureDetector;
    private OnLongClickListener mLongClickListener;

    private final GestureDetector.OnGestureListener mGestureDetectorListener = new GestureDetector.SimpleOnGestureListener() {

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            mLongClickListener.onLongClick(null);
            super.onLongPress(e);
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

            float absX = Math.abs(velocityX);
            float absY = Math.abs(velocityY);

            int newPositionX = mCurrentPosition.x;
            int newPositionY = mCurrentPosition.y;

            // horizontal
            if (absX > absY) {
                Log.d("D", "Horizontal");
                newPositionX = velocityX > 0 ? decPositionX() : incPositionX();
            }
            // vertical
            else {
                Log.d("D", "Vertical");
                newPositionY = velocityY > 0 ? decPositionY() : incPositionY();
            }

            setPosition(newPositionX, newPositionY);

            Log.d("D", String.format("New position (%d, %d)", newPositionX, newPositionY));

            return false;
        }

    };

    private int incPositionX() {
        return Math.min(mCurrentPosition.x + 1, mNumChunksX - 1);
    }

    private int incPositionY() {
        return Math.min(mCurrentPosition.y + 1, mNumChunksY - 1);
    }

    private int decPositionX() {
        return Math.max(mCurrentPosition.x - 1, 0);
    }

    private int decPositionY() {
        return Math.max(mCurrentPosition.y - 1, 0);
    }

    public BitmapRegionView(Context context) {
        super(context);
    }

    public BitmapRegionView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BitmapRegionView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void init(Context context, Bitmap bitmap, int numHorChunks, int numVertChunks) {

        mBitmap = bitmap;

        mNumChunksX = numHorChunks;
        mNumChunksY = numVertChunks;

        mChunkWidth = bitmap.getWidth() / numHorChunks;
        mChunkHeight = bitmap.getHeight() / numVertChunks;

        mSrcRect = new Rect(0, 0, mChunkWidth, mChunkHeight);
        mDstRect = new Rect(0, 0, getWidth(), getHeight());
        mPaint = new Paint();
        mPaint.setColor(Color.WHITE);
        mGestureDetector = new GestureDetectorCompat(context, mGestureDetectorListener);

        setOnTouchListener(this);

        mCurrentPosition = new Point(0, 0);

        setPosition(0, 0);

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        Log.d("D", "Touch event");
        return mGestureDetector.onTouchEvent(event) || super.onTouchEvent(event);
    }

    private void setPosition(int col, int row) {

        mCurrentPosition.set(col, row);

        mSrcRect.offsetTo(col * mChunkWidth, row * mChunkHeight);

        invalidate();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mBitmap != null) {
            canvas.drawBitmap(mBitmap, mSrcRect, mDstRect, mPaint);
        }

    }

    public void setLongClickListener(OnLongClickListener longClickListener) {
        mLongClickListener = longClickListener;
    }

}
