package doodlejump.client.game.drawing;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;


public class Rectangle
{
    private Renderable square2D;
    private Color rectangleColor;
    private Image art;
    private int xSizeOffset;
    private int ySizeOffset;

    public Rectangle(int xPos,int yPos,int xSize, int ySize, float rotation)
    {
        this.square2D = new Renderable(new Rectangle2D.Double(-(xSize/2),-(ySize/2),xSize,ySize), new Point2D.Double(xPos,yPos), rotation, 1);
        this.xSizeOffset = xSize/2;
        this.ySizeOffset = ySize/2;
        SetImageByFilePath("assets/Default.png");
    }

    public void SetImageByFilePath(String filePath)
    {
        File file = new File(filePath);

        try {
            art = ImageIO.read(file);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void Draw(Graphics2D graphics2D)
    {
        graphics2D.setColor(rectangleColor);
        graphics2D.draw(square2D.getTransformedShape());
    }

    public void FilledDraw(Graphics2D graphics2D)
    {
        graphics2D.setColor(rectangleColor);
        graphics2D.fill(square2D.getTransformedShape());
        graphics2D.draw(square2D.getTransformedShape());
    }

    public void ImageDraw(Graphics2D graphics2D)
    {
        graphics2D.drawImage(art,square2D.getTransform(xSizeOffset, ySizeOffset),null);
    }


    //getters and setters from here
    public Color getRectangleColor()
    {
        return rectangleColor;
    }

    public void setRectangleColor(Color rectangleColor)
    {
        this.rectangleColor = rectangleColor;
    }

    public Renderable getSquare2D()
    {
        return square2D;
    }

    public void setSquare2D(Renderable square2D)
    {
        this.square2D = square2D;
    }

    public Image getArt()
    {
        return art;
    }

    public void setArt(Image art)
    {
        this.art = art;
    }
}
