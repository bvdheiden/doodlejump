package doodlejump.client.game;

import doodlejump.client.game.generators.LongJumpGenerator;
import doodlejump.client.game.generators.VariedJumpGenerator;
import doodlejump.core.networking.Player;
import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.transform.Affine;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class GameView extends AnchorPane implements ChunkLoader.@Nullable ChunkLoadListener, ChunkLoader.@Nullable ChunkUnloadListener {
    public static final double WINDOW_WIDTH = 400.0;
    public static final double WINDOW_HEIGHT = 800.0;
    protected final List<Chunk> activeChunks = new ArrayList<>();
    private final Canvas canvas;
    private final GraphicsContext graphicsContext;
    private final DeltaTimer drawTimer = new DeltaTimer(1.0 / 60, true, true);
    private final DeltaTimer fixedUpdateTimer = new DeltaTimer(1.0 / 120, true, true);
    private final ChunkLoader chunkLoader;
    protected double minCameraY = 0.0;
    protected Player player;
    private boolean playing;
    private Affine preTransform;

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

    public void start(long seed, Player player) {
        this.playing = true;
        this.player = player;

        player.setPosition(0, 0);

        chunkLoader.setSeed(seed);
    }

    public void stop() {
        this.playing = false;
        this.player = null;

        minCameraY = 0.0;

        if (player != null) {
            player.setPosition(0, 0);
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

                handleAnimationLoop(deltaTime);

                last = now;
            }
        }.start();
    }

    protected void handleAnimationLoop(double deltaTime) {
        update(deltaTime);

        drawTimer.update(deltaTime);
        if (drawTimer.timeout()) {
            preDraw(graphicsContext);
            draw(graphicsContext);
            postDraw(graphicsContext);
        }

        fixedUpdateTimer.update(deltaTime);
        while (fixedUpdateTimer.timeout())
            fixedUpdate(fixedUpdateTimer.getWait());
    }

    /**
     * Run the update loop
     *
     * @param deltaTime time difference in nano seconds
     */
    protected void update(double deltaTime) {
        // update logic here
    }

    /**
     * Run the fixed update loop
     *
     * @param deltaTime time difference in nano seconds
     */
    protected void fixedUpdate(double deltaTime) {
        // fixed update logic here

        chunkLoader.onPlayerMovement(player.getX(), player.getY());
        minCameraY = Math.min(minCameraY, -(player.getY() - WINDOW_HEIGHT / 2.0) - WINDOW_HEIGHT - 40);
    }

    private void preDraw(GraphicsContext graphicsContext) {
        this.preTransform = graphicsContext.getTransform();
        graphicsContext.setFill(Color.rgb(210, 255, 254));
        graphicsContext.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        graphicsContext.scale(1, -1);
        graphicsContext.translate(0, minCameraY);
    }

    /**
     * Draw the graphics
     *
     * @param graphicsContext graphics context
     */
    protected void draw(GraphicsContext graphicsContext) {
        graphicsContext.setFill(Color.rgb(200, 40, 40));
        graphicsContext.fillRect(player.getX() - Player.WIDTH / 2.0, player.getY() - Player.HEIGHT / 2.0, Player.WIDTH, Player.HEIGHT);

        for (Chunk chunk : activeChunks) {
            graphicsContext.setFill(Color.rgb(40, 150, 50));
            for (Platform platform : chunk.getPlatformList()) {
                graphicsContext.fillRect(platform.getX(), platform.getY(), platform.getWidth(), platform.getHeight());
            }
        }
    }

    private void postDraw(GraphicsContext graphicsContext) {
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
