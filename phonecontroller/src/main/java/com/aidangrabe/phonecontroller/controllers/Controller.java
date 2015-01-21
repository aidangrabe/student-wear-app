package com.aidangrabe.phonecontroller.controllers;

import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by aidan on 21/01/15.
 *
 */
public abstract class Controller implements View.OnTouchListener {

    protected ControllerCallbacks mCallback;

    public interface ControllerCallbacks {
        public void onDirectionChanged(float direction);
    }

    public abstract void onDraw(Canvas canvas);
    public abstract boolean onTouch(View v, MotionEvent event);

    public void setCallback(ControllerCallbacks callback) {
        mCallback = callback;
    }

}
