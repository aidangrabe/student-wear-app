package com.aidangrabe.common.games.minesweeper;

import android.graphics.Point;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by aidan on 11/01/15.
 * A Mine Sweeper board/grid
 */
public class Board {

    private int width, height;
    private TileType[][] grid;

    public enum TileType {
        BOMB, NORMAL
    }

    public Board(int width, int height) {
        this.width = width;
        this.height = height;
        reset();
    }

    public void reset() {
        grid = new TileType[height][width];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                setTile(x, y, TileType.NORMAL);
            }
        }
    }

    public int getBombProximity(int x, int y) {

        int bombCount = 0;

        List<Point> neighbours = getAllNeighbours(x, y);
        for (Point tile : neighbours) {
            if (isBomb(tile.x, tile.y)) {
                bombCount++;
            }
        }

        return bombCount;

    }

    public List<Point> getNeighbours(int x, int y) {

        List<Point> neighbours = new ArrayList<>();
        if (isValidTile(x-1, y)) {
            neighbours.add(new Point(x - 1, y));
        }
        if (isValidTile(x+1, y)) {
            neighbours.add(new Point(x+1, y));
        }
        if (isValidTile(x, y-1 )) {
            neighbours.add(new Point(x, y-1));
        }
        if (isValidTile(x, y+1)) {
            neighbours.add(new Point(x, y+1));
        }

        return neighbours;

    }

    public List<Point> getAllNeighbours(int x, int y) {

        List<Point> neighbours = getNeighbours(x, y);
        if (isValidTile(x-1, y-1)) {
            neighbours.add(new Point(x-1, y-1));
        }
        if (isValidTile(x-1, y+1)) {
            neighbours.add(new Point(x-1, y+1));
        }
        if (isValidTile(x+1, y+1)) {
            neighbours.add(new Point(x+1, y+1));
        }
        if (isValidTile(x+1, y-1)) {
            neighbours.add(new Point(x+1, y-1));
        }

        return neighbours;
    }

    public boolean isValidTile(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }

    public TileType getTile(int x, int y) throws IndexOutOfBoundsException {
        return grid[x][y];
    }

    public void placeBomb(int x, int y) {
        setTile(x, y, TileType.BOMB);
    }

    public void scatterBombs(int numBombs) {
        List<Integer> bombPositions = range(0, width * height);
        Collections.shuffle(bombPositions);
        for (int i = 0; i < numBombs; i++) {
            int position = bombPositions.get(i);
            placeBomb(position%width, position/width);
        }
    }

    public boolean isBomb(int x, int y) {
        return getTile(x, y) == TileType.BOMB;
    }

    public void setTile(int x, int y, TileType type) {
        grid[x][y] = type;
    }

    public List<Integer> range(int lower, int upper) {
        List<Integer> range = new ArrayList<>();
        for (int i = lower; i < upper; i++) {
            range.add(i);
        }
        return range;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("--- Board ---");
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int tile = grid[x][y] == TileType.NORMAL ? 0 : 1;
                sb.append(Integer.toString(tile));
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
