package doodlejump.client.game.Collision2D.Colliders;

import doodlejump.client.game.Collision2D.CollisionCallback;
import doodlejump.client.game.Collision2D.CollisionSystem;
import doodlejump.client.game.Collision2D.Enums.ColliderTag;
import doodlejump.client.game.Collision2D.Enums.ColliderType;
import doodlejump.client.game.Collision2D.Vector2;

public abstract class Collider2D
{
    protected Object ownerObject = null;
    protected ColliderTag colliderTag = ColliderTag.DEFAULT;
    protected ColliderType colliderType;
    protected boolean isColliding;

    public CollisionCallback collisionCallback;

    protected Vector2 pos;

    //check for collision
    public boolean Collide(ColliderType collideWith, Collider2D other)
    {
        return false;
    }

    //collisionChecks
    protected boolean CircleCollision(CircleCollider other)
    {
        System.out.println("the collision checks should not call the parent class");
        return false;
    }

    protected boolean BoxCollision(BoxCollider other)
    {
        System.out.println("the collision checks should not call the parent class");
        return false;
    }

    protected boolean ContainsPoint(Vector2 point)
    {
        System.out.println("the collision checks should not call the parent class");
        return false;
    }
    /////////////////

    public void OnDestroy()
    {
        CollisionSystem.INSTANCE.allColliders.remove(this);
    }

    public void UpdateCollisons(Collider2D other)
    {

    }

    public Object getOwnerObject()
    {
        return ownerObject;
    }

    public void setOwnerObject(Object ownerObject)
    {
        this.ownerObject = ownerObject;
    }

    public ColliderType getColliderType()
    {
        return colliderType;
    }

    public CollisionCallback getCollisionCallback()
    {
        return collisionCallback;
    }

    public boolean getIsColliding()
    {
        return isColliding;
    }

    public void setIsColliding(boolean isColl)
    {
        this.isColliding = isColl;
    }

    public ColliderTag getColliderTag()
    {
        return colliderTag;
    }

    public void setColliderTag(ColliderTag colliderTag)
    {
        this.colliderTag = colliderTag;
    }

    public Vector2 getPos()
    {
        return pos;
    }

    public void setPos(Vector2 pos)
    {
        this.pos = pos;
    }
}
