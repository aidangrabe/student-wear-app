package com.aidangrabe.studentapp.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.aidangrabe.common.SharedConstants;
import com.aidangrabe.common.wearable.WearUtil;
import com.aidangrabe.studentapp.R;

/**
 * Created by aidan on 12/01/15.
 * Activity that sends a find-my-phone request to the mobile
 */
public class FindMyPhoneActivity extends Activity implements View.OnClickListener {

    private WearUtil mWearUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_find_my_phone);

        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(this);

        mWearUtil = new WearUtil(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        mWearUtil.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mWearUtil.disconnect();
    }

    @Override
    public void onClick(View v) {

        mWearUtil.sendMessage(SharedConstants.Wearable.MESSAGE_FIND_MY_PHONE, "");

    }

}
