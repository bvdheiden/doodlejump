package doodlejump.client.game;

import doodlejump.client.game.collision.CollisionSystem;
import doodlejump.client.game.generators.LongJumpGenerator;
import doodlejump.client.game.generators.VariedJumpGenerator;
import doodlejump.client.networking.GameClient;
import doodlejump.core.networking.Player;
import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.transform.Affine;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class GameView extends AnchorPane implements ChunkLoader.@Nullable ChunkLoadListener, ChunkLoader.@Nullable ChunkUnloadListener {
    private static final double WINDOW_WIDTH = 400.0;
    private static final double WINDOW_HEIGHT = 800.0;
    private final Canvas canvas;
    private final GraphicsContext graphicsContext;
    private final DeltaTimer drawTimer = new DeltaTimer(1.0 / 60, true, true);
    private final DeltaTimer fixedUpdateTimer = new DeltaTimer(1.0 / 120, true, true);
    private final DeltaTimer uploadTimer = new DeltaTimer(1.0 / 30, true, true);
    private final List<Chunk> activeChunks = new ArrayList<>();
    private final ChunkLoader chunkLoader;
    private boolean isHost;
    private Player player;
    private CollisionSystem collisionSystem;
    private PlayerController playerController;
    private boolean playing;
    private boolean playerGiven = false;

    public GameView() {
        this.getChildren().add(this.canvas = new Canvas(WINDOW_WIDTH, WINDOW_HEIGHT));
        this.graphicsContext = this.canvas.getGraphicsContext2D();
        this.chunkLoader = new ChunkLoader(WINDOW_WIDTH, WINDOW_HEIGHT);

        // Request focus for detecting keyboard input
        canvas.requestFocus();
        canvas.setOnMouseClicked(event -> this.canvas.requestFocus());

        // Disable AA
        graphicsContext.setImageSmoothing(false);


        setupInterface();
        setupChunkLoading();
        setupAnimationLoop();

    }

    public void start(long seed, Player player, boolean isHost) {
        this.playing = true;
        this.player = player;
        this.isHost = isHost;

        collisionSystem = CollisionSystem.INSTANCE;
        if(isHost)
        {
            playerController = new PlayerController(player);
        }

        addEventFilter(KeyEvent.KEY_PRESSED,
                new EventHandler<KeyEvent>() {
                    public void handle(KeyEvent e) {
                        playerController.OnKeyPress(e);
                    }

                    ;
                });

        player.setPosition(0, 0);
        player.setVelocity(0, 0);

        chunkLoader.setSeed(seed);
    }

    public void stop() {
        this.playing = false;
        this.player = null;
        this.isHost = false;

        if (player != null) {
            player.setPosition(0, 0);
            player.setVelocity(0, 0);
        }

        chunkLoader.reset();
    }

    private void setupInterface() {
        Label difficultyLabel = new Label("Difficulty: 0");
        difficultyLabel.setFont(new Font(20));
        difficultyLabel.setTextFill(Color.WHITE);
        AnchorPane.setTopAnchor(difficultyLabel, 20.0);
        AnchorPane.setRightAnchor(difficultyLabel, 20.0);

        chunkLoader.chunkDifficultyProperty().addListener((observable, oldValue, newValue) -> {
            difficultyLabel.setText("Difficulty: " + newValue);
        });

        getChildren().addAll(difficultyLabel);
    }

    private void setupChunkLoading() {
        chunkLoader.addGenerator(new VariedJumpGenerator(0));
        chunkLoader.addGenerator(new LongJumpGenerator(10));
        chunkLoader.setOnChunkLoad(this);
        chunkLoader.setOnChunkUnload(this);
    }

    /**
     * Setup the timed animation loop
     */
    private void setupAnimationLoop() {
        new AnimationTimer() {
            long last = -1;

            @Override
            public void handle(long now) {
                if (!playing)
                    return;

                if (last == -1) last = now;
                double deltaTime = (now - last) / 1000000000.0;

                update(deltaTime);

                drawTimer.update(deltaTime);
                if (drawTimer.timeout())
                    draw(graphicsContext);

                fixedUpdateTimer.update(deltaTime);
                while (fixedUpdateTimer.timeout())
                    fixedUpdate(fixedUpdateTimer.getWait());

                if (isHost) {
                    uploadTimer.update(deltaTime);
                    if (uploadTimer.timeout())
                        GameClient.INSTANCE.sendPosition();
                }

                last = now;
            }
        }.start();
    }

    /**
     * Run the update loop
     *
     * @param deltaTime time difference in nano seconds
     */
    public void update(double deltaTime) {
        // update logic here
        if(isHost) {
            playerController.update(deltaTime);
        }
        collisionSystem.CheckCollosions();
    }

    /**
     * Run the fixed update loop
     *
     * @param deltaTime time difference in nano seconds
     */
    public void fixedUpdate(double deltaTime) {
        // fixed update logic here

        chunkLoader.onPlayerMovement(player.getX(), player.getY());
    }

    /**
     * Draw the graphics
     *
     * @param graphicsContext graphics context
     */
    public void draw(GraphicsContext graphicsContext) {
        final Affine preTransform = graphicsContext.getTransform();
        graphicsContext.setFill(Color.rgb(210,255,254));
        graphicsContext.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        graphicsContext.scale(1, -1);
        graphicsContext.translate(0, -(player.getY()-100) - WINDOW_HEIGHT + 80);

        // draw logic here
        if(isHost)
        {
            playerController.Draw(graphicsContext);
        };

        graphicsContext.setStroke(Color.BLUE);
        graphicsContext.strokeLine(0, player.getY(), WINDOW_WIDTH, player.getY());

        for (Chunk chunk : activeChunks) {
            graphicsContext.setStroke(Color.GREEN);
            graphicsContext.strokeRect(0, chunk.getStartY(), WINDOW_WIDTH, chunk.getEndY() - chunk.getStartY());

            graphicsContext.setFill(Color.rgb(255,239,208));
            for (Platform platform : chunk.getPlatformList()) {
                graphicsContext.fillRect(platform.getX(), platform.getY(), platform.getWidth(), platform.getHeight());
            }
        }
        //uncomment to see colliders
        //collisionSystem.DebugDraw(graphicsContext);

        graphicsContext.setTransform(preTransform);
    }

    @Override
    public void onChunkLoad(Chunk chunk) {
        System.out.println("Chunk loaded");

        activeChunks.add(chunk);
    }

    @Override
    public void onChunkUnload(Chunk chunk) {
        System.out.println("Chunk unloaded");

        activeChunks.remove(chunk);
    }
}
