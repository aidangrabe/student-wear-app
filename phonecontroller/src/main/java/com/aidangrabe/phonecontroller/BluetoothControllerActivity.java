package com.aidangrabe.phonecontroller;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.aidangrabe.phonecontroller.controllers.Controller;
import com.aidangrabe.phonecontroller.controllers.JoystickController;
import com.aidangrabe.phonecontroller.controllers.listeners.BluetoothController;
import com.aidangrabe.phonecontroller.views.ControllerView;


public class BluetoothControllerActivity extends ActionBarActivity {

    private static final String UUID = "4b6e3550-9f63-11e4-bcd8-0800200c9a66";

    private BluetoothController mControllerListener;
    private Controller mController;
    private ControllerView mView;

    @Override
    protected void onResume() {
        super.onResume();

        mControllerListener.connect();

    }

    @Override
    protected void onPause() {
        super.onPause();

        mControllerListener.disconnect();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_controller);

        setContentView(R.layout.activity_bluetooth_controller);

        mControllerListener = new BluetoothController(UUID);

        mController = new JoystickController();
        mController.setCallback(mControllerListener);

        mView = (ControllerView) findViewById(R.id.controller_view);
        mView.setController(mController);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_bluetooth_controller, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
