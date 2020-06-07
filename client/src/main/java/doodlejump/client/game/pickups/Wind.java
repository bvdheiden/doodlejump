package doodlejump.client.game.pickups;

import doodlejump.client.game.collision.colliders.Collider2D;
import doodlejump.client.game.collision.enums.ColliderTag;
import doodlejump.client.networking.GameClient;
import javafx.scene.paint.Color;

public class Wind extends Pickup {
    public static final double RADIUS = 20.0;

    public Wind(double x, double y) {
        super(x, y, RADIUS, Color.BLUE);
    }

    @Override
    public void onPickup(Collider2D other) {
        super.onPickup(other);
        if (other.getColliderTag() == ColliderTag.PLAYER_UNIT) {
            System.out.println("wind test");
            collider.onDestroy();
            GameClient.INSTANCE.sendWind();
            shouldBeRemoved = true;
        }
    }
}
