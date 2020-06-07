package doodlejump.client.game.collision;

import doodlejump.client.game.collision.colliders.Collider2D;

public interface CollisionCallback {
    void collide(Collider2D other);
}
