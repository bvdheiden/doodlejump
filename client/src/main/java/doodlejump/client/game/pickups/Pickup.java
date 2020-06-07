package doodlejump.client.game.pickups;

import doodlejump.client.game.collision.Vector2;
import doodlejump.client.game.collision.colliders.CircleCollider;
import doodlejump.client.game.collision.colliders.Collider2D;
import doodlejump.client.game.collision.enums.ColliderTag;

import javafx.scene.paint.Paint;
import org.jetbrains.annotations.Contract;

public class Pickup {
    private final double x;
    private final double y;
    private final double radius;
    private final Paint paint;

    protected boolean shouldBeRemoved = false;
    protected final CircleCollider collider;

    @Contract(pure = true)
    public Pickup(double x, double y, double radius, Paint paint) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.paint = paint;

        this.collider = new CircleCollider(new Vector2(x + radius, y + radius), radius);
        collider.setColliderTag(ColliderTag.DEFAULT);
        collider.collisionCallback = this::onPickup;
        collider.setOwnerObject(this);
    }

    public void onPickup(Collider2D other)
    {
        System.out.println("bom test 1");
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
