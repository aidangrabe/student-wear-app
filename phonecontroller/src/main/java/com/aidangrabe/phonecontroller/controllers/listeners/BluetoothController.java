package com.aidangrabe.phonecontroller.controllers.listeners;

import com.aidangrabe.common.SharedConstants;
import com.aidangrabe.common.bluetooth.BluetoothClient;
import com.aidangrabe.phonecontroller.controllers.Controller;

/**
 * Created by aidan on 21/01/15.
 * This class sends the controller direction via Bluetooth
 */
public class BluetoothController implements Controller.ControllerCallbacks {

    private BluetoothClient mClient;

    public BluetoothController(String uuid) {

        mClient = new BluetoothClient(uuid);

    }

    public void connect() {

        mClient.connect();

    }

    public void disconnect() {

        mClient.disconnect();

    }

    @Override
    public void onDirectionChanged(float direction) {

        String message = "";
        if (direction == 0) {
            message = SharedConstants.Wearable.MESSAGE_GAME_CONTROLLER_RIGHT;
        } else if (direction == 90) {
            message = SharedConstants.Wearable.MESSAGE_GAME_CONTROLLER_UP;
        } else if (direction == 180) {
            message = SharedConstants.Wearable.MESSAGE_GAME_CONTROLLER_LEFT;
        } else if (direction == 270) {
            message = SharedConstants.Wearable.MESSAGE_GAME_CONTROLLER_DOWN;
        }

        if (message.length() > 0) {
            mClient.write(message);
        }

    }
}
