package doodlejump.client.game;

import org.jetbrains.annotations.Contract;

import java.util.Random;

public abstract class ChunkGenerator {
    private final long seed;

    protected final int difficulty;
    protected final double windowWidth;
    protected final double windowHeight;

    @Contract(pure = true)
    public ChunkGenerator(long seed, int difficulty, double windowWidth, double windowHeight) {
        this.seed = seed;
        this.difficulty = difficulty;
        this.windowWidth = windowWidth;
        this.windowHeight = windowHeight;
    }

    public abstract Chunk generateChunk(double startY);

    public int getDifficulty() {
        return difficulty;
    }

    protected Random getRandom(double y) {
        return new Random(seed + (long) y);
    }
}
