package doodlejump.client.game.collision.colliders;

import doodlejump.client.game.collision.Vector2;
import doodlejump.client.game.collision.enums.ColliderType;

public class BoxCollider extends Collider2D {
    public double width;
    public double height;

    public BoxCollider(Vector2 pos, double width, double height) {
        super();
        this.pos = pos;
        this.width = width;
        this.height = height;

        this.colliderType = ColliderType.BOX_COLLIDER;

        preCollisionCallback = this::PreCollisons;
        collisionCallback = this::UpdateCollisons;
    }

    @Override
    public boolean getIsColliding() {
        return isColliding;
    }


    @Override
    public boolean Collide(ColliderType collideWith, Collider2D other) {
        switch (collideWith) {
            case CIRCLE_COLLIDER:
                return CircleCollision((CircleCollider) other);
            case BOX_COLLIDER:
                return BoxCollision((BoxCollider) other);
            case DEFAULT:
                return false;
            default:
                return false;

        }
    }

    @Override
    public boolean CircleCollision(CircleCollider other) {
        double circleDistanceX = Math.abs(other.pos.x - this.pos.x);
        double circleDistanceY = Math.abs(other.pos.y - this.pos.y);

        if (circleDistanceX > (this.width / 2 + other.radius)) {
            return false;
        }
        if (circleDistanceY > (this.height / 2 + other.radius)) {
            return false;
        }

        if (circleDistanceX <= (this.width / 2)) {
            return true;
        }
        if (circleDistanceY <= (this.height / 2)) {
            return true;
        }

        double cornerDistance_sq = Math.pow((circleDistanceX - this.width / 2), 2) +
                Math.pow((circleDistanceY - this.height / 2), 2);

        return (cornerDistance_sq <= (other.radius * other.radius));
    }

    @Override
    public boolean BoxCollision(BoxCollider other) {
        return (this.getRight() >= other.getLeft() &&    // r1 right edge past r2 left
                this.getLeft() <= other.getRight() &&    // r1 left edge past r2 right
                this.getTop() >= other.getBottom() &&    // r1 top edge past r2 bottom
                this.getBottom() <= other.getTop())    // r1 bottom edge past r2 top
        ;
    }

    @Override
    public boolean ContainsPoint(Vector2 point) {
        return (point.x >= this.getLeft() &&
                point.x <= this.getRight() &&
                point.y >= this.getTop() &&
                point.y <= this.getBottom());
    }

    @Override
    public void PreCollisons()
    {

    }

    @Override
    public void UpdateCollisons(Collider2D other) {

    }

    public double getLeft() {
        return this.pos.x - this.width * 0.5;
    }

    public double getRight() {
        return this.pos.x + this.width * 0.5;
    }

    public double getTop() {
        return this.pos.y + this.height * 0.5;
    }

    public double getBottom() {
        return this.pos.y - this.height * 0.5;
    }
}
