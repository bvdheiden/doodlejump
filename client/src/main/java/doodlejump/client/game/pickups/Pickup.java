package doodlejump.client.game.pickups;

import org.jetbrains.annotations.Contract;

public class Pickup {
    private final double x;
    private final double y;
    private final double radius;

    @Contract(pure = true)
    public Pickup(double x, double y, double radius) {
        this.x = x;
        this.y = y;
        this.radius = radius;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getRadius() {
        return radius;
    }
}
