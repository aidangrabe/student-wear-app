package com.aidangrabe.common.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.ParcelUuid;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.SocketException;
import java.util.Set;
import java.util.UUID;

/**
 * Created by aidan on 20/01/15.
 *
 */
public class BluetoothClient {

    private UUID mUuid;
    private BluetoothAdapter mBtAdapter;
    private BluetoothSocket mServer;
    private BluetoothDevice mServerDevice;
    private BufferedReader mInReader;
    private PrintWriter mOutWriter;

    public BluetoothClient(String uuid) {
        this(UUID.fromString(uuid));
    }

    public BluetoothClient(UUID uuid) {

        mUuid = uuid;
        mBtAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBtAdapter == null) {
            throw new IllegalStateException("Bluetooth not available");
        }

        discoverServerDevice();

    }

    public void connect() {

        if (mServerDevice == null) {
            return;
        }

        Log.d("D", "Connecting");
        try {
            mServer = mServerDevice.createInsecureRfcommSocketToServiceRecord(mUuid);
            mServer.connect();
            mInReader = new BufferedReader(new InputStreamReader(mServer.getInputStream()));
            mOutWriter = new PrintWriter(mServer.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void disconnect() {

        try {
            mServer.close();
            mInReader.close();
            mOutWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void discoverServerDevice() {

        Set<BluetoothDevice> devices = mBtAdapter.getBondedDevices();

        // query the paired devices
        for (BluetoothDevice device : devices) {

            mServerDevice = device;
            Log.d("D", "ServerDevice: " + device.getName());

        }

    }

    public void write(String msg) {

        Log.d("D", "Writing message: " + msg);
        if (mOutWriter != null) {
            mOutWriter.println(msg);
            mOutWriter.flush();
        }

    }

}
