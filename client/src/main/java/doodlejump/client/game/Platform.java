package doodlejump.client.game;

public class Platform {
    private static final double DEFAULT_WIDTH = 20.0;
    private static final double DEFAULT_HEIGHT = 4.0;

    private final double x;
    private final double y;
    private final double width;
    private final double height;

    public Platform(double x, double y) {
        this(x, y, DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    public Platform(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }
}
