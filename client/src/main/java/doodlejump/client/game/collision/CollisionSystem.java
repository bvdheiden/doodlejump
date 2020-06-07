package doodlejump.client.game.collision;

import doodlejump.client.game.collision.colliders.BoxCollider;
import doodlejump.client.game.collision.colliders.CircleCollider;
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
    private boolean emptyTheSystem = false;

    CollisionSystem() {
        allColliders = new ArrayList<Collider2D>();
        collidersToBeAdded = new ArrayList<Collider2D>();
        collidersToBeRemoved = new ArrayList<Collider2D>();
    }

    public void checkCollosions() {
        boolean hasCollided;

        if (emptyTheSystem) {
            allColliders.clear();
            emptyTheSystem = false;
        }

        for (int i = 0; i < allColliders.size(); i++) {
            allColliders.get(i).getPreCollisionCallback().run();
        }

        for (int i = 0; i < allColliders.size(); i++) {
            hasCollided = false;
            for (int j = 0; j < allColliders.size(); j++) {
                if (i != j) {
                    if (allColliders.get(i).collide(allColliders.get(j).getColliderType(), allColliders.get(j))) {
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

    public void emptySystem() {
        emptyTheSystem = true;
    }

    public void debugDraw(GraphicsContext graphicsContext) {
        graphicsContext.setStroke(Color.YELLOW);
        for (Collider2D col : allColliders) {
            if (col.getColliderType() == ColliderType.BOX_COLLIDER) {
                BoxCollider box = (BoxCollider) col;
                graphicsContext.strokeRect(box.getPos().x - box.getWidth() * 0.5, box.getPos().y - box.getHeight() * 0.5, box.getWidth(), box.getHeight());
            } else if (col.getColliderType() == ColliderType.CIRCLE_COLLIDER) {
                CircleCollider circle = (CircleCollider) col;
                graphicsContext.strokeOval(circle.getPos().x - circle.getRadius(), circle.getPos().y - circle.getRadius(), circle.getRadius() * 2, circle.getRadius() * 2);
            }
        }
    }

    public ArrayList<Collider2D> getAllColliders() {
        return allColliders;
    }

    public ArrayList<Collider2D> getCollidersToBeAdded() {
        return collidersToBeAdded;
    }

    public ArrayList<Collider2D> getCollidersToBeRemoved() {
        return collidersToBeRemoved;
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
