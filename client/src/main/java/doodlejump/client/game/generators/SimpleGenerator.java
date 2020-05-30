package doodlejump.client.game.generators;

import doodlejump.client.game.Chunk;
import doodlejump.client.game.ChunkGenerator;
import doodlejump.client.game.Pickup;
import doodlejump.client.game.Platform;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Simple generator for normal platforming experience.
 * Generates only 1 path upwards without pickups.
 */
public class SimpleGenerator extends ChunkGenerator {
    private static final int MIN_PLATFORMS = 4;
    private static final int MAX_PLATFORMS = 10;
    private static final double MIN_PLATFORM_DISTANCE = 20.0;
    private static final double MAX_PLATFORM_DISTANCE = 20.0;

    public SimpleGenerator(long seed, int difficulty, double windowWidth, double windowHeight) {
        super(seed, difficulty, windowWidth, windowHeight);
    }

    @Override
    public @NotNull Chunk generateChunk(double startY) {
        int platforms = getRandomPlatformAmount(getRandom(startY));

        List<Platform> platformList = new ArrayList<>();
        List<Pickup> pickupList = new ArrayList<>();

        Random distanceRandom = getRandom(startY);
        double nextPlatformY = startY;

        for (int i = 0; i < platforms; i++) {
            double platformY = nextPlatformY;
            double platformWidth = getPlatformWidth();
            double platformX = getRandomPlatformX(getRandom(nextPlatformY), platformWidth, windowWidth);

            Platform platform = new Platform(platformX, platformY, platformWidth);
            platformList.add(platform);

            nextPlatformY = platformY + getRandomPlatformDistance(distanceRandom);
        }

        return new Chunk(platformList, pickupList, startY, nextPlatformY);
    }

    private int getRandomPlatformAmount(Random random) {
        return MIN_PLATFORMS + random.nextInt(MAX_PLATFORMS - MIN_PLATFORMS);
    }

    private double getRandomPlatformDistance(Random random) {
        return MIN_PLATFORM_DISTANCE + (random.nextDouble() * (MAX_PLATFORM_DISTANCE - MIN_PLATFORM_DISTANCE));
    }

    private double getPlatformWidth() {
        return 20.0;
    }

    private double getRandomPlatformX(Random random, double platformWidth, double windowWidth) {
        double minX = platformWidth / 2.0;
        double maxX = windowWidth - minX;

        return minX + (random.nextDouble() * (maxX - minX));
    }
}
