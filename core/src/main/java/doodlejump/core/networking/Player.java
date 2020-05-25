package doodlejump.core.networking;

import java.io.Serializable;

public class Player implements Serializable {
    private final String name;
    private double x;
    private double y;

    public Player(String name) {
        this.name = name;
        this.x = 0.0;
        this.y = 0.0;
    }

    public String getName() {
        return name;
    }

    public void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }
}
