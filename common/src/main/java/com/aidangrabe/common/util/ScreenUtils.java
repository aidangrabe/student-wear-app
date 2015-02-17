package com.aidangrabe.common.util;

import android.content.Context;
import android.graphics.Point;
import android.view.WindowManager;

/**
 * Created by aidan on 17/02/15.
 * Simple Utility class for getting Screen related information
 */
public class ScreenUtils {

    private static Point mSize;

    public static int getHeight(Context context) {
        return getSize(context).y;
    }

    public static int getWidth(Context context) {
        return getSize(context).x;
    }

    /**
     * Get the Screen size
     * @param context the application context
     * @return the dimensions of the screen
     */
    public static Point getSize(Context context) {

        if (mSize == null) {
            mSize = new Point();
            WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            windowManager.getDefaultDisplay().getSize(mSize);
        }

        return mSize;

    }

}
