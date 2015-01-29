package com.aidangrabe.studentapp.activities;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.ImageView;

import com.aidangrabe.common.SharedConstants;
import com.aidangrabe.studentapp.R;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.Asset;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataItemBuffer;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

import java.io.InputStream;
import java.util.Collection;
import java.util.HashSet;

/**
 * Created by aidan on 28/01/15.
 * This Activity displays a static image of a Map region by asking the mobile app
 * for a Bitmap
 */
public class MapActivity extends Activity implements DataApi.DataListener, GoogleApiClient.ConnectionCallbacks {

    private ImageView mImageView;
    private GoogleApiClient mGoogleApiClient;
    private Collection<Node> mNodes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_map);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .build();
        mImageView = (ImageView) findViewById(R.id.image_view);
        mNodes = new HashSet<>();

    }

    /**
     * Ask the mobile app for the map Bitmap
     */
    private void requestMap() {
        for (Node node : mNodes) {
            Wearable.MessageApi.sendMessage(mGoogleApiClient, node.getId(), SharedConstants.Wearable.REQUEST_MAP, null);
        }
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
        Wearable.DataApi.getDataItems(mGoogleApiClient).setResultCallback(new ResultCallback<DataItemBuffer>() {
            @Override
            public void onResult(DataItemBuffer dataItems) {
                for (DataItem dataItem : dataItems) {
                    if (dataItem.getUri().getPath().equals(SharedConstants.Wearable.REQUEST_MAP)) {
                        DataMapItem dataMapItem = DataMapItem.fromDataItem(dataItem);
                        loadBitmapFromAsset(dataMapItem.getDataMap().getAsset("map"));
                    }
                }
                dataItems.release();
            }
        });
    }

    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {

        for (DataEvent event : dataEvents) {
            if (event.getDataItem().getUri().getPath().equals(SharedConstants.Wearable.REQUEST_MAP)) {
                Logd("Map Asset received");
                DataMapItem dataMapItem = DataMapItem.fromDataItem(event.getDataItem());
                loadBitmapFromAsset(dataMapItem.getDataMap().getAsset("map"));
            }
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

                // set the new image on the main thread
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        mImageView.setImageBitmap(mapBitmap);
                    }
                });
            }
        });
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

}
