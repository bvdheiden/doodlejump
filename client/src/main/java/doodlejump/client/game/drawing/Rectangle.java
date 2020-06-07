package doodlejump.client.game.drawing;

import doodlejump.client.game.collision.Vector2;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Rectangle {
    private final int xSize;
    private final int ySize;
    private final int xSizeOffset;
    private final int ySizeOffset;
    private Color rectangleColor;
    private Image art;
    private Vector2 pos;
    private int xPos;
    private int yPos;

    public Rectangle(int xPos, int yPos, int xSize, int ySize) {
        this.pos = new Vector2(xPos, yPos);
        this.xSizeOffset = xSize / 2;
        this.ySizeOffset = ySize / 2;
        this.xSize = xSize;
        this.ySize = ySize;
        this.xPos = xPos;
        this.yPos = yPos;
        this.rectangleColor = Color.ALICEBLUE;
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
        graphicsContext.setStroke(rectangleColor);
        graphicsContext.strokeRect(xPos, yPos, xSize, ySize);
    }

    public void filledDraw(GraphicsContext graphicsContext) {
        graphicsContext.setFill(rectangleColor);
        graphicsContext.fillRect(xPos, yPos, xSize, ySize);
    }

    public void imageDraw(GraphicsContext graphicsContext) {
        graphicsContext.drawImage(art, xPos, yPos, xSize, ySize);
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
    public Color getRectangleColor() {
        return rectangleColor;
    }

    public void setRectangleColor(Color rectangleColor) {
        this.rectangleColor = rectangleColor;
    }

    public Image getArt() {
        return art;
    }

    public void setArt(Image art) {
        this.art = art;
    }
}
