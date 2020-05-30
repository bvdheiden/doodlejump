package doodlejump.client.game;

import org.jetbrains.annotations.Contract;

import java.util.Random;

public abstract class ChunkGenerator {
    private long seed;

    protected final int difficulty;
    protected double windowWidth;
    protected double windowHeight;

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

    public abstract Chunk generateChunk(double startY);

    public int getDifficulty() {
        return difficulty;
    }

    protected Random getRandom(double y) {
        return new Random(new Random(seed + (long) y).nextLong());
    }
}
