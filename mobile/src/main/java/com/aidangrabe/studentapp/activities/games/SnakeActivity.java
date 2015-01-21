package com.aidangrabe.studentapp.activities.games;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.aidangrabe.common.SharedConstants;
import com.aidangrabe.studentapp.R;
import com.aidangrabe.studentapp.bluetooth.BluetoothServer;
import com.aidangrabe.studentapp.games.snake.Snake;
import com.aidangrabe.studentapp.games.snake.SnakeController;
import com.aidangrabe.studentapp.games.snake.view.SnakeCanvasView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 * Created by aidan on 13/01/15.
 *
 */
public class SnakeActivity extends Activity implements BluetoothServer.BluetoothListener {

    private SnakeCanvasView mView;
    private BluetoothServer mServer;
    private Map<Integer, Integer> mClientPlayerMap;
    private Stack<Integer> mFreeSlots;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_snake);

        mView = (SnakeCanvasView) findViewById(R.id.snake_canvas_view);
        mClientPlayerMap = new LinkedHashMap<>();
        mFreeSlots = new Stack<>();

        mServer = new BluetoothServer();

    }

    @Override
    protected void onResume() {
        super.onResume();

        mServer.addBluetoothListener(this);
        mServer.start();

    }

    @Override
    protected void onPause() {
        super.onPause();

        mServer.stop();
        mServer.removeBluetoothListener(this);

    }

    @Override
    public void onMessageReceived(BluetoothServer.ClientHandler client, String message) {

        SnakeController game = mView.getSnakeController();
        Snake snake = game.getSnake(mClientPlayerMap.get(client.getId()));

        if (snake == null) {
            return;
        }

        switch (message) {
            // LEFT
            case SharedConstants.Wearable.MESSAGE_GAME_CONTROLLER_LEFT:
                snake.move(Snake.Dir.LEFT);
                break;
            // RIGHT
            case SharedConstants.Wearable.MESSAGE_GAME_CONTROLLER_RIGHT:
                snake.move(Snake.Dir.RIGHT);
                break;
            // UP
            case SharedConstants.Wearable.MESSAGE_GAME_CONTROLLER_UP:
                snake.move(Snake.Dir.UP);
                break;
            // DOWN
            case SharedConstants.Wearable.MESSAGE_GAME_CONTROLLER_DOWN:
                snake.move(Snake.Dir.DOWN);
                break;
        }
    }

    @Override
    public void onClientConnected(BluetoothServer.ClientHandler client) {

        Log.d("D", "Client connected: " + client.getId());

        addPlayer(client);
        mView.setNumPlayers(mClientPlayerMap.size());

    }

    @Override
    public void onClientDisconnected(BluetoothServer.ClientHandler client) {

        Log.d("D", "Client disconnected: " + client.getId());

        removePlayer(client);
        mView.setNumPlayers(mClientPlayerMap.size());

    }

    private void addPlayer(BluetoothServer.ClientHandler player) {

        int playerNumber = mClientPlayerMap.size();

        if (!mFreeSlots.isEmpty()) {
            playerNumber = mFreeSlots.pop();
        }

        mClientPlayerMap.put(player.getId(), playerNumber);

    }

    private void removePlayer(BluetoothServer.ClientHandler player) {

        int playerNumber = mClientPlayerMap.remove(player.getId());
        mFreeSlots.push(playerNumber);

    }

}
