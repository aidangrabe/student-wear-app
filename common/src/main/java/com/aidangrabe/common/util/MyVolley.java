package com.aidangrabe.common.util;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by aidan on 28/01/15.
 * Simple Volley Singleton wrapper class for a single RequestQueue
 */
public class MyVolley {

    private static RequestQueue mRequestQueue;

    // no new instances
    private MyVolley() {};

    public static RequestQueue getInstance(Context context) {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(context);
        }
        return mRequestQueue;
    }

}
