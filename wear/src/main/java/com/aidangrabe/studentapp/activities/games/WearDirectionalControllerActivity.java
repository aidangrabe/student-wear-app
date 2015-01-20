package com.aidangrabe.studentapp.activities.games;

import android.app.Activity;
import android.os.Bundle;

import com.aidangrabe.common.SharedConstants;
import com.aidangrabe.common.wearable.WearUtil;
import com.aidangrabe.studentapp.R;
import com.aidangrabe.studentapp.views.DirectionalControllerView;

/**
 * Created by aidan on 13/01/15.
 *
 */
public class WearDirectionalControllerActivity extends Activity implements DirectionalControllerView.DirectionalControllerListener {

    private WearUtil mWearUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_directional_controller);

        mWearUtil = new WearUtil(this);

        DirectionalControllerView view = (DirectionalControllerView) findViewById(R.id.directional_controller_view);
        view.setDirectionalControllerListener(this);

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
    public void onDirectionPressed(DirectionalControllerView.Dir direction) {

        switch (direction) {
            case UP:
                mWearUtil.sendMessage(SharedConstants.Wearable.MESSAGE_GAME_CONTROLLER, SharedConstants.Wearable.MESSAGE_GAME_CONTROLLER_UP);
                break;
            case DOWN:
                mWearUtil.sendMessage(SharedConstants.Wearable.MESSAGE_GAME_CONTROLLER, SharedConstants.Wearable.MESSAGE_GAME_CONTROLLER_DOWN);
                break;
            case RIGHT:
                mWearUtil.sendMessage(SharedConstants.Wearable.MESSAGE_GAME_CONTROLLER, SharedConstants.Wearable.MESSAGE_GAME_CONTROLLER_RIGHT);
                break;
            case LEFT:
                mWearUtil.sendMessage(SharedConstants.Wearable.MESSAGE_GAME_CONTROLLER, SharedConstants.Wearable.MESSAGE_GAME_CONTROLLER_LEFT);
                break;
        }

    }

}
