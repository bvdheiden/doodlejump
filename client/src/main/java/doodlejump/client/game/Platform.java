package doodlejump.client.game;

import doodlejump.client.game.collision.Vector2;
import doodlejump.client.game.collision.colliders.BoxCollider;
import doodlejump.client.game.collision.colliders.Collider2D;
import doodlejump.client.game.collision.enums.ColliderTag;
import org.jetbrains.annotations.Contract;

public class Platform {
    private static final double HEIGHT = 20.0;

    private final double x;
    private final double y;
    private final double width;
    private final BoxCollider collider;

    @Contract(pure = true)
    public Platform(double x, double y, double width) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.collider = new BoxCollider(new Vector2(x+width*0.5, y+HEIGHT*0.5), width, HEIGHT);

        collider.setColliderTag(ColliderTag.PLATFORM);
        collider.setOwnerObject(this);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return HEIGHT;
    }

    public Collider2D getCollider()
    {
        return collider;
    }
}
