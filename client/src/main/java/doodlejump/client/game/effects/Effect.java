package doodlejump.client.game.effects;

import javafx.scene.canvas.GraphicsContext;

public class Effect {
    protected boolean shouldBeRemoved = false;

    public void activate() {

    }

    public void update(double deltaTime) {

    }

    public void draw(GraphicsContext graphicsContext) {

    }

    public void remove() {

    }

    public boolean isShouldBeRemoved() {
        return shouldBeRemoved;
    }
}
