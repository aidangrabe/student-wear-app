package com.aidangrabe.studentapp.activities;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.wearable.view.FragmentGridPagerAdapter;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import com.aidangrabe.common.SharedConstants;
import com.aidangrabe.common.util.WearUtils;
import com.aidangrabe.studentapp.R;
import com.aidangrabe.studentapp.fragments.MapFragment;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.Asset;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

import java.io.InputStream;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

/**
 * Created by aidan on 28/01/15.
 * This Activity displays a static image of a Map region by asking the mobile app
 * for a Bitmap
 */
public class MapActivity extends Activity implements DataApi.DataListener, GoogleApiClient.ConnectionCallbacks {

    private static final int GRID_SIZE_X = 3;
    private static final int GRID_SIZE_Y = 3;

    private GoogleApiClient mGoogleApiClient;
    private Collection<Node> mNodes;
    private Bitmap[][] mMapGrid;
    private int mCurrentRow, mCurrentCol;
    private GestureDetector mGestureDetector;
    private ImageView[] mImageViews;
    private View mRootView;
    private boolean mAnimating;

    private GestureDetector.OnGestureListener mGestureListener = new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

            int newRow = mCurrentRow;
            int newCol = mCurrentCol;

            if (Math.abs(velocityY) > Math.abs(velocityX)) {
                newRow += velocityY > 0
                        ? -1 : 1;
                Log.d("D", "New row: " + mCurrentRow + " -> " + newRow);
            } else {
                newCol += velocityX > 0
                        ? -1 : 1;
                Log.d("D", "New col: " + mCurrentCol + " -> " + newCol);
            }

            animateImage(newCol, newRow);

            return true;
        }
    };

    private void animateImage(final int x, final int y) {

        // don't animate if we're already animating, or if there is no change in x/y
        if (mAnimating || (x == mCurrentCol && y == mCurrentRow)) {
            return;
        }

        Log.d("D", "Animating!");

        int destX = 0;
        int destY = 0;
        int destX2 = 0;
        int destY2 = 0;
        if (x != mCurrentCol) {
            destX = x > mCurrentCol ? -320 : 640;
            destX2 = x > mCurrentCol ? 640 : -320;
        }
        if (y != mCurrentRow) {
            destY = y > mCurrentRow ? -320 : 640;
            destY2 = y > mCurrentRow ? 640 : -320;
        }

        TranslateAnimation anim1 = new TranslateAnimation(0, destX, 0, destY);
        anim1.setDuration(200);
        anim1.setInterpolator(new LinearInterpolator());
        anim1.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                mAnimating = true;
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mImageViews[0].setX(0);
                mImageViews[0].setImageBitmap(getBitmapAt(x, y));
                mCurrentCol = getX(x);
                mCurrentRow = getY(y);
                mAnimating = false;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        TranslateAnimation anim2 = new TranslateAnimation(destX2, 0, destY2, 0);
        anim2.setDuration(200);
        anim2.setInterpolator(new LinearInterpolator());
        anim2.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                mImageViews[1].setImageBitmap(getBitmapAt(x, y));
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        mImageViews[0].startAnimation(anim1);
        mImageViews[1].startAnimation(anim2);

    }

    private int getX(int x) {
        return Math.min(GRID_SIZE_X - 1, Math.max(0, x));
    }

    private int getY(int y) {
        return Math.min(GRID_SIZE_Y - 1, Math.max(0, y));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_map);

        mRootView = findViewById(R.id.frame_layout);

        mImageViews = new ImageView[2];
        mImageViews[0] = (ImageView) findViewById(R.id.image_view);
        mImageViews[1] = (ImageView) findViewById(R.id.image_view2);

        mCurrentRow = GRID_SIZE_X / 2;
        mCurrentCol = GRID_SIZE_Y / 2;

        mGestureDetector = new GestureDetector(this, mGestureListener);
        mGoogleApiClient = WearUtils.makeClient(this, this, null);
        mNodes = new HashSet<>();

        mAnimating = false;

    }

    /**
     * Ask the mobile app for the map Bitmap
     */
    private void requestMap() {
        WearUtils.sendMessage(mGoogleApiClient, mNodes, SharedConstants.Wearable.REQUEST_MAP, null);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGoogleApiClient.connect();
        Wearable.DataApi.addListener(mGoogleApiClient, this);
    }

    @Override
    protected void onPause() {
        Wearable.DataApi.removeListener(mGoogleApiClient, this);
        mGoogleApiClient.disconnect();
        super.onPause();
    }

    /**
     * Get the map image currently synced to the DataApi
     */
    public void getMapImage() {
        WearUtils.getSyncedItems(mGoogleApiClient, SharedConstants.Wearable.REQUEST_MAP, new WearUtils.GetDataListener() {
            @Override
            public void onDataReceived(List<DataMap> dataMaps) {
                for (DataMap dataMap : dataMaps) {
                    loadBitmapFromAsset(dataMap.getAsset("map"));
                }
            }
        });
    }

    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {

        List<DataMap> dataMaps = WearUtils.getDataMaps(dataEvents, SharedConstants.Wearable.REQUEST_MAP);
        for (DataMap dataMap : dataMaps) {
            loadBitmapFromAsset(dataMap.getAsset("map"));
        }

    }

    /**
     * Using the given Asset, creates a Bitmap and sets the ImageView imageBitmap
     * @param asset the Asset to to convert to a Bitmap
     */
    public void loadBitmapFromAsset(Asset asset) {
        if (asset == null) {
            throw new IllegalArgumentException("Asset must be non-null");
        }
        // convert asset into a file descriptor and block until it's ready
        Wearable.DataApi.getFdForAsset(
                mGoogleApiClient, asset).setResultCallback(new ResultCallback<DataApi.GetFdForAssetResult>() {
            @Override
            public void onResult(DataApi.GetFdForAssetResult getFdForAssetResult) {
                InputStream assetInputStream = getFdForAssetResult.getInputStream();
                final Bitmap mapBitmap = BitmapFactory.decodeStream(assetInputStream);

                mMapGrid = chunkMap(GRID_SIZE_X, GRID_SIZE_Y, mapBitmap);
                onMapReceived();

            }
        });
    }

    private void onMapReceived() {

        mImageViews[0].setImageBitmap(getBitmapAt(mCurrentCol, mCurrentRow));

        getWindow().getDecorView().findViewById(android.R.id.content).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mGestureDetector.onTouchEvent(event);
                return true;
            }
        });
    }

    private Bitmap getBitmapAt(int x, int y) {
        x = Math.min(GRID_SIZE_X - 1, Math.max(0, x));
        y = Math.min(GRID_SIZE_Y - 1, Math.max(0, y));
        Log.d("D", String.format("map at: (%d, %d)", x, y));
        return mMapGrid[x][y];
    }

    /**
     * Split the given map into <numX>x<numY> grid
     */
    private Bitmap[][] chunkMap(int numX, int numY, Bitmap bitmap) {
        int w = bitmap.getWidth() / numX;
        int h = bitmap.getHeight() / numY;
        Bitmap[][] grid = new Bitmap[numX][numY];
        for (int y = 0; y < numY; y++) {
            for (int x = 0; x < numX; x++) {
                grid[x][y] = Bitmap.createBitmap(bitmap, w * x, h * y, w, h);
            }
        }
        return grid;
    }

    @Override
    public void onConnected(Bundle bundle) {
        Wearable.NodeApi.getConnectedNodes(mGoogleApiClient).setResultCallback(new ResultCallback<NodeApi.GetConnectedNodesResult>() {
            @Override
            public void onResult(NodeApi.GetConnectedNodesResult getConnectedNodesResult) {
                mNodes.addAll(getConnectedNodesResult.getNodes());
                onNodesConnected();
            }
        });
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    public void onNodesConnected() {
        requestMap();
        getMapImage();
    }

    private void Logd(String message) {
        Log.d("D", message);
    }

    private class Adapter extends FragmentGridPagerAdapter {

        public Adapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getFragment(int row, int col) {
            MapFragment frag = new MapFragment();
            frag.setBitmap(mMapGrid[row][col]);
            return frag;
        }

        @Override
        public int getRowCount() {
            return GRID_SIZE_Y;
        }

        @Override
        public int getColumnCount(int row) {
            return GRID_SIZE_X;
        }

    }
}
