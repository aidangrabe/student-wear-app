package com.aidangrabe.studentapp.activities.games.minesweeper;

import com.aidangrabe.common.util.ArrayUtil;

import java.util.ArrayList;

/**
 * Created by aidan on 01/11/14.
 *
 */
public class MineSweeper {

    private int width, height, numMines;
    private MineSweeperTile[][] grid;

    public MineSweeper(int width, int height, int numMines) {
        this.width = width;
        this.height = height;
        this.numMines = numMines;
        grid = new MineSweeperTile[width][height];
        clearGrid();
        addMines(numMines);
    }

    // create numMines number of mines and place them randomly on the grid
    private void addMines(int numMines) {
        int[] positions = new int[width * height];
        for (int i = 0; i < width * height; i++) {
            positions[i] = i;
        }
        ArrayUtil.shuffle(positions);
        for (int i = 0; i < numMines; i++) {
            int x = positions[i] % width;
            int y = positions[i] / height;
            grid[x][y] = MineSweeperTile.MINE;
        }
    }

    public MineSweeperTile selectTile(int x, int y) {
        return grid[x][y];
    }

    public enum MineSweeperTile {
        EMPTY, MINE
    }

    public void clearGrid() {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                grid[x][y] = MineSweeperTile.EMPTY;
            }
        }
    }

    public MineSweeperTile[][] getGrid() {
        return grid;
    }

    public ArrayList<MineSweeperTile> getNeighbours(int tileX, int tileY) {
        int x, y;
        ArrayList<MineSweeperTile> neighbours = new ArrayList<MineSweeperTile>();
        for (int i = 0; i < 9; i++) {
            x = i % 3;
            y = i / 3;
            int checkX = tileX - 1 + x;
            int checkY = tileY - 1 + y;
            MineSweeperTile tile;
            try {
                tile = grid[checkX][checkY];
                if (checkX == tileX && checkY == tileY) {
                    continue;
                }
            } catch (IndexOutOfBoundsException e) {
                continue;
            }
            neighbours.add(tile);
        }
        return neighbours;
    }



}
