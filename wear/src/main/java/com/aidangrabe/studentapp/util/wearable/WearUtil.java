package com.aidangrabe.studentapp.util.wearable;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

/**
 * Created by aidan on 10/01/15.
 *
 */
public class WearUtil implements DataApi.DataListener {

    private GoogleApiClient mGoogleApiClient;

    public WearUtil(Context context) {

        mGoogleApiClient = new GoogleApiClient.Builder(context).addApi(Wearable.API).build();
        Wearable.DataApi.addListener(mGoogleApiClient, this);

    }

    public void connect() {
        mGoogleApiClient.connect();
    }

    public void disconnect() {
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
        Log.d("DEBUG", "WE HAVE LIFTOFF");
    }
}
