package doodlejump.client.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PlatformGenerator {
    private final long seed;
    private final double gameWidth;
    private final double minPlatformDistance;
    private final double maxPlatformDistance;
    private final double minPlatformWidth;
    private final double maxPlatformWidth;

    private final List<Platform> platformList = new ArrayList<>();

    public PlatformGenerator(long seed, double gameWidth, double minPlatformDistance, double maxPlatformDistance, double minPlatformWidth, double maxPlatformWidth) {
        this.seed = seed;
        this.gameWidth = gameWidth;
        this.minPlatformDistance = minPlatformDistance;
        this.maxPlatformDistance = maxPlatformDistance;
        this.minPlatformWidth = minPlatformWidth;
        this.maxPlatformWidth = maxPlatformWidth;
    }

    public Platform getPlatform(int index) {
        if (platformList.size() < index) {
            for (int i = platformList.size(); i <= index; i++) {
                generatePlatform(i);
            }
        }

        return platformList.get(index);
    }

    private void generatePlatform(int index) {
        Random random = new Random(seed + index);
        double lastY = index <= 0 ? 0 : platformList.get(index - 1).getY();

        double width = minPlatformWidth + (random.nextDouble() * (maxPlatformWidth - minPlatformWidth));

        double x = (width / 2) + random.nextDouble() * (gameWidth - (width / 2));
        double y = lastY + minPlatformDistance + (random.nextDouble() * (maxPlatformDistance - minPlatformDistance));

        Platform platform = new Platform(x, y, width, 4.0);

        platformList.add(index, platform);
    }
}
