package doodlejump.client.game.generators;

import java.util.Random;

public class VariedJumpGenerator extends SimpleGenerator {
    private static final int MIN_PLATFORMS = 4;
    private static final int MAX_PLATFORMS = 10;
    private static final double MIN_PLATFORM_DISTANCE = 40.0;
    private static final double MAX_PLATFORM_DISTANCE = 160.0;
    private static final double MIN_PLATFORM_WIDTH = 100.0;
    private static final double MAX_PLATFORM_WIDTH = 200.0;

    public VariedJumpGenerator(int difficulty) {
        super(difficulty);
    }

    protected int getRandomPlatformAmount(Random random) {
        return MIN_PLATFORMS + random.nextInt(MAX_PLATFORMS - MIN_PLATFORMS);
    }

    protected double getRandomPlatformDistance(Random random) {
        return MIN_PLATFORM_DISTANCE + (random.nextDouble() * (MAX_PLATFORM_DISTANCE - MIN_PLATFORM_DISTANCE));
    }

    protected double getRandomPlatformWidth(Random random) {
        return MIN_PLATFORM_WIDTH + (random.nextDouble() * (MAX_PLATFORM_WIDTH - MIN_PLATFORM_WIDTH));
    }

    protected double getRandomPlatformX(Random random, double platformWidth, double windowWidth) {
        double minX = 0;
        double maxX = windowWidth - platformWidth;

        return minX + (random.nextDouble() * (maxX - minX));
    }
}
