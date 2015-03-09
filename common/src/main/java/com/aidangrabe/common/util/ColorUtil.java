package com.aidangrabe.common.util;

import android.graphics.Color;

/**
 * Created by aidan on 09/03/15.
 * Utility class for colours
 */
public class ColorUtil {

    /**
     * Darken a color by the given percentage
     * @param color the color to darken
     * @param percent the amount to darken the color by. In range 0-1
     * @return the darkened colour
     */
    public static int darken(int color, float percent) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[2] *= 1 - percent;
        return Color.HSVToColor(hsv);
    }

}
