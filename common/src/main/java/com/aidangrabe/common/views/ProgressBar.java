package com.aidangrabe.common.views;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

/**
 * Created by aidan on 02/02/15.
 *
 */
public class ProgressBar extends View {

    private Paint mPaint;
    private int mEmptyColor, mFillColor;
    private int mWidth, mHeight;
    private float mProgress;

    public ProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mProgress = 0.5f;

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mWidth = w;
        mHeight = h;

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // draw the background
        mPaint.setColor(mEmptyColor);
        canvas.drawRect(0, 0, mWidth, mHeight, mPaint);

        // draw the progress
        mPaint.setColor(mFillColor);
        canvas.drawRect(0, 0, mWidth * mProgress, mHeight, mPaint);

    }

    public float getProgress() {
        return mProgress;
    }

    public void setProgress(float progress, boolean animated) {

        Log.d("D", "setProgress");

        if (animated) {
            Log.d("D", "animating!");
            ValueAnimator animator = ValueAnimator.ofFloat(mProgress, progress);
            animator.setDuration(1500)
                    .addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            mProgress = (float) animation.getAnimatedValue();
                            invalidate();
                        }
                    });
            animator.setInterpolator(new DecelerateInterpolator(3f));
            animator.start();
        } else {
            mProgress = progress;
        }

        invalidate();

    }

    public int getFillColor() {
        return mFillColor;
    }

    public void setFillColor(int fillColor) {
        mFillColor = fillColor;
    }

    public int getEmptyColor() {
        return mEmptyColor;
    }

    public void setEmptyColor(int emptyColor) {
        mEmptyColor = emptyColor;
    }
}
