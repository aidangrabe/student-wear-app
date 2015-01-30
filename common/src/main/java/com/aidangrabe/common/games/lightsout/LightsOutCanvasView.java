package com.aidangrabe.common.games.lightsout;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.aidangrabe.common.R;

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
    private Bitmap mImgOn, mImgOff;
    private Rect mImgSrcRect, mImgDestRect;

    public LightsOutCanvasView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();

    }

    public void setBoardSize(int width, int height) {
        mBoardWidth = width;
        mBoardHeight = height;
        mTilePaint = new Paint();
        mTilePaint.setColor(Color.WHITE);
        mTilePaint.setAntiAlias(true);
    }

    private void init() {

        mBackgroundColor = Color.rgb(20, 20, 20);
        mTiles = new ArrayList<>();

        mImgOn = BitmapFactory.decodeResource(getResources(), R.drawable.ic_circle_blue);
        mImgOff = BitmapFactory.decodeResource(getResources(), R.drawable.ic_circle);

        mImgSrcRect = new Rect(0, 0, mImgOn.getWidth(), mImgOn.getHeight());

    }

    public void setTiles(Collection<Tile> tiles) {
        mTiles = tiles;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        // recalculate all the heights
        mWidth = w;
        mHeight = h;

        mTileWidth = w / mBoardWidth;
        mTileHeight = h / mBoardHeight;

        mImgDestRect = new Rect(0, 0, mTileWidth, mTileHeight);

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
            // position the drawing rectangle
            mImgDestRect.offsetTo(tile.position.x * mTileWidth, tile.position.y * mTileHeight);
            c.drawBitmap(tile.isOn() ? mImgOn : mImgOff, mImgSrcRect, mImgDestRect, mTilePaint);
        }

    }

}
