package com.aidangrabe.studentapp.activities.games;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.aidangrabe.common.SharedConstants;
import com.aidangrabe.common.wearable.WearUtil;
import com.aidangrabe.studentapp.R;
import com.aidangrabe.studentapp.bluetooth.BluetoothServer;
import com.aidangrabe.studentapp.games.snake.Snake;
import com.aidangrabe.studentapp.games.snake.SnakeController;
import com.aidangrabe.studentapp.games.snake.view.SnakeCanvasView;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageEvent;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by aidan on 13/01/15.
 *
 */
public class SnakeActivity extends Activity implements BluetoothServer.BluetoothListener {

    private SnakeCanvasView mView;
    private Map<String, Integer> mPlayerMap;
    private int mNumPlayers;
    private BluetoothServer mServer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_snake);

        mView = (SnakeCanvasView) findViewById(R.id.snake_canvas_view);
        mPlayerMap = new HashMap<>();
        mNumPlayers = 0;

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

    private void addPlayer(String nodeId) {
        mPlayerMap.put(nodeId, mNumPlayers++);
        mView.setNumPlayers(mNumPlayers);
        Log.d("DEBUG", "Add Player: " + mNumPlayers);
    }

    @Override
    public void onMessageReceived(BluetoothServer.ClientHandler client, String message) {

        SnakeController game = mView.getSnakeController();
//        Snake snake = game.getSnake(player);

//        if (snake == null) {
//            return;
//        }

        Log.d("D", "onMessageReceived: " + message);

//        if (message) {
//
//        }

//        String dir = new String();
//        switch (dir) {
//            // LEFT
//            case SharedConstants.Wearable.MESSAGE_GAME_CONTROLLER_LEFT:
//                snake.move(Snake.Dir.LEFT);
//                break;
//            // RIGHT
//            case SharedConstants.Wearable.MESSAGE_GAME_CONTROLLER_RIGHT:
//                snake.move(Snake.Dir.RIGHT);
//                break;
//            // UP
//            case SharedConstants.Wearable.MESSAGE_GAME_CONTROLLER_UP:
//                snake.move(Snake.Dir.UP);
//                break;
//            // DOWN
//            case SharedConstants.Wearable.MESSAGE_GAME_CONTROLLER_DOWN:
//                snake.move(Snake.Dir.DOWN);
//                break;
//        }
    }

    @Override
    public void onClientConnected(BluetoothServer.ClientHandler client) {

        Log.d("D", "Client connected: " + client.getId());

    }

    @Override
    public void onClientDisconnected(BluetoothServer.ClientHandler client) {

        Log.d("D", "Client disconnected: " + client.getId());

    }
}
