package com.aidangrabe.common.util;

import android.content.Context;
import android.view.View;
import android.view.animation.OvershootInterpolator;

/**
 * Created by aidan on 17/02/15.
 * This class implements some simple, re-useable animations
 */
public class MyAnimations {

    public static void animateFab(Context context, View fab) {
        fab.setTranslationY(ScreenUtils.getHeight(context));
        fab.animate()
                .setDuration(700)
                .setInterpolator(new OvershootInterpolator(1f))
                .translationY(0)
                .setStartDelay(300)
                .start();
    }

}
