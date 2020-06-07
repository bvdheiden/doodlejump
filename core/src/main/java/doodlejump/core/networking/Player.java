package doodlejump.core.networking;

import java.io.Serializable;

public class Player implements Serializable {
    public static final double WIDTH = 30;
    public static final double HEIGHT = 70;
    public static final double HALF_WIDTH = WIDTH / 2.0;
    public static final double HALF_HEIGHT = HEIGHT / 2.0;

    private final String name;
    private double x;
    private double y;
    private boolean isReady;
    private boolean currentlyBlownByWind;
    private volatile boolean isHost;

    public Player(String name) {
        this.name = name;
        this.x = 0.0;
        this.y = 0.0;
        currentlyBlownByWind = false;
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

    public boolean isReady() {
        return isReady;
    }

    public void setReady(boolean ready) {
        isReady = ready;
    }

    public boolean isHost() {
        return isHost;
    }

    public void setHost(boolean host) {
        isHost = host;
    }

    @Override
    public String toString() {
        return name + ": " + (isReady ? "ready" : "not ready");
    }

    public boolean isCurrentlyBlownByWind() {
        return currentlyBlownByWind;
    }

    public void setCurrentlyBlownByWind(boolean currentlyBlownByWind) {
        this.currentlyBlownByWind = currentlyBlownByWind;
    }
}
