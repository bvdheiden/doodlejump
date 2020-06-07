package doodlejump.client.game;

import doodlejump.client.game.collision.CollisionSystem;
import doodlejump.client.game.pickups.Pickup;
import doodlejump.client.networking.GameClient;
import doodlejump.client.sound.SoundPlayer;
import doodlejump.core.networking.Player;
import javafx.event.EventHandler;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;

public class PlayerGameView extends GameView
{
    private static final String BACKGROUND_MUSIC_SOUND_PATH = "bensoundFunnySong.wav";
    private static final String DEAD_SOUND = "ded.wav";

    private final DeltaTimer uploadTimer = new DeltaTimer(1.0 / 30, true, true);
    private PlayerController playerController;
    private EventHandler<? super KeyEvent> eventHandler;

    @Override
    public void start(long seed, Player player) {
        super.start(seed, player);

        this.playerController = new PlayerController(player);
        this.eventHandler = e -> playerController.onKeyPress(e);

        SoundPlayer.loop(BACKGROUND_MUSIC_SOUND_PATH);

        addEventFilter(KeyEvent.KEY_PRESSED, eventHandler);
    }

    @Override
    public void stop() {
        super.stop();

        CollisionSystem.INSTANCE.emptySystem();

        SoundPlayer.stop();

        if (eventHandler != null)
            removeEventFilter(KeyEvent.KEY_PRESSED, eventHandler);
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
        CollisionSystem.INSTANCE.checkCollosions();
    }

    @Override
    protected void fixedUpdate(double deltaTime) {
        super.fixedUpdate(deltaTime);

        double playerTop = player.getY() + Player.HEIGHT / 2.0;
        double viewBottom = -minCameraY - GameView.WINDOW_HEIGHT;

        if (playerTop < viewBottom) {
            System.out.println("U ded");

            SoundPlayer.play(DEAD_SOUND);

            stop();

            GameClient.INSTANCE.sendDead();
        }
    }

    @Override
    public void draw(GraphicsContext graphicsContext) {
        super.draw(graphicsContext);

        for (Chunk chunk : activeChunks) {
            for (Pickup pickup : chunk.getPickupList()) {
                graphicsContext.setFill(pickup.getPaint());
                graphicsContext.fillOval(pickup.getX(), pickup.getY(), pickup.getRadius(), pickup.getRadius());
            }

            graphicsContext.setStroke(Color.GREEN);
            graphicsContext.strokeRect(0, chunk.getStartY(), WINDOW_WIDTH, chunk.getEndY() - chunk.getStartY());
        }

        //uncomment to see colliders
        CollisionSystem.INSTANCE.debugDraw(graphicsContext);
    }

    @Override
    public void onChunkLoad(Chunk chunk) {
        super.onChunkLoad(chunk);
    }

    @Override
    public void onChunkUnload(Chunk chunk) {
        super.onChunkUnload(chunk);
    }

    public void onWind() {
        // @todo apply wind effect to player
    }

    public void onBomb() {
        // @todo spawn bomb above player
    }
}
