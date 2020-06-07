package doodlejump.client.game;

import org.jetbrains.annotations.Contract;

import java.util.Random;

public abstract class ChunkGenerator {
    protected final int difficulty;
    protected double windowWidth;
    protected double windowHeight;
    private long seed;

    @Contract(pure = true)
    public ChunkGenerator(int difficulty) {
        this.difficulty = difficulty;
    }

    public void setSeed(long seed) {
        this.seed = seed;
    }

    public void setWindowWidth(double windowWidth) {
        this.windowWidth = windowWidth;
    }

    public void setWindowHeight(double windowHeight) {
        this.windowHeight = windowHeight;
    }

    public abstract Chunk generateChunk(double startY, boolean usePickup);

    public int getDifficulty() {
        return difficulty;
    }

    protected Random getRandom(double y) {
        return new Random(new Random(new Random(seed + (long) y).nextLong()).nextLong());
    }

    public boolean hasPickup(double startY) {
        if (startY < 50.0) {
            return false; // 0% chance
        } else {
            return getRandom(startY).nextDouble() < .50; // 50% chance
        }
    }
}
