package doodlejump.client.game;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;

public class GameView extends AnchorPane {
    private final Canvas canvas;
    private final GraphicsContext graphicsContext;

    private final DeltaTimer drawTimer = new DeltaTimer(1.0 / 60, true, true);
    private final DeltaTimer fixedUpdateTimer = new DeltaTimer(1.0 / 120, true, true);

    public GameView() {
        this.getChildren().add(this.canvas = new Canvas(getWidth(), getHeight()));
        this.graphicsContext = this.canvas.getGraphicsContext2D();

        // Bind parent dimensions onto the canvas
        this.canvas.widthProperty().bind(this.widthProperty());
        this.canvas.heightProperty().bind(this.heightProperty());

        // Request focus for detecting keyboard input
        this.canvas.requestFocus();
        this.canvas.setOnMouseClicked(event -> this.canvas.requestFocus());

        // Disable AA
        this.graphicsContext.setImageSmoothing(false);

        setupAnimationLoop();
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
                    fixedUpdate(deltaTime);

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
    }

    /**
     * Draw the graphics
     * @param graphicsContext graphics context
     */
    public void draw(GraphicsContext graphicsContext) {
        final Affine preTransform = graphicsContext.getTransform();
        graphicsContext.setFill(Color.BLACK);
        graphicsContext.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        // draw logic here

        graphicsContext.setTransform(preTransform);
    }
}
