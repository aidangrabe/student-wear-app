package com.aidangrabe.studentapp.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aidangrabe.common.SharedConstants;
import com.aidangrabe.common.model.Module;
import com.aidangrabe.common.model.Result;
import com.aidangrabe.common.util.WearUtils;
import com.aidangrabe.common.views.SimpleGraph;
import com.aidangrabe.studentapp.R;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.Wearable;

import java.util.List;

/**
 * Created by aidan on 06/02/15.
 */
public class ResultFragment extends Fragment implements DataApi.DataListener {

    public static final String ARG_MODULE = "module";

    private SimpleGraph mGraph;
    private TextView mTitleText;
    private Module mModule;
    private GoogleApiClient mGoogleApiClient;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle args = getArguments();
        if (args != null) {
            mModule = args.getParcelable(ARG_MODULE);
            mTitleText.setText(mModule.getName());
        }

        getModuleResults();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_result, container, false);

        mTitleText = (TextView) view.findViewById(R.id.title_text);
        mGraph = (SimpleGraph) view.findViewById(R.id.simple_graph);

        return view;

    }

    private void getModuleResults() {

        WearUtils.getSyncedItems(mGoogleApiClient, SharedConstants.Wearable.DATA_PATH_RESULTS(mModule),
                new WearUtils.GetDataListener() {
            @Override
            public void onDataReceived(List<DataMap> dataMaps) {
                if (dataMaps.size() > 0) {
                    DataMap dataMap = dataMaps.get(0);
                    List<Result> results = WearUtils.listFromDataMap("results", dataMap, Result.CREATOR);
                    mGraph.clearValues();
                    for (Result result : results) {
                        mGraph.addValue(result.getGrade());
                    }
                    mGraph.invalidate();
                }
            }
        });

    }

    public void setGoogleApiClient(GoogleApiClient googleApiClient) {
        mGoogleApiClient = googleApiClient;
    }

    @Override
    public void onPause() {
        super.onPause();

        Wearable.DataApi.removeListener(mGoogleApiClient, this);

    }

    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {

    }

}
