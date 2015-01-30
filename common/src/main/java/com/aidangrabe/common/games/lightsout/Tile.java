package com.aidangrabe.common.games.lightsout;

import android.graphics.Point;

/**
 * Created by aidan on 30/01/15.
 * This class represents a game tile in the Lights Out game
 */
public class Tile {

    public Point position;
    public boolean on;

    public Tile(int x, int y) {

        position = new Point(x, y);
        on = false;

    }

    public void setOn(boolean on) {
        this.on = on;
    }

    public boolean isOn() {
        return on;
    }

    public void toggle() {
        setOn(!on);
    }

}
