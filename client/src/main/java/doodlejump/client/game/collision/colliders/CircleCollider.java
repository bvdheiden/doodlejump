package doodlejump.client.game.collision.colliders;

import doodlejump.client.game.collision.Vector2;
import doodlejump.client.game.collision.enums.ColliderType;

public class CircleCollider extends Collider2D {

    private final double radius;

    public CircleCollider(Vector2 pos, double radius) {
        super();
        this.radius = radius;
        this.pos = pos;
        this.colliderType = ColliderType.CIRCLE_COLLIDER;

        preCollisionCallback = this::preCollisons;
        collisionCallback = this::updateCollisons;
    }


    //the check for collision call
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
        return ((this.radius + other.radius) > this.pos.distance(other.pos));
    }

    @Override
    public boolean boxCollision(BoxCollider other) {
        double circleDistanceX = Math.abs(this.pos.x - other.pos.x);
        double circleDistanceY = Math.abs(this.pos.y - other.pos.y);

        if (circleDistanceX > (other.getWidth() / 2 + this.radius)) {
            return false;
        }
        if (circleDistanceY > (other.getHeight() / 2 + this.radius)) {
            return false;
        }

        if (circleDistanceX <= (other.getWidth() / 2)) {
            return true;
        }
        if (circleDistanceY <= (other.getHeight() / 2)) {
            return true;
        }

        double cornerDistance_sq = Math.pow((circleDistanceX - other.getWidth() / 2), 2) +
                Math.pow((circleDistanceY - other.getHeight() / 2), 2);

        return (cornerDistance_sq <= (this.radius * this.radius));
    }

    public double getRadius() {
        return radius;
    }

    @Override
    public boolean containsPoint(Vector2 point) {
        return (this.pos.distance(point) < this.radius);
    }

    @Override
    public void preCollisons() {

    }

    @Override
    public void updateCollisons(Collider2D other) {

    }
}
