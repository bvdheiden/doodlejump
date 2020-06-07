package doodlejump.client.game.effects;

import doodlejump.client.game.DeltaTimer;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CloudManager {
    private static final int MAX_CLOUDS = 10;
    private static final String CLOUD_1 = "cloud1.png";
    private static final String CLOUD_2 = "cloud2.png";
    private static final String CLOUD_3 = "cloud3.png";

    private final DeltaTimer cloudSpawnTimer = new DeltaTimer(1, true, true);
    private final List<Cloud> cloudList = new ArrayList<>();
    private final List<Image> imageList = new ArrayList<>();
    private final double screenWidth;
    private final double screenHeight;

    public CloudManager(double screenWidth, double screenHeight) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;

        String[] cloudFiles = {CLOUD_1, CLOUD_2, CLOUD_3};
        for (String cloudFile : cloudFiles) {
            try {
                Image image = new Image(getClass().getClassLoader().getResourceAsStream("images/" + cloudFile));

                imageList.add(image);
            } catch (Exception exception) {
                System.out.println("Failed to load image: " + cloudFile);
            }
        }
    }

    public void update(double deltaTime) {
        cloudSpawnTimer.update(deltaTime);
        if (cloudSpawnTimer.timeout() && cloudList.size() < MAX_CLOUDS) {
            spawnCloud();
            cloudSpawnTimer.setWait(2 + (Math.random() * 3));
        }

        List<Cloud> removeClouds = new ArrayList<>();

        for (Cloud cloud : cloudList) {
            cloud.update(deltaTime);

            if (cloud.getX() + Cloud.WIDTH < 0) {
                removeClouds.add(cloud);
            }
        }

        for (Cloud cloud : removeClouds) {
            cloudList.remove(cloud);
        }
    }

    public void draw(GraphicsContext gc) {
        for (Cloud cloud : cloudList) {
            gc.drawImage(cloud.getImage(), cloud.getX(), -cloud.getY(), Cloud.WIDTH, Cloud.HEIGHT);
        }
    }

    private void spawnCloud() {
        double speed = Cloud.MIN_SPEED + (Math.random() * (Cloud.MAX_SPEED - Cloud.MIN_SPEED));
        Image image = imageList.get(new Random().nextInt(imageList.size()));
        double y = Cloud.HEIGHT + (Math.random() * (screenHeight - Cloud.HEIGHT));

        Cloud cloud = new Cloud(speed, image, screenWidth, y);

        System.out.println("Cloud spawned");

        cloudList.add(cloud);
    }
}
