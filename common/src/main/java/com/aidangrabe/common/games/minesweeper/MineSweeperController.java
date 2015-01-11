package com.aidangrabe.common.games.minesweeper;

import android.graphics.Point;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aidan on 11/01/15.
 *
 */
public class MineSweeperController {

    private Board board;
    private List<Point> inActiveTiles;
    private List<Point> affectedTiles;

    public interface MineSweeperListener {
        public void onBombFound(int x, int y);
        public void onTileCleared(int x, int y);
        public void onBombProximityUpdated(int x, int y, int touchingBombs);
    }

    // default listener
    private MineSweeperListener listener = new MineSweeperListener() {
        @Override
        public void onBombFound(int x, int y) {}
        @Override
        public void onTileCleared(int x, int y) {}
        @Override
        public void onBombProximityUpdated(int x, int y, int touchingBombs) {}
    };

    public MineSweeperController(int width, int height, int numBombs) {

        board = new Board(width, height);
        board.scatterBombs(numBombs);
        inActiveTiles = new ArrayList<>();
        affectedTiles = new ArrayList<>();

    }

    public void mineTile(int x, int y) {

        affectedTiles.clear();
        mineNeighbours(x, y);

        if (board.isBomb(x, y)) {
            listener.onBombFound(x, y);
            return;
        }

        for (Point tile : affectedTiles) {
            listener.onBombProximityUpdated(tile.x, tile.y, board.getBombProximity(tile.x, tile.y));
        }

    }

    private void mineNeighbours(int x, int y) {
        Point current = new Point(x, y);

        // tile not active, so ignore
        if (inActiveTiles.contains(current)) {
            return;
        }

        listener.onTileCleared(x, y);
        inActiveTiles.add(current);
        affectedTiles.add(current);
        for (Point neighbour : board.getNeighbours(x, y)) {
            if (!board.isBomb(neighbour.x, neighbour.y)) {
                mineNeighbours(neighbour.x, neighbour.y);
            }
        }

    }

    public boolean checkWin() {
        return false;
    }

    public void setListener(MineSweeperListener listener) {
        this.listener = listener;
    }

}
