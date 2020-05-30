package doodlejump.client.game;

import org.jetbrains.annotations.Contract;

public class Platform {
    private static final double HEIGHT = 20.0;

    private final double x;
    private final double y;
    private final double width;

    @Contract(pure = true)
    public Platform(double x, double y, double width) {
        this.x = x;
        this.y = y;
        this.width = width;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return HEIGHT;
    }
}
