package doodlejump.client.game;

import doodlejump.client.game.collision.CollisionSystem;
import doodlejump.client.networking.GameClient;
import doodlejump.core.networking.Player;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;

public class PlayerGameView extends GameView {
    private final DeltaTimer uploadTimer = new DeltaTimer(1.0 / 30, true, true);
    private CollisionSystem collisionSystem;
    private PlayerController playerController;

    @Override
    public void start(long seed, Player player) {
        super.start(seed, player);

        addEventFilter(KeyEvent.KEY_PRESSED, e -> playerController.OnKeyPress(e));
        collisionSystem = CollisionSystem.INSTANCE;
        playerController = new PlayerController(player);
    }

    @Override
    public void stop() {
        super.stop();
    }

    @Override
    protected void handleAnimationLoop(double deltaTime) {
        super.handleAnimationLoop(deltaTime);

        uploadTimer.update(deltaTime);
        if (uploadTimer.timeout())
            GameClient.INSTANCE.sendPosition();
    }

    @Override
    public void update(double deltaTime) {
        super.update(deltaTime);

        playerController.update(deltaTime);
        collisionSystem.CheckCollosions();
    }

    @Override
    public void draw(GraphicsContext graphicsContext) {
        super.draw(graphicsContext);

        for (Chunk chunk : activeChunks) {
            graphicsContext.setStroke(Color.GREEN);
            graphicsContext.strokeRect(0, chunk.getStartY(), WINDOW_WIDTH, chunk.getEndY() - chunk.getStartY());
        }

        //uncomment to see colliders
        collisionSystem.DebugDraw(graphicsContext);
    }

    @Override
    public void onChunkLoad(Chunk chunk) {
        super.onChunkLoad(chunk);
    }

    @Override
    public void onChunkUnload(Chunk chunk) {
        super.onChunkUnload(chunk);
    }
}
