package doodlejump.client.game.collision;

import doodlejump.client.game.collision.colliders.BoxCollider;
import doodlejump.client.game.collision.colliders.Collider2D;
import doodlejump.client.game.collision.enums.ColliderType;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public enum CollisionSystem {
    INSTANCE;

    private ArrayList<Collider2D> allColliders;
    private ArrayList<Collider2D> collidersToBeAdded;
    private ArrayList<Collider2D> collidersToBeRemoved;

    private boolean shouldAddToColliders = false;
    private boolean shouldRemoveFromColliders = false;

    CollisionSystem() {
        allColliders = new ArrayList<Collider2D>();
        collidersToBeAdded = new ArrayList<Collider2D>();
        collidersToBeRemoved = new ArrayList<Collider2D>();
    }

    public void CheckCollosions() {
        boolean hasCollided;

        for (int i = 0; i < allColliders.size(); i++) {
            allColliders.get(i).getPreCollisionCallback().run();
        }

        for (int i = 0; i < allColliders.size(); i++) {
            hasCollided = false;
            for (int j = 0; j < allColliders.size(); j++) {
                if (i != j) {
                    if (allColliders.get(i).Collide(allColliders.get(j).getColliderType(), allColliders.get(j))) {
                        hasCollided = true;
                        allColliders.get(i).getCollisionCallback().collide(allColliders.get(j));
                        allColliders.get(j).getCollisionCallback().collide(allColliders.get(i));
                    }
                }
            }
            allColliders.get(i).setIsColliding(hasCollided);
        }

        if (shouldAddToColliders) {
            for (Collider2D c : collidersToBeAdded) {
                allColliders.add(c);
            }
            collidersToBeAdded.clear();
            shouldAddToColliders = false;
        }

        if (shouldRemoveFromColliders) {
            for (Collider2D c : collidersToBeRemoved) {
                allColliders.remove(c);
            }
            collidersToBeRemoved.clear();
            shouldRemoveFromColliders = false;
        }
    }

    public void DebugDraw(GraphicsContext graphicsContext) {
        graphicsContext.setStroke(Color.YELLOW);
        for(Collider2D col : allColliders)
        {
            if(col.getColliderType() == ColliderType.BOX_COLLIDER)
            {
                BoxCollider box = (BoxCollider)col;
                graphicsContext.strokeRect(box.getPos().x-box.width*0.5, box.getPos().y-box.height*0.5, box.width, box.height);
            }
        }
    }

    public ArrayList<Collider2D> getAllColliders() {
        return allColliders;
    }

    public void setAllColliders(ArrayList<Collider2D> allColliders) {
        this.allColliders = allColliders;
    }

    public ArrayList<Collider2D> getCollidersToBeAdded() {
        return collidersToBeAdded;
    }

    public void setCollidersToBeAdded(ArrayList<Collider2D> collidersToBeAdded) {
        this.collidersToBeAdded = collidersToBeAdded;
    }

    public ArrayList<Collider2D> getCollidersToBeRemoved() {
        return collidersToBeRemoved;
    }

    public void setCollidersToBeRemoved(ArrayList<Collider2D> collidersToBeRemoved) {
        this.collidersToBeRemoved = collidersToBeRemoved;
    }

    public boolean isShouldAddToColliders() {
        return shouldAddToColliders;
    }

    public void setShouldAddToColliders(boolean shouldAddToColliders) {
        this.shouldAddToColliders = shouldAddToColliders;
    }

    public boolean isShouldRemoveFromColliders() {
        return shouldRemoveFromColliders;
    }

    public void setShouldRemoveFromColliders(boolean shouldRemoveFromColliders) {
        this.shouldRemoveFromColliders = shouldRemoveFromColliders;
    }
}
