package doodlejump.client.game;

import doodlejump.core.networking.listeners.PlayerMovementListener;
import javafx.beans.property.SimpleIntegerProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class ChunkLoader implements PlayerMovementListener {
    private final LinkedList<Chunk> chunkList = new LinkedList<>();
    private final List<ChunkGenerator> generatorList = new ArrayList<>();
    private final double windowWidth;
    private final double windowHeight;
    private final SimpleIntegerProperty chunkDifficulty = new SimpleIntegerProperty();
    private long seed;
    private ChunkLoadListener chunkLoadListener;
    private ChunkUnloadListener chunkUnloadListener;

    public ChunkLoader(double windowWidth, double windowHeight) {
        this.windowWidth = windowWidth;
        this.windowHeight = windowHeight;
    }

    public long getSeed() {
        return seed;
    }

    public void setSeed(long seed) {
        this.seed = seed;

        for (ChunkGenerator generator : generatorList)
            generator.setSeed(seed);
    }

    public void addGenerator(@NotNull ChunkGenerator generator) {
        generator.setSeed(seed);
        generator.setWindowWidth(windowWidth);
        generator.setWindowHeight(windowHeight);
        generatorList.add(generator);
    }

    public int getChunkDifficulty() {
        return chunkDifficulty.get();
    }

    public SimpleIntegerProperty chunkDifficultyProperty() {
        return chunkDifficulty;
    }

    public void reset() {
        for (Chunk chunk : chunkList)
            if (chunkUnloadListener != null)
                chunkUnloadListener.onChunkUnload(chunk);
        chunkList.clear();
    }

    @Override
    public void onPlayerMovement(double newX, double newY) {
        if (chunkList.size() == 0)
            initialize();

        // check if player is well passed a chunk
        if (newY - 800 > chunkList.getFirst().getEndY()) {
            unloadChunk();
        }

        if (newY + 1000 > chunkList.getLast().getEndY()) {
            loadChunk();
        }

        chunkDifficulty.set(calculateChunkDifficulty(newY));
    }

    public void setOnChunkLoad(@Nullable ChunkLoadListener listener) {
        this.chunkLoadListener = listener;
    }

    public void setOnChunkUnload(@Nullable ChunkUnloadListener listener) {
        this.chunkUnloadListener = listener;
    }

    private void initialize() {
        double lastEndY = 0.0;
        while (lastEndY < windowHeight) {
            loadChunk();
            lastEndY = chunkList.getLast().getEndY();
        }
    }

    private void loadChunk() {
        chunkList.addLast(generateChunk());
        if (chunkLoadListener != null)
            chunkLoadListener.onChunkLoad(chunkList.getLast());
    }

    private void unloadChunk() {
        if (chunkUnloadListener != null)
            chunkUnloadListener.onChunkUnload(chunkList.getFirst());
        Chunk unloadedChunk = chunkList.pop();
        unloadedChunk.OnDestroy();
    }

    private @NotNull Chunk generateChunk() {
        // check if generators are present
        if (generatorList.size() == 0)
            throw new IllegalStateException("Generators must first be added.");

        // get generation parameters
        double chunkStartY = chunkList.size() > 0 ? chunkList.getLast().getEndY() : 0.0;
        int chunkDifficulty = calculateChunkDifficulty(chunkStartY);

        // filter chunk generators for difficulty
        List<ChunkGenerator> chunkGeneratorList = this.generatorList.stream()
                .filter(generator -> chunkDifficulty >= generator.getDifficulty())
                .collect(Collectors.toUnmodifiableList());

        // check if generators for this difficulty are present
        if (chunkGeneratorList.size() == 0)
            throw new IllegalStateException("No generators exist for this difficulty: " + chunkDifficulty);

        // select random generator
        Random random = new Random(seed + (long) chunkStartY);
        ChunkGenerator chunkGenerator = chunkGeneratorList.get(random.nextInt(chunkGeneratorList.size()));

        boolean usePickup = true;

        if (chunkList.size() > 0) {
            Chunk lastChunk = chunkList.getLast();

            if (lastChunk.getPickupList().size() > 0) {
                usePickup = false;
            }
        }

        // generate chunk
        return chunkGenerator.generateChunk(chunkStartY, usePickup);
    }

    private int calculateChunkDifficulty(double startY) {
        if (startY == 0.0)
            return 0;

        return (int) (startY / 100.0);
    }

    @FunctionalInterface
    public interface ChunkLoadListener {
        void onChunkLoad(Chunk chunk);
    }

    @FunctionalInterface
    public interface ChunkUnloadListener {
        void onChunkUnload(Chunk chunk);
    }
}
