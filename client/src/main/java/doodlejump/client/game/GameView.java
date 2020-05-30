package doodlejump.client.game;

import doodlejump.client.game.generators.LongJumpGenerator;
import doodlejump.client.game.generators.SimpleGenerator;
import doodlejump.client.game.generators.VariedJumpGenerator;
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
    private static final double WINDOW_WIDTH = 400.0;
    private static final double WINDOW_HEIGHT = 800.0;

    private final Canvas canvas;
    private final GraphicsContext graphicsContext;

    private final DeltaTimer drawTimer = new DeltaTimer(1.0 / 60, true, true);
    private final DeltaTimer fixedUpdateTimer = new DeltaTimer(1.0 / 120, true, true);

    private final ChunkLoader chunkLoader;
    private final List<Chunk> activeChunks = new ArrayList<>();

    private double tempPlayerY;

    public GameView(long seed) {
        this.getChildren().add(this.canvas = new Canvas(WINDOW_WIDTH, WINDOW_HEIGHT));
        this.graphicsContext = this.canvas.getGraphicsContext2D();
        this.chunkLoader = new ChunkLoader(seed, WINDOW_WIDTH, WINDOW_HEIGHT);

        // Request focus for detecting keyboard input
        canvas.requestFocus();
        canvas.setOnMouseClicked(event -> this.canvas.requestFocus());

        // Disable AA
        graphicsContext.setImageSmoothing(false);

        setupInterface();
        setupChunkLoading();
        setupAnimationLoop();
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
                if (last == -1) last = now;
                double deltaTime = (now - last) / 1000000000.0;

                update(deltaTime);

                drawTimer.update(deltaTime);
                if (drawTimer.timeout())
                    draw(graphicsContext);

                fixedUpdateTimer.update(deltaTime);
                while (fixedUpdateTimer.timeout())
                    fixedUpdate(fixedUpdateTimer.getWait());

                last = now;
            }
        }.start();
    }

    /**
     * Run the update loop
     * @param deltaTime time difference in nano seconds
     */
    public void update(double deltaTime) {
        // update logic here
    }

    /**
     * Run the fixed update loop
     * @param deltaTime time difference in nano seconds
     */
    public void fixedUpdate(double deltaTime) {
        // fixed update logic here

        tempPlayerY += 100 * deltaTime;

        chunkLoader.onPlayerMovement(0, tempPlayerY);
    }

    /**
     * Draw the graphics
     * @param graphicsContext graphics context
     */
    public void draw(GraphicsContext graphicsContext) {
        final Affine preTransform = graphicsContext.getTransform();
        graphicsContext.setFill(Color.BLACK);
        graphicsContext.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        graphicsContext.scale(1, -1);
        graphicsContext.translate(0, -tempPlayerY - WINDOW_HEIGHT + 80);

        // draw logic here

        graphicsContext.setStroke(Color.BLUE);
        graphicsContext.strokeLine(0, tempPlayerY, WINDOW_WIDTH, tempPlayerY);

        for (Chunk chunk : activeChunks) {
            graphicsContext.setStroke(Color.GREEN);
            graphicsContext.strokeRect(0, chunk.getStartY(), WINDOW_WIDTH, chunk.getEndY() - chunk.getStartY());

            graphicsContext.setFill(Color.RED);
            for (Platform platform : chunk.getPlatformList()) {
                graphicsContext.fillRect(platform.getX(), platform.getY(), platform.getWidth(), platform.getHeight());
            }
        }

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
