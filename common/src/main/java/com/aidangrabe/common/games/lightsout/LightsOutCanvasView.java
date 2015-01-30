package com.aidangrabe.common.games.lightsout;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by aidan on 30/01/15.
 * This class can render a game of Lights Out using a canvas
 */
public class LightsOutCanvasView extends View {

    private int mBackgroundColor;
    private Collection<Tile> mTiles;
    private int mWidth, mHeight, mBoardWidth, mBoardHeight, mTileWidth, mTileHeight;
    private Paint mTilePaint;

    public LightsOutCanvasView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();

    }

    public void setBoardSize(int width, int height) {
        mBoardWidth = width;
        mBoardHeight = height;
        mTilePaint = new Paint();
        mTilePaint.setColor(Color.RED);
    }

    private void init() {

        mBackgroundColor = Color.WHITE;
        mTiles = new ArrayList<>();

    }

    public void setTiles(Collection<Tile> tiles) {
        mTiles = tiles;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mWidth = w;
        mHeight = h;

        mTileWidth = w / mBoardWidth;
        mTileHeight = h / mBoardHeight;

        invalidate();

    }

    public Point getTilePosition(float x, float y) {
        return new Point((int) x / mTileWidth, (int) y / mTileHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawColor(mBackgroundColor);
        drawTiles(canvas);

    }

    private void drawTiles(Canvas c) {

        for (Tile tile : mTiles) {
            mTilePaint.setColor(tile.isOn() ? Color.RED : Color.GREEN);
            c.drawRect(tile.position.x * mTileWidth, tile.position.y * mTileHeight,
                    tile.position.x * mTileWidth + mTileWidth, tile.position.y * mTileHeight + mTileHeight,
                    mTilePaint);
        }

    }

}
