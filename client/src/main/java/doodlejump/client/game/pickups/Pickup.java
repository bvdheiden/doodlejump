package doodlejump.client.game.pickups;

import doodlejump.client.game.collision.Vector2;
import doodlejump.client.game.collision.colliders.BoxCollider;
import doodlejump.client.game.collision.colliders.CircleCollider;
import doodlejump.client.game.collision.colliders.Collider2D;
import doodlejump.client.game.collision.enums.ColliderTag;
import javafx.scene.canvas.GraphicsContext;
import org.jetbrains.annotations.Contract;

public class Pickup {
    protected final double x;
    protected final double y;
    protected final double radius;

    protected boolean shouldBeRemoved = false;

    protected final CircleCollider collider;

    @Contract(pure = true)
    public Pickup(double x, double y, double radius) {
        this.x = x;
        this.y = y;
        this.radius = radius;

        this.collider = new CircleCollider(new Vector2(x + radius, y + radius), radius);
        collider.setColliderTag(ColliderTag.DEFAULT);
        collider.collisionCallback = this::onPickup;
        collider.setOwnerObject(this);
    }

    public void onPickup(Collider2D other)
    {

    }

    public void draw(GraphicsContext graphicsContext)
    {

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
