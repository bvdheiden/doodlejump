package doodlejump.client.game.generators;

import doodlejump.client.game.Chunk;
import doodlejump.client.game.ChunkGenerator;
import doodlejump.client.game.pickups.Pickup;
import doodlejump.client.game.Platform;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Simple generator for normal platforming experience.
 * Generates only 1 path upwards without pickups.
 */
public abstract class SimpleGenerator extends ChunkGenerator {


    public SimpleGenerator(int difficulty) {
        super(difficulty);
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
            double platformWidth = getRandomPlatformWidth(getRandom(nextPlatformY));
            double platformX = getRandomPlatformX(getRandom(nextPlatformY), platformWidth, windowWidth);

            Platform platform = new Platform(platformX, platformY, platformWidth);
            platformList.add(platform);

            nextPlatformY = platformY + getRandomPlatformDistance(distanceRandom);
        }

        return new Chunk(platformList, pickupList, startY, nextPlatformY);
    }

    protected abstract int getRandomPlatformAmount(Random random);

    protected abstract double getRandomPlatformDistance(Random random);

    protected abstract double getRandomPlatformWidth(Random random);

    protected abstract double getRandomPlatformX(Random random, double platformWidth, double windowWidth);
}
