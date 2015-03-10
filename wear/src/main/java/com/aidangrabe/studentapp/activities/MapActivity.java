package com.aidangrabe.studentapp.activities;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.View;

import com.aidangrabe.common.SharedConstants;
import com.aidangrabe.common.util.WearUtils;
import com.aidangrabe.studentapp.R;
import com.aidangrabe.studentapp.views.BitmapRegionView;
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
    private BitmapRegionView mBitmapRegionView;
    private GestureDetector mGestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_map);

        mBitmapRegionView = (BitmapRegionView) findViewById(R.id.bitmap_region_view);
        mBitmapRegionView.setLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                finish();
                return false;
            }
        });

        mGoogleApiClient = WearUtils.makeClient(this, this, null);
        mNodes = new HashSet<>();

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

                onMapReceived(mapBitmap);

            }
        });
    }

    private void onMapReceived(Bitmap bitmap) {

        mBitmapRegionView.init(this, bitmap, 3, 3);

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

}
