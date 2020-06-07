package doodlejump.client.game.pickups;

import javafx.scene.paint.Color;

public class Bomb extends Pickup {
    public static final double RADIUS = 20.0;

    public Bomb(double x, double y) {
        super(x, y, RADIUS, Color.BLACK);
    }
}
