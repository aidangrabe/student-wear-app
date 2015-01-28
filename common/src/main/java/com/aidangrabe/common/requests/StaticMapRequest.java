package com.aidangrabe.common.requests;

import android.graphics.Bitmap;

import com.android.volley.Response;
import com.android.volley.toolbox.ImageRequest;

/**
 * Created by aidan on 28/01/15.
 * This class is a Volley Request object for downloading Static Map images using
 * Google Static Maps API
 */
public class StaticMapRequest extends ImageRequest {

    private static final String ENDPOINT = "http://maps.google.com/maps/api/";

    /**
     * Creates a new static map request using Google Maps Static API
     *
     * @param latitude the latitude of the center
     * @param longitude  the longitude of the center
     * @param zoom the zoom level
     * @param width the width of the returned image
     * @param height the height of the returned image
     * @param listener      Listener to receive the decoded bitmap
     * @param errorListener Error listener, or null to ignore errors
     */
    public StaticMapRequest(double latitude, double longitude, int zoom, int width, int height,
                            Response.Listener<Bitmap> listener, Response.ErrorListener errorListener) {
        super(buildUrl(latitude, longitude, zoom, width, height), listener, 0, 0, null, errorListener);
    }

    /**
     * Build an Static Maps API URL
     * @param latitude the latitude of the center
     * @param longitude  the longitude of the center
     * @param zoom the zoom level
     * @param width the width of the returned image
     * @param height the height of the returned image
     * @return the URL of the API request as a String
     */
    private static String buildUrl(double latitude, double longitude, int zoom, int width, int height) {
        return String.format(ENDPOINT + "staticmap?center=%f,%f&zoom=%d&size=%dx%d&sensor=false",
                latitude, longitude, zoom, width, height);
    }
}
