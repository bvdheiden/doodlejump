package doodlejump.client.game.Collision2D;

import doodlejump.client.game.Collision2D.Colliders.Collider2D;

public interface CollisionCallback
{
    void collide(Collider2D other);
}
