package com.aidangrabe.phonecontroller.views;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.aidangrabe.phonecontroller.controllers.Controller;

/**
 * Created by aidan on 21/01/15.
 * This class draws a controller and handles the controller's touch events, passing them to the
 * controller
 */
public class ControllerView extends View {

    private Controller mController;

    public ControllerView(Context context, AttributeSet attrs) {
        super(context, attrs);

        // create a default, useless controller
        mController = new Controller() {
            @Override
            public void onDraw(Canvas canvas) {}
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        };

    }

    public void setController(Controller controller) {

        mController = controller;
        setOnTouchListener(mController);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mController.onDraw(canvas);

    }

}
