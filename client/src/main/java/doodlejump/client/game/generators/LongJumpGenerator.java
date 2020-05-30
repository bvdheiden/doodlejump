package doodlejump.client.game.generators;

import java.util.Random;

public class LongJumpGenerator extends SimpleGenerator {
    private static final int MIN_PLATFORMS = 2;
    private static final int MAX_PLATFORMS = 4;
    private static final double PLATFORM_DISTANCE = 160.0;
    private static final double MIN_PLATFORM_WIDTH = 100.0;
    private static final double MAX_PLATFORM_WIDTH = 200.0;

    public LongJumpGenerator(int difficulty) {
        super(difficulty);
    }

    @Override
    protected int getRandomPlatformAmount(Random random) {
        return MIN_PLATFORMS + random.nextInt(MAX_PLATFORMS - MIN_PLATFORMS);
    }

    @Override
    protected double getRandomPlatformDistance(Random random) {
        return PLATFORM_DISTANCE;
    }

    @Override
    protected double getRandomPlatformWidth(Random random) {
        return MIN_PLATFORM_WIDTH + (random.nextDouble() * (MAX_PLATFORM_WIDTH - MIN_PLATFORM_WIDTH));
    }

    @Override
    protected double getRandomPlatformX(Random random, double platformWidth, double windowWidth) {
        double minX = 0;
        double maxX = windowWidth - platformWidth;

        return minX + (random.nextDouble() * (maxX - minX));
    }
}
