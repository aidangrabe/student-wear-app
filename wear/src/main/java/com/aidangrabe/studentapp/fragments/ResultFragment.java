package com.aidangrabe.studentapp.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aidangrabe.common.model.Module;
import com.aidangrabe.common.views.SimpleGraph;
import com.aidangrabe.studentapp.R;

/**
 * Created by aidan on 06/02/15.
 */
public class ResultFragment extends Fragment {

    public static final String ARG_MODULE = "module";

    private SimpleGraph mGraph;
    private TextView mTitleText;
    private Module mModule;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle args = getArguments();
        if (args != null) {
            mModule = args.getParcelable(ARG_MODULE);
            mTitleText.setText(mModule.getName());
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_result, container, false);

        mTitleText = (TextView) view.findViewById(R.id.title_text);
        mGraph = (SimpleGraph) view.findViewById(R.id.simple_graph);

        return view;

    }
}
