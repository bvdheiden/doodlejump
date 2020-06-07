package doodlejump.client.game.generators;

import doodlejump.client.game.Chunk;
import doodlejump.client.game.ChunkGenerator;
import doodlejump.client.game.Platform;
import doodlejump.client.game.pickups.Bomb;
import doodlejump.client.game.pickups.Pickup;
import doodlejump.client.game.pickups.Wind;
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
    public @NotNull Chunk generateChunk(double startY, boolean usePickup) {
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

        if (usePickup && hasPickup(startY)) {
            Platform platform = platformList.get(getRandom(startY).nextInt(platformList.size()));

            double x = platform.getX() + platform.getWidth() / 2.0;
            double y = platform.getY() + platform.getHeight();

            if (getRandom(y).nextBoolean()) {
                pickupList.add(new Bomb(x - Bomb.RADIUS, y + Bomb.RADIUS));
            } else {
                pickupList.add(new Wind(x - Wind.RADIUS,y + Wind.RADIUS));
            }
        }

        return new Chunk(platformList, pickupList, startY, nextPlatformY);
    }

    protected abstract int getRandomPlatformAmount(Random random);

    protected abstract double getRandomPlatformDistance(Random random);

    protected abstract double getRandomPlatformWidth(Random random);

    protected abstract double getRandomPlatformX(Random random, double platformWidth, double windowWidth);
}
