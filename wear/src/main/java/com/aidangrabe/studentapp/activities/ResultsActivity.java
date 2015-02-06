package com.aidangrabe.studentapp.activities;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.wearable.view.FragmentGridPagerAdapter;
import android.support.wearable.view.GridPagerAdapter;
import android.support.wearable.view.GridViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.aidangrabe.common.model.Module;
import com.aidangrabe.common.util.WearUtils;
import com.aidangrabe.common.wearable.WearUtil;
import com.aidangrabe.studentapp.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.Wearable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aidan on 06/02/15.
 * This Activity displays a list of modules that when clicked will show the graph
 */
public class ResultsActivity extends Activity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, MessageApi.MessageListener, DataApi.DataListener {

    private GridViewPager mGridPager;
    private List<Module> mModules;
    private GoogleApiClient mGoogleApiClient;
    private List<Node> mNodes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_grid_pager);

        mModules = new ArrayList<>();
        mNodes = new ArrayList<>();
        mGoogleApiClient = WearUtils.makeClient(this, this, this);

        mGridPager = (GridViewPager) findViewById(R.id.grid_pager);
        mGridPager.setAdapter(new Adapter(getFragmentManager()));

    }

    @Override
    protected void onResume() {
        super.onResume();

        mGoogleApiClient.connect();
        Wearable.MessageApi.addListener(mGoogleApiClient, this);
        Wearable.DataApi.addListener(mGoogleApiClient, this);

    }

    @Override
    protected void onPause() {
        super.onPause();

        mGoogleApiClient.disconnect();
        Wearable.MessageApi.removeListener(mGoogleApiClient, this);
        Wearable.DataApi.removeListener(mGoogleApiClient, this);

    }

    // called when the api client is connected, and we have the list of Nodes
    public void onWearableReady() {
        getModules();
    }

    private void getModules() {
        WearUtils.getSyncedItems(mGoogleApiClient, "/modules/get", new WearUtils.GetDataListener() {
            @Override
            public void onDataReceived(List<DataMap> dataMaps) {
                Log.d("D", "DataMaps received: " + dataMaps.size());
            }
        });
    }

    @Override
    public void onConnected(Bundle bundle) {
        WearUtils.getNodes(mGoogleApiClient, new WearUtils.NodeListener() {
            @Override
            public void onNodesConnected(List<Node> nodes) {
                mNodes = nodes;
                onWearableReady();
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

    private class Adapter extends FragmentGridPagerAdapter {

        public Adapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getRowCount() {
            return mModules.size();
        }

        @Override
        public int getColumnCount(int row) {
            return 1;
        }

        @Override
        public Fragment getFragment(int row, int col) {
            Fragment fragment = null;



            return fragment;
        }
    }

}
