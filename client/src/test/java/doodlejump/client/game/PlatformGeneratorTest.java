package doodlejump.client.game;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PlatformGeneratorTest {
    private final double gameWidth = 200.0;
    private final double minPlatformDistance = 10.0;
    private final double maxPlatformDistance = 30.0;
    private final double minPlatformWidth = 10.0;
    private final double maxPlatformWidth = 30.0;

    @Test
    void getPlatform() {
        long seed = 1000;

        PlatformGenerator generator1 = new PlatformGenerator(seed, gameWidth, minPlatformDistance, maxPlatformDistance, minPlatformWidth, maxPlatformWidth);
        PlatformGenerator generator2 = new PlatformGenerator(seed, gameWidth, minPlatformDistance, maxPlatformDistance, minPlatformWidth, maxPlatformWidth);

        Platform platform1 = generator1.getPlatform(10);
        Platform platform2 = generator2.getPlatform(10);

        assertEquals(platform1.getX(), platform2.getX());
        assertEquals(platform1.getY(), platform2.getY());
        assertEquals(platform1.getWidth(), platform2.getWidth());
        assertEquals(platform1.getHeight(), platform2.getHeight());
    }
}
