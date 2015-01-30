package com.aidangrabe.common.games.lightsout;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by aidan on 30/01/15.
 * This class handles the game of Lights Out
 * @link http://en.wikipedia.org/wiki/Lights_Out_%28game%29
 */
public class LightsOutController {

    private int mWidth, mHeight;
    private Listener mListener;
    private Tile[][] mBoard;
    private int mNumMoves;

    public interface Listener {
        public void onTileToggled(Tile tile);
        public void onGameOver(int numMoves);
    }

    public LightsOutController(int width, int height, Listener listener) {

        mWidth = width;
        mHeight = height;
        mListener = listener;

        mBoard = new Tile[width][height];
        mNumMoves = 0;

        resetBoard();

    }

    public void resetBoard() {

        for (int y = 0; y < mHeight; y++) {
            for (int x = 0; x < mWidth; x++) {
                Tile tile = getTile(x, y);
                if (tile != null) {
                    tile.position.set(x, y);
                } else {
                    setTile(new Tile(x, y));
                }
            }
        }

    }

    public void loadLevel(String level) {

        int position = 0;
        int x = 0;
        int y = 0;

        for (char c : level.toCharArray()) {
            switch (c) {
                default:
                    break;
                case 'X':
                    selectTile(x, y);
                    break;
            }
            position++;
            x = position % mWidth;
            y = position / mWidth;
        }

    }

    public Collection<Tile> getNeighbours(int x, int y) {
        Tile tile = getTile(x, y);
        return getNeighbours(tile);
    }

    public Collection<Tile> getNeighbours(Tile tile) {

        Collection<Tile> neighbours = new ArrayList<>();

        if (tile == null) {
            return neighbours;
        }

        int x = tile.position.x;
        int y = tile.position.y;

        for (int i = -1; i < 2; i += 2) {
            Tile tileH = getTile(x + i, y);
            Tile tileV = getTile(x, y + i);

            if (tileH != null) {
                neighbours.add(tileH);
            }

            if (tileV != null) {
                neighbours.add(tileV);
            }
        }

        return neighbours;

    }

    public Tile getTile(int x, int y) {

        if (x >= 0 && x < mWidth && y >= 0 && y < mHeight) {
            return mBoard[x][y];
        }
        return null;

    }

    public Collection<Tile> getTiles() {

        Collection<Tile> tiles = new ArrayList<>();

        for (int y = 0; y < mHeight; y++) {
            for (int x = 0; x < mWidth; x++) {
                Tile tile = getTile(x, y);
                if (tile != null) {
                    tiles.add(tile);
                }
            }
        }

        return tiles;

    }

    private void setTile(Tile tile) {

        if (tile.position.x >= 0 && tile.position.x < mWidth && tile.position.y >= 0 && tile.position.y < mHeight) {
            mBoard[tile.position.x][tile.position.y] = tile;
        }

    }

    public void selectTile(int x, int y) {

        Tile tile = getTile(x, y);
        if (tile != null) {
            toggleTile(tile);
            toggleTiles(getNeighbours(tile));
            mNumMoves++;
            checkWin();
        }

    }

    private void toggleTile(Tile tile) {
        tile.toggle();
        mListener.onTileToggled(tile);
    }

    private void toggleTiles(Collection<Tile> tiles) {
        for (Tile tile : tiles) {
            toggleTile(tile);
        }
    }

    private boolean checkWin() {

        boolean win = true;

        lose:
        for (int y = 0; y < mHeight; y++) {
            for (int x = 0; x < mWidth; x++) {
                Tile tile = getTile(x, y);
                if (tile.isOn()) {
                    win = false;
                    break lose;
                }
            }
        }

        if (win) {
            mListener.onGameOver(mNumMoves);
        }

        return win;

    }

}
