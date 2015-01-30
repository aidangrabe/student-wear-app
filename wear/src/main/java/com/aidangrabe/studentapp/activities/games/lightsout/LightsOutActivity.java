package com.aidangrabe.studentapp.activities.games.lightsout;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.wearable.activity.ConfirmationActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.aidangrabe.common.games.lightsout.LightsOutCanvasView;
import com.aidangrabe.common.games.lightsout.LightsOutController;
import com.aidangrabe.common.games.lightsout.Tile;
import com.aidangrabe.studentapp.R;

/**
 * Created by aidan on 30/01/15.
 *
 */
public class LightsOutActivity extends Activity implements LightsOutController.Listener, View.OnTouchListener {

    public static final String ARG_LEVEL = "level";

    private static final int NUM_TILES_X = 5;
    private static final int NUM_TILES_Y = 5;

    private LightsOutController mGame;
    private LightsOutCanvasView mView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_lightsout);

        mView = (LightsOutCanvasView) findViewById(R.id.lights_out_canvas_view);

        mGame = new LightsOutController(NUM_TILES_X, NUM_TILES_Y, this);

        mView.setBoardSize(NUM_TILES_X, NUM_TILES_Y);
        mView.setTiles(mGame.getTiles());
        mView.setOnTouchListener(this);

        Bundle args = getIntent().getExtras();
        String level = args.getString(ARG_LEVEL, "");
        if (level.length() > 0) {
            mGame.loadLevel(level);
        }

    }

    @Override
    public void onTileToggled(Tile tile) {

    }

    @Override
    public void onGameOver(int numMoves) {

        Intent intent = new Intent(this, ConfirmationActivity.class);
        intent.putExtra(ConfirmationActivity.EXTRA_ANIMATION_TYPE, ConfirmationActivity.SUCCESS_ANIMATION);
        intent.putExtra(ConfirmationActivity.EXTRA_MESSAGE, "Level complete!\nScore: " + numMoves);
        startActivity(intent);

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        Log.d("D", "onTouch");

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            Point p = mView.getTilePosition(event.getX(), event.getY());
            mGame.selectTile(p.x, p.y);
            mView.invalidate();
        }

        return false;
    }
}
