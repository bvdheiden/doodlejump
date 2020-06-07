package doodlejump.client.game.pickups;

import javafx.scene.paint.Color;

public class Wind extends Pickup {
    public static final double RADIUS = 20.0;

    public Wind(double x, double y) {
        super(x, y, RADIUS, Color.BLUE);
    }
}
