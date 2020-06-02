package doodlejump.client.game.collision;

import doodlejump.client.game.collision.colliders.Collider2D;

import java.util.ArrayList;

public enum CollisionSystem
{
    INSTANCE;

    public ArrayList<Collider2D> allColliders;

    CollisionSystem()
    {
        allColliders = new ArrayList<Collider2D>();
    }

    public void CheckCollosions()
    {
        boolean hasCollided;
        for (int i = 0; i < allColliders.size(); i++) {
            hasCollided = false;
            for (int j = 0; j < allColliders.size(); j++) {
                if(i != j)
                {
                    if(allColliders.get(i).Collide(allColliders.get(j).getColliderType(), allColliders.get(j)))
                    {
                        hasCollided = true;
                        allColliders.get(i).getCollisionCallback().collide(allColliders.get(j));
                        allColliders.get(j).getCollisionCallback().collide(allColliders.get(i));
                    }
                }
            }
            allColliders.get(i).setIsColliding(hasCollided);
        }
        //DebugDrawer.DebugCollision(allColliders);
    }
}
