package doodlejump.client.game.collision.colliders;

import doodlejump.client.game.collision.Vector2;
import doodlejump.client.game.collision.enums.ColliderType;

public class BoxCollider extends Collider2D {
    private double width;
    private double height;

    public BoxCollider(Vector2 pos, double width, double height) {
        super();
        this.pos = pos;
        this.width = width;
        this.height = height;

        this.colliderType = ColliderType.BOX_COLLIDER;

        preCollisionCallback = this::preCollisons;
        collisionCallback = this::updateCollisons;
    }

    @Override
    public boolean getIsColliding() {
        return isColliding;
    }


    @Override
    public boolean collide(ColliderType collideWith, Collider2D other) {
        switch (collideWith) {
            case CIRCLE_COLLIDER:
                return circleCollision((CircleCollider) other);
            case BOX_COLLIDER:
                return boxCollision((BoxCollider) other);
            case DEFAULT:
                return false;
            default:
                return false;

        }
    }

    @Override
    public boolean circleCollision(CircleCollider other) {
        double circleDistanceX = Math.abs(other.pos.x - this.pos.x);
        double circleDistanceY = Math.abs(other.pos.y - this.pos.y);

        if (circleDistanceX > (this.getWidth() / 2 + other.getRadius())) {
            return false;
        }
        if (circleDistanceY > (this.getHeight() / 2 + other.getRadius())) {
            return false;
        }

        if (circleDistanceX <= (this.getWidth() / 2)) {
            return true;
        }
        if (circleDistanceY <= (this.getHeight() / 2)) {
            return true;
        }

        double cornerDistance_sq = Math.pow((circleDistanceX - this.getWidth() / 2), 2) +
                Math.pow((circleDistanceY - this.getHeight() / 2), 2);

        return (cornerDistance_sq <= (other.getRadius() * other.getRadius()));
    }

    @Override
    public boolean boxCollision(BoxCollider other) {
        return (this.getRight() >= other.getLeft() &&    // right edge past left
                this.getLeft() <= other.getRight() &&    // left edge past right
                this.getTop() >= other.getBottom() &&    // top edge past bottom
                this.getBottom() <= other.getTop())      // bottom edge past top
        ;
    }

    @Override
    public boolean containsPoint(Vector2 point) {
        return (point.x >= this.getLeft() &&
                point.x <= this.getRight() &&
                point.y >= this.getTop() &&
                point.y <= this.getBottom());
    }

    @Override
    public void preCollisons()
    {

    }

    @Override
    public void updateCollisons(Collider2D other) {

    }

    public double getWidth()
    {
        return this.width;
    }

    public double getHeight()
    {
        return this.height;
    }

    public double getLeft() {
        return this.pos.x - this.getWidth() * 0.5;
    }

    public double getRight() {
        return this.pos.x + this.getWidth() * 0.5;
    }

    public double getTop() {
        return this.pos.y + this.getHeight() * 0.5;
    }

    public double getBottom() {
        return this.pos.y - this.getHeight() * 0.5;
    }
}
