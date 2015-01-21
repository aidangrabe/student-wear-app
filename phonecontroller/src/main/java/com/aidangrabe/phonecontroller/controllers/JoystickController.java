package com.aidangrabe.phonecontroller.controllers;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by aidan on 21/01/15.
 *
 */
public class JoystickController extends Controller {

    private Paint mPaint;
    private Point mControllerCenter;

    public JoystickController() {

        mControllerCenter = new Point(0, 0);

        mPaint = new Paint();
        mPaint.setColor(Color.BLACK);

    }

    @Override
    public void onDraw(Canvas canvas) {

        canvas.drawColor(Color.WHITE);

        // draw the controller
        canvas.drawLine(0, 0, canvas.getWidth(), canvas.getHeight(), mPaint);
        canvas.drawLine(canvas.getWidth(), 0, 0, canvas.getHeight(), mPaint);

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        float direction = -1;
        mControllerCenter.set(v.getWidth() / 2, v.getHeight() / 2);

        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                direction = getDirection(event.getX(), event.getY());
                Log.d("D", "Direction: " + direction);
                break;
        }

        if (direction >= 0) {
            mCallback.onDirectionChanged(direction);
        }

        // refresh the view
//        v.invalidate();

        return false;
    }

    private float getDirection(float x, float y) {

        float deltaX, deltaY;

//        deltaX = x < mControllerCenter.x ? mControllerCenter.x - x : x - mControllerCenter.x;
//        deltaY = y < mControllerCenter.y ? mControllerCenter.y - y : y - mControllerCenter.y;
        deltaX = x - mControllerCenter.x;
        deltaY = y - mControllerCenter.y;

        Log.d("D", String.format("Touch: (%.1f, %.1f)", x, y));
        Log.d("D", String.format("\tCenter: (%d, %d)", mControllerCenter.x, mControllerCenter.y));
        Log.d("D", String.format("\tDelta: (%.1f, %.1f)", deltaX, deltaY));

        if (Math.abs(deltaX) > Math.abs(deltaY)) {
            // horizontal
            return deltaX > 0 ? 0 : 180;
        } else {
            // vertical
            return deltaY > 0 ? 270 : 90;
        }

    }

}
