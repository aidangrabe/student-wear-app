package com.aidangrabe.common.wearable;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

/**
 * Created by aidan on 10/01/15.
 *
 */
public class WearUtil implements DataApi.DataListener, MessageApi.MessageListener, GoogleApiClient.ConnectionCallbacks {

    private GoogleApiClient mGoogleApiClient;
    private GoogleApiClient.ConnectionCallbacks mConnectionCallbacks;
    private DataApi.DataListener mDataListener;
    private MessageApi.MessageListener mMessageListener;

    public WearUtil(Context context) {

        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .build();

    }

    public void connect() {
        mGoogleApiClient.connect();
    }

    public void disconnect() {
        Wearable.DataApi.removeListener(mGoogleApiClient, this);
        Wearable.MessageApi.removeListener(mGoogleApiClient, this);
        mGoogleApiClient.disconnect();
    }

    public void sendMessage(final String path, final String message) {
        sendMessage(path, message.getBytes());
    }

    public void sendMessage(final String path, DataMap message) {

        byte[] data = message == null ? null : message.toByteArray();
        sendMessage(path, data);

    }

    public void sendMessage(final String path, final byte[] data) {

        Log.d("DEBUG", "WearUtils: Sending message");
        Wearable.NodeApi.getConnectedNodes(mGoogleApiClient)
                .setResultCallback(new ResultCallback<NodeApi.GetConnectedNodesResult>() {
                                       @Override
                                       public void onResult(NodeApi.GetConnectedNodesResult getConnectedNodesResult) {
                                           Log.d("DEBUG", "First callback");
                                           for (Node node : getConnectedNodesResult.getNodes()) {
                                               Wearable.MessageApi.sendMessage(mGoogleApiClient, node.getId(), path,
                                                       data).setResultCallback(getSendMessageResultCallback());
                                           }
                                       }
                                   }
                );

    }

    private ResultCallback<MessageApi.SendMessageResult> getSendMessageResultCallback() {
        return new ResultCallback<MessageApi.SendMessageResult>() {
            @Override
            public void onResult(MessageApi.SendMessageResult sendMessageResult) {
                Log.d("DEBUG", "Message sent successfully");
                // notify listeners
            }
        };
    }

    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {
        if (mDataListener != null) {
            mDataListener.onDataChanged(dataEvents);
        }
    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        Log.d("DEBUG", "onMessageReceive" + messageEvent.getPath());

        if (mMessageListener != null) {
            mMessageListener.onMessageReceived(messageEvent);
        }

    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.d("DEBUG", "GoogleApiClient connected");
        Wearable.DataApi.addListener(mGoogleApiClient, this);
        Wearable.MessageApi.addListener(mGoogleApiClient, this);
        if (mConnectionCallbacks != null) {
            mConnectionCallbacks.onConnected(bundle);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        if (mConnectionCallbacks != null) {
            mConnectionCallbacks.onConnectionSuspended(i);
        }
    }

    public WearUtil setConnectionCallbacks(GoogleApiClient.ConnectionCallbacks callbacks) {
        mConnectionCallbacks = callbacks;
        return this;
    }

    public WearUtil setDataListener(DataApi.DataListener dataListener) {
        mDataListener = dataListener;
        return this;
    }

    public WearUtil setMessageListener(MessageApi.MessageListener messageListener) {
        mMessageListener = messageListener;
        return this;
    }

    public GoogleApiClient getGoogleApiClient() {
        return mGoogleApiClient;
    }

}
