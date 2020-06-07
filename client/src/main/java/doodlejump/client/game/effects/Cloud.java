package doodlejump.client.game.effects;

import javafx.scene.image.Image;

public class Cloud {
    public static double MIN_SPEED = 30;
    public static double MAX_SPEED = 100;
    public static double WIDTH = 100;
    public static double HEIGHT = 100;

    private final double speed;
    private final Image image;
    private double x;
    private double y;

    public Cloud(double speed, Image image, double x, double y) {
        this.speed = Math.max(MIN_SPEED, Math.min(MIN_SPEED, speed));
        this.image = image;
        this.x = x;
        this.y = y;
    }

    public void update(double deltaTime) {
        this.x -= speed * deltaTime;
    }

    public Image getImage() {
        return image;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}
