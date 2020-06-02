package doodlejump.client.game.collision.colliders;

import doodlejump.client.game.collision.CollisionSystem;
import doodlejump.client.game.collision.enums.ColliderType;
import doodlejump.client.game.collision.Vector2;

public class CircleCollider extends Collider2D
{
    public double  radius;

    public CircleCollider(Vector2 pos, double radius)
    {
        this.radius = radius;
        this.pos = pos;
        this.colliderType = ColliderType.CIRCLE_COLLIDER;

        collisionCallback = this::UpdateCollisons;
        CollisionSystem.INSTANCE.allColliders.add(this);
    }


    //the check for collision call
    @Override
    public boolean Collide(ColliderType collideWith, Collider2D other)
    {
        switch (collideWith)
        {
            case CIRCLE_COLLIDER:
                return CircleCollision((CircleCollider)other);
            case BOX_COLLIDER:
                return BoxCollision((BoxCollider)other);
            case DEFAULT:
                return false;
            default:
                return false;
        }
    }

    
    @Override
    public boolean CircleCollision(CircleCollider other)
    {
        return ((this.radius + other.radius) > this.pos.Distance(other.pos));
    }

    @Override
    public boolean BoxCollision(BoxCollider other)
    {
        double circleDistanceX = Math.abs(this.pos.x - other.pos.x);
        double circleDistanceY = Math.abs(this.pos.y - other.pos.y);

        if (circleDistanceX > (other.width/2 + this.radius)) { return false; }
        if (circleDistanceY > (other.height/2 + this.radius)) { return false; }

        if (circleDistanceX <= (other.width/2)) { return true; }
        if (circleDistanceY <= (other.height/2)) { return true; }

        double cornerDistance_sq = Math.pow((circleDistanceX - other.width/2),2) +
                Math.pow((circleDistanceY - other.height/2),2);

        return (cornerDistance_sq <= (this.radius*this.radius));
    }

    @Override
    public boolean ContainsPoint(Vector2 point)
    {
        return (this.pos.Distance(point)<this.radius);
    }

    @Override
    public void UpdateCollisons(Collider2D other)
    {

    }
}
