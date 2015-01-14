package com.aidangrabe.studentapp.activities.games;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.aidangrabe.studentapp.R;
import com.aidangrabe.studentapp.views.DirectionalControllerView;

/**
 * Created by aidan on 13/01/15.
 *
 */
public class DirectionalControllerActivity extends Activity implements DirectionalControllerView.DirectionalControllerListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_directional_controller);

        DirectionalControllerView view = (DirectionalControllerView) findViewById(R.id.directional_controller_view);
        view.setDirectionalControllerListener(this);

    }

    @Override
    public void onDirectionPressed(DirectionalControllerView.Dir direction) {
        String dir = "";
        switch (direction) {
            case UP:
                dir = "UP";
                break;
            case DOWN:
                dir = "DOWN";
                break;
            case RIGHT:
                dir = "RIGHT";
                break;
            case LEFT:
                dir = "LEFT";
                break;
        }
        Log.d("DIR", dir);
    }
}
