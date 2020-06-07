package doodlejump.client.game.drawing;

import doodlejump.client.game.collision.Vector2;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Circle {
    private Color circleColor;
    private Image art;
    private Vector2 pos;
    private int radius;
    private int halfRadius;
    private int xPos;
    private int yPos;

    public Circle(int xPos, int yPos, int radius, float rotation) {
        this.pos = new Vector2(xPos, yPos);
        this.radius = radius;
        this.halfRadius = radius / 2;
        this.xPos = xPos;
        this.yPos = yPos;
        //SetImageByFilePath("assets/Default.png");
    }

    public void setImageByFilePath(String filePath) {
        try {
            this.art = new Image(new FileInputStream(filePath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void draw(GraphicsContext graphicsContext) {
        graphicsContext.setStroke(circleColor);
        graphicsContext.strokeOval(xPos, yPos, radius, radius);
    }

    public void filledDraw(GraphicsContext graphicsContext) {
        graphicsContext.setFill(circleColor);
        graphicsContext.fillOval(xPos, yPos, radius, radius);
    }

    public void filledDrawWithLine(GraphicsContext graphicsContext, Color lineColor) {
        graphicsContext.setStroke(circleColor);
        graphicsContext.fillOval(xPos, yPos, radius, radius);
        graphicsContext.setStroke(lineColor);
        graphicsContext.strokeOval(xPos, yPos, radius, radius);
    }

    public void changePos(int newXPos, int newYPos) {
        pos.x = newXPos;
        pos.y = newYPos;
        xPos = newXPos;
        yPos = newYPos;
    }

    public void changePos(Vector2 newPos) {
        pos = newPos;
        xPos = (int) newPos.x;
        yPos = (int) newPos.y;
    }

    //getters and setters from here
    public Color getCircleColor() {
        return circleColor;
    }

    public void setCircleColor(Color circleColor) {
        this.circleColor = circleColor;
    }

    public Image getArt() {
        return art;
    }

    public void setArt(Image art) {
        this.art = art;
    }
}
