package doodlejump.core.networking;

import java.io.Serializable;

public class Player implements Serializable {
    private final String name;
    private double x;
    private double y;
    private double velocityX;
    private double velocityY;
    private boolean isReady;
    private volatile boolean isHost;

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

    public void addVelocity(double x, double y) {
        this.velocityX += x;
        this.velocityY += y;
    }

    public void setVelocity(double x, double y) {
        this.velocityX = x;
        this.velocityY = y;
    }

    public double getVelocityX() {
        return velocityX;
    }

    public void setVelocityX(double velocityX) {
        this.velocityX = velocityX;
    }

    public double getVelocityY() {
        return velocityY;
    }

    public void setVelocityY(double velocityY) {
        this.velocityY = velocityY;
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
}
