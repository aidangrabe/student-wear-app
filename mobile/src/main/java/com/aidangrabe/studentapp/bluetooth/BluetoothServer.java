package com.aidangrabe.studentapp.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattServer;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Created by aidan on 20/01/15.
 *
 */
public class BluetoothServer {

    public static final String SERVICE_NAME = "BluetoothService";
    public static final String SERVICE_UUID = "4b6e3550-9f63-11e4-bcd8-0800200c9a66";

    private static volatile int clientId = 0;

    private BluetoothAdapter mBtAdapter;
    private BluetoothServerSocket mServerSocket;
    private List<BluetoothDevice> mDevices;
    private ServerHandler mServerHandler;
    private List<BluetoothListener> mListeners;

    public interface BluetoothListener {
        public void onMessageReceived(ClientHandler client, String message);
        public void onClientConnected(ClientHandler client);
        public void onClientDisconnected(ClientHandler client);
    }

    public BluetoothServer() {

        mDevices = new ArrayList<>();
        mListeners = new ArrayList<>();

        mBtAdapter = BluetoothAdapter.getDefaultAdapter();

        // check if bluetooth is available
        if (mBtAdapter == null) {
            throw new IllegalStateException("No bluetooth available");
        }

        try {
            mServerSocket = mBtAdapter.listenUsingInsecureRfcommWithServiceRecord(SERVICE_NAME, UUID.fromString(SERVICE_UUID));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void start() {

        mServerHandler = new ServerHandler();
        Thread serverThread = new Thread(mServerHandler);
        serverThread.start();

    }

    public void stop() {

        mServerHandler.stop();



        try {
            mServerSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void bluetoothListPairedDevices() {

        Set<BluetoothDevice> devices = mBtAdapter.getBondedDevices();
        for (BluetoothDevice device : devices) {
            mDevices.add(device);
        }

    }

    public void addBluetoothListener(BluetoothListener listener) {
        mListeners.add(listener);
    }

    public void removeBluetoothListener(BluetoothListener listener) {
        mListeners.remove(listener);
    }

    private class ServerHandler implements Runnable {
        private boolean running;

        public ServerHandler() {
            Log.d("D", "New Server Handler");
            running = true;
        }

        @Override
        public void run() {
            Log.d("D", "Server running");
            while(running) {

                try {
                    BluetoothSocket client = mServerSocket.accept();

                    // create a new thread to handle the connection
                    Thread clientThread = new Thread(new ClientHandler(client));
                    clientThread.start();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        public void stop() {
            running = false;
        }

    }

    public class ClientHandler implements Runnable {

        private int id;
        private BluetoothSocket mSocket;
        private PrintWriter mOutWriter;
        private BufferedReader mInReader;
        private boolean running;

        public ClientHandler(BluetoothSocket socket) {
            Log.d("D", "New ClientHandler");
            mSocket = socket;
            running = true;
            id = clientId++;

            for (BluetoothListener listener : mListeners) {
                listener.onClientConnected(this);
            }

            try {
                mInReader = new BufferedReader(new InputStreamReader(mSocket.getInputStream()));
                mOutWriter = new PrintWriter(mSocket.getOutputStream(), true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {

            while (running) {
                String line = null;
                try {
                    line = mInReader.readLine();
                    if (line == null) {
                        running = false;
                        break;
                    }

                    for (BluetoothListener listener : mListeners) {
                        listener.onMessageReceived(this, line);
                    }
                } catch (IOException e) {
                    for (BluetoothListener listener : mListeners) {
                        listener.onClientDisconnected(this);
                    }
                    Log.d("D", "Client disconnected");
                    running = false;
                    break;
                }
                Log.d("D", "Server received line [id=" + id + "]: " + line);
            }
            Log.d("D", "Closed Client");
            disconnect();

        }

        public void disconnect() {

            try {
                mSocket.close();
                mInReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mOutWriter.close();

        }

        public void setRunning(boolean running) {
            this.running = running;
        }

        public void write(String msg) {
            mOutWriter.println(msg);
            mOutWriter.flush();
        }

        public int getId() {
            return id;
        }

    }

}

