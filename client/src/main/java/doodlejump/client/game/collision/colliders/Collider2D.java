package doodlejump.client.game.collision.colliders;

import doodlejump.client.game.collision.Callback;
import doodlejump.client.game.collision.CollisionCallback;
import doodlejump.client.game.collision.CollisionSystem;
import doodlejump.client.game.collision.Vector2;
import doodlejump.client.game.collision.enums.ColliderTag;
import doodlejump.client.game.collision.enums.ColliderType;

public abstract class Collider2D
{
    protected Object ownerObject = null;
    protected ColliderTag colliderTag = ColliderTag.DEFAULT;
    protected ColliderType colliderType;
    protected boolean isColliding;

    public CollisionCallback collisionCallback;

    public Callback preCollisionCallback;

    protected Vector2 pos;

    protected Collider2D() {
        CollisionSystem.INSTANCE.setShouldAddToColliders(true);
        CollisionSystem.INSTANCE.getCollidersToBeAdded().add(this);
    }

    //check for collision
    public boolean Collide(ColliderType collideWith, Collider2D other) {
        return false;
    }

    //collisionChecks
    protected boolean CircleCollision(CircleCollider other) {
        System.out.println("the collision checks should not call the parent class");
        return false;
    }

    protected boolean BoxCollision(BoxCollider other) {
        System.out.println("the collision checks should not call the parent class");
        return false;
    }

    protected boolean ContainsPoint(Vector2 point) {
        System.out.println("the collision checks should not call the parent class");
        return false;
    }
    /////////////////

    public void OnDestroy() {
        CollisionSystem.INSTANCE.setShouldRemoveFromColliders(true);
        CollisionSystem.INSTANCE.getCollidersToBeRemoved().add(this);
    }

    public void PreCollisons()
    {

    }

    public void UpdateCollisons(Collider2D other) {

    }

    public Object getOwnerObject() {
        return ownerObject;
    }

    public void setOwnerObject(Object ownerObject) {
        this.ownerObject = ownerObject;
    }

    public ColliderType getColliderType() {
        return colliderType;
    }

    public Callback getPreCollisionCallback() {
        return preCollisionCallback;
    }

    public CollisionCallback getCollisionCallback() {
        return collisionCallback;
    }

    public boolean getIsColliding() {
        return isColliding;
    }

    public void setIsColliding(boolean isColl) {
        this.isColliding = isColl;
    }

    public ColliderTag getColliderTag() {
        return colliderTag;
    }

    public void setColliderTag(ColliderTag colliderTag) {
        this.colliderTag = colliderTag;
    }

    public Vector2 getPos() {
        return pos;
    }

    public void setPos(Vector2 pos) {
        this.pos = pos;
    }

    public void setPos(double xPos, double yPos) {
        this.pos.x = xPos;
        this.pos.y = yPos;
    }
}
