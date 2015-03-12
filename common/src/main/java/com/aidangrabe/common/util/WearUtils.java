package com.aidangrabe.common.util;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataItemBuffer;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by aidan on 06/02/15.
 * This class contains many static helper methods for dealing with the
 * GoogleApiClient class
 */
public class WearUtils {

    // Listeners
    public interface NodeListener {
        public void onNodesConnected(List<Node> nodes);
    }

    public interface GetDataListener {
        public void onDataReceived(List<DataMap> dataMaps);
    }

    public interface UriListener {
        public void onUriBuilt(Uri uri);
    }

    /**
     * Create a simple GoogleApiClient with connection callbacks
     * @param context the context to create this client with
     * @param connectionCallbacks the callbacks for when connection events occur. null for no callbacks
     * @param onConnectionFailedListener the callback for when connection fails. null for no callbacks
     * @return
     */
    public static GoogleApiClient makeClient(Context context,
                                             GoogleApiClient.ConnectionCallbacks connectionCallbacks,
                                             GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener) {
        GoogleApiClient client =  new GoogleApiClient.Builder(context)
                .addApi(Wearable.API)
                .build();
        if (connectionCallbacks != null) {
            client.registerConnectionCallbacks(connectionCallbacks);
        }
        if (onConnectionFailedListener != null) {
            client.registerConnectionFailedListener(onConnectionFailedListener);
        }
        return client;
    }

    /**
     * Get all nodes connected to a given GoogleApiClient, excluding the local node
     * @param client the GoogleApiClient to get the nodes of
     * @param nodeListener the callback for when the nodes are available
     */
    public static void getNodes(final GoogleApiClient client, final NodeListener nodeListener) {

        new AsyncTask<Void, Void, List<Node>>() {
            @Override
            protected List<Node> doInBackground(Void... params) {
                ensureConnected(client);
                NodeApi.GetConnectedNodesResult result = Wearable.NodeApi.getConnectedNodes(client).await();
                return result.getNodes();
            }

            @Override
            protected void onPostExecute(List<Node> nodes) {
                nodeListener.onNodesConnected(nodes);
            }
        }.execute();

    }

    public static void sendMessage(GoogleApiClient client, Collection<Node> nodes, String path,
                                   String message) {
        for (Node node : nodes) {
            sendMessage(client, node, path, message);
        }
    }

    public static void sendMessage(GoogleApiClient client, Collection<Node> nodes, String path,
                                   String message, ResultCallback<MessageApi.SendMessageResult> callback) {
        for (Node node : nodes) {
            sendMessage(client, node, path, message, callback);
        }
    }

    public static void sendMessage(GoogleApiClient client, Node node, String path, String message) {
        sendMessage(client, node, path, message, null);
    }

    /**
     * Send a message using Android Wear's MessageApi to a given Node using a given path
     * @param client the GoogleApiClient to use to send the message
     * @param node the destination Node for the message
     * @param path the path component of the URI the message will use
     * @param message the message to send
     * @param callback the method that will be called when the message has been sent/failed
     */
    public static void sendMessage(final GoogleApiClient client, final Node node, final String path,
                                   final String message,
                                   final ResultCallback<MessageApi.SendMessageResult> callback) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                ensureConnected(client);
                byte[] messageBytes = message == null ? null : message.getBytes();
                PendingResult<MessageApi.SendMessageResult> result = Wearable.MessageApi.sendMessage(client,
                        node.getId(), path, messageBytes);
                if (callback != null) {
                    result.setResultCallback(callback);
                }
                return null;
            }
        }.execute();
    }

    /**
     * Ensures that the given client is connected
     * NOTE: this method must not be run on the UI thread
     * @param client the GoogleApiClient that should be connected
     */
    private static void ensureConnected(GoogleApiClient client) {
        if (!client.isConnected()) {
            client.blockingConnect();
        }
    }

    public static void putDataItem(final GoogleApiClient client, final DataMap dataMap, final String path) {

        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                ensureConnected(client);

                PutDataMapRequest putDataMapRequest = PutDataMapRequest.create(path);
                putDataMapRequest.getDataMap().putAll(dataMap);
                Wearable.DataApi.putDataItem(client, putDataMapRequest.asPutDataRequest()).await();

                return null;
            }
        }.execute();

    }

    public static void getSyncedItems(final GoogleApiClient client, final GetDataListener getDataListener) {
        getSyncedItems(client, null, getDataListener);
    }

    /**
     * Get items that are synced in the DataLayer storage
     * @param client the GoogleApiClient to use
     * @param path the path of the items you want to retrieve
     * @param getDataListener the callback for when the DataMaps have been retrieved
     */
    public static void getSyncedItems(final GoogleApiClient client, final String path, final GetDataListener getDataListener) {

        new AsyncTask<Void, Void, List<DataMap>>() {

            @Override
            protected List<DataMap> doInBackground(Void... params) {
                ensureConnected(client);

                List<DataMap> dataMaps = new ArrayList<>();
                DataItemBuffer result =  Wearable.DataApi.getDataItems(client).await();

                for (int i = 0; i < result.getCount(); i++) {
                    DataItem item = result.get(i);
                    if (path != null && path.length() > 0) {
                        if (item.getUri().getPath().equals(path)) {
                            dataMaps.add(DataMapItem.fromDataItem(item).getDataMap());
                        }
                    } else {
                        dataMaps.add(DataMapItem.fromDataItem(item).getDataMap());
                    }
                }

                result.close();

                return dataMaps;
            }

            @Override
            protected void onPostExecute(List<DataMap> dataMaps) {
                getDataListener.onDataReceived(dataMaps);
            }
        }.execute();

    }

    // Note: cannot be run on main thread as uses call to 'await()'
    private static Node getLocalNode(GoogleApiClient client) {
        return Wearable.NodeApi.getLocalNode(client).await().getNode();
    }

    public static void getUri(final GoogleApiClient client, final String path, final UriListener uriListener) {

        new AsyncTask<Void, Void, Uri>() {

            @Override
            protected Uri doInBackground(Void... params) {
                return new Uri.Builder()
                        .scheme(PutDataRequest.WEAR_URI_SCHEME)
                        .authority(getLocalNode(client).getId())
                        .path(path)
                        .build();
            }

            @Override
            protected void onPostExecute(Uri uri) {
                uriListener.onUriBuilt(uri);
            }
        }.execute();

    }

    /**
     * Convert a list to a DataMap
     * @param key the key in the DataMap for this list
     * @param list the List to convert
     * @param <T> All elements in the List must be Parcelable
     * @return a DataMap containing the List
     */
    public static <T extends Parcelable> DataMap listToDataMap(String key, List<T> list) {

        Parcel parcel = Parcel.obtain();
        parcel.setDataPosition(0);
        parcel.writeTypedList(list);
        DataMap dataMap = new DataMap();
        dataMap.putByteArray(key, parcel.marshall());
        parcel.recycle();
        return dataMap;

    }

    /**
     * Retrieve a List from a DataMap
     * @param key the key for the List in the DataMap
     * @param dataMap the DataMap to retrieve the List from
     * @param creator the Creator that manages creating the List
     * @param <T> all elements of the List should be Parcelable
     * @return the List that was in the DataMap
     */
    public static <T extends Parcelable> List<T> listFromDataMap(String key, DataMap dataMap, Parcelable.Creator<T> creator) {

        List<T> list = new ArrayList<>();
        byte[] byteArray = dataMap.getByteArray(key);
        Parcel parcel = Parcel.obtain();

        parcel.unmarshall(byteArray, 0, byteArray.length);
        parcel.setDataPosition(0);
        parcel.readTypedList(list, creator);
        parcel.recycle();

        return list;

    }

    public static List<DataMap> getDataMaps(DataEventBuffer dataEvents) {
        return getDataMaps(dataEvents, null);
    }

    /**
     * Get data maps from a given DataEventBuffer
     * @param dataEvents the DataEventBuffer to get the DataMaps from
     * @param path the path of the DataMaps to get
     * @return a List of DataMaps
     */
    public static List<DataMap> getDataMaps(DataEventBuffer dataEvents, String path) {

        List<DataMap> dataMaps = new ArrayList<>();

        for (DataEvent event : dataEvents) {
            DataItem dataItem = event.getDataItem();
            if (path != null && path.length() > 0) {
                if (dataItem.getUri().getPath().equals(path)) {
                    dataMaps.add(DataMapItem.fromDataItem(dataItem).getDataMap());
                }
            } else {
                dataMaps.add(DataMapItem.fromDataItem(dataItem).getDataMap());
            }
        }

        return dataMaps;

    }

}
