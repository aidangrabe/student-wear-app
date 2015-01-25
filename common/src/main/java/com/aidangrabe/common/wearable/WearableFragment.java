package com.aidangrabe.common.wearable;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by aidan on 25/01/15.
 *
 */
public class WearableFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, MessageApi.MessageListener,
        DataApi.DataListener {

    private GoogleApiClient mApiClient;
    private Collection<Node> mNodes;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mApiClient = new GoogleApiClient.Builder(getActivity())
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        mNodes = new ArrayList<>();

    }

    @Override
    public void onResume() {
        super.onResume();

        mApiClient.connect();

    }

    @Override
    public void onPause() {
        super.onPause();

        mApiClient.disconnect();

        // remove the communication listeners
        Wearable.DataApi.removeListener(mApiClient, this);
        Wearable.MessageApi.removeListener(mApiClient, this);

    }

    @Override
    public void onConnected(Bundle bundle) {

        // add the communication listeners
        Wearable.MessageApi.addListener(mApiClient, this);
        Wearable.DataApi.addListener(mApiClient, this);

        // fetch the connected nodes
        Wearable.NodeApi.getConnectedNodes(mApiClient).setResultCallback(new ResultCallback<NodeApi.GetConnectedNodesResult>() {
            @Override
            public void onResult(NodeApi.GetConnectedNodesResult getConnectedNodesResult) {
                mNodes.addAll(getConnectedNodesResult.getNodes());
            }
        });

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {

    }

    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {

    }

    public boolean isConnected() {
        return mApiClient.isConnected();
    }

    public GoogleApiClient getGoogleApiClient() {
        return mApiClient;
    }

    public void setGoogleApiClient(GoogleApiClient mApiClient) {
        this.mApiClient = mApiClient;
    }

    public Collection<Node> getConnectedNodes() {
        return mNodes;
    }
}
