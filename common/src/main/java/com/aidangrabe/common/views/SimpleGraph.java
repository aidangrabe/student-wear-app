package com.aidangrabe.common.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aidan on 03/02/15.
 *
 */
public class SimpleGraph extends View {

    private int mWidth, mHeight;
    private float mMin, mMax;
    private List<Float> mValues;
    private Paint mPaint, mPointPaint, mFillPaint;
    private Path mPath;

    public SimpleGraph(Context context, AttributeSet attrs) {
        super(context, attrs);

        mValues = new ArrayList<>();
        mPaint = new Paint();
        mPaint.setColor(Color.parseColor("#64B5F6"));
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(6);
        mPaint.setStyle(Paint.Style.STROKE);

        mFillPaint = new Paint();
        mFillPaint.setStyle(Paint.Style.FILL);
        mFillPaint.setColor(Color.parseColor("#CFD8DC"));

        mPointPaint = new Paint();
        mPointPaint.setColor(Color.parseColor("#4DB6AC"));
        mPointPaint.setAntiAlias(true);
        mPointPaint.setStyle(Paint.Style.FILL);

        mPath = new Path();

    }

    public void addValue(float value) {
        mValues.add(value);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mValues.size() == 0) {
            // nothing to do!
            return;
        }

        float deltaX = mWidth / (float) mValues.size();
        mPath.reset();

        int i = 0;
        for (float value : mValues) {
            mPath.lineTo(deltaX * i, mHeight - mHeight * value);
            i++;
        }
        mPath.lineTo(mWidth, mHeight - mHeight * mValues.get(mValues.size() - 1));

        canvas.drawPath(mPath, mPaint);
//        canvas.drawPath(mPath, mFillPaint);

        i = 0;
        for (float value : mValues) {
            canvas.drawCircle(deltaX * i, mHeight - mHeight * value, 7, mPointPaint);
            i++;
        }

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mWidth = w;
        mHeight = h;

    }

    public void clearValues() {
        mValues.clear();
    }

}
