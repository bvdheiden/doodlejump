package doodlejump.client.game.pickups;

import javafx.scene.paint.Paint;
import org.jetbrains.annotations.Contract;

public class Pickup {
    private final double x;
    private final double y;
    private final double radius;
    private final Paint paint;

    @Contract(pure = true)
    public Pickup(double x, double y, double radius, Paint paint) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.paint = paint;
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

    public Paint getPaint() {
        return paint;
    }
}
