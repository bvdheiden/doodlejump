package doodlejump.client.game.pickups;

import doodlejump.client.Client;
import doodlejump.client.game.collision.colliders.Collider2D;
import doodlejump.client.game.collision.enums.ColliderTag;
import doodlejump.client.networking.GameClient;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Bomb extends Pickup {
    public static final double RADIUS = 20.0;

    public Bomb(double x, double y) {
        super(x, y, RADIUS, Color.BLACK);
        this.collider.setColliderTag(ColliderTag.BOMB_PICKUP);
    }

    @Override
    public void onPickup(Collider2D other) {
        super.onPickup(other);
        if (other.getColliderTag() == ColliderTag.PLAYER_UNIT) {
            collider.onDestroy();
            GameClient.INSTANCE.sendBomb();
            shouldBeRemoved = true;
        }
    }
}