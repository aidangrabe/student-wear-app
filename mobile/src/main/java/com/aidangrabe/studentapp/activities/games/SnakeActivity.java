package com.aidangrabe.studentapp.activities.games;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.aidangrabe.common.SharedConstants;
import com.aidangrabe.common.wearable.WearUtil;
import com.aidangrabe.studentapp.R;
import com.aidangrabe.studentapp.games.snake.Snake;
import com.aidangrabe.studentapp.games.snake.view.SnakeCanvasView;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageEvent;

/**
 * Created by aidan on 13/01/15.
 *
 */
public class SnakeActivity extends Activity implements MessageApi.MessageListener {

    private WearUtil mWearUtil;
    private SnakeCanvasView mView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_snake);

        mView = (SnakeCanvasView) findViewById(R.id.snake_canvas_view);

        mWearUtil = new WearUtil(this);
        mWearUtil.setMessageListener(this);

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

    /**
     * Receive messages sent from the controllers and pass on the control to the snakes
     * @param messageEvent the messageEvent sent by the controllers. Should contain a direction
     */
    @Override
    public void onMessageReceived(MessageEvent messageEvent) {

        // get first snake, TODO: support multiple players/snakes
        Snake snake = mView.getSnakes()[0];

        if (messageEvent.getPath().equals(SharedConstants.Wearable.MESSAGE_GAME_CONTROLLER)) {

            String dir = new String(messageEvent.getData());
            switch (dir) {
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


    }
}
