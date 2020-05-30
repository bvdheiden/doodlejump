package doodlejump.client.game;

import doodlejump.core.networking.listeners.PlayerMovementListener;
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
    private final long seed;

    private ChunkLoadListener chunkLoadListener;
    private ChunkUnloadListener chunkUnloadListener;

    public ChunkLoader(long seed) {
        this.seed = seed;
    }

    public void addGenerator(@NotNull ChunkGenerator generator) {
        generatorList.add(generator);
    }

    /**
     * Fill the window with chunks.
     * @param windowHeight height of the window
     */
    public void initialize(double windowHeight) {
        double lastEndY = 0.0;
        while (lastEndY < windowHeight) {
            Chunk chunk = generateChunk();
            chunkList.addLast(chunk);
            lastEndY = chunk.getEndY();
        }
    }

    @Override
    public void onPlayerMovement(double newX, double newY) {
        // check if player is well passed a chunk
        if (newY + 200.0 > chunkList.getFirst().getEndY()) {
            unloadChunk();
            loadChunk();
        }
    }

    public void setOnChunkLoad(@Nullable ChunkLoadListener listener) {
        this.chunkLoadListener = listener;
    }

    public void setOnChunkUnload(@Nullable ChunkUnloadListener listener) {
        this.chunkUnloadListener = listener;
    }

    @FunctionalInterface
    public interface ChunkLoadListener {
        void onChunkLoad(Chunk chunk);
    }

    @FunctionalInterface
    public interface ChunkUnloadListener {
        void onChunkUnload(Chunk chunk);
    }

    private void loadChunk() {
        chunkList.addLast(generateChunk());
        if (chunkLoadListener != null)
            chunkLoadListener.onChunkLoad(chunkList.getLast());
    }

    private void unloadChunk() {
        if (chunkUnloadListener != null)
            chunkUnloadListener.onChunkUnload(chunkList.getFirst());
        chunkList.removeFirst();
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

        // generate chunk
        return chunkGenerator.generateChunk(chunkStartY);
    }

    private int calculateChunkDifficulty(double startY) {
        if (startY == 0.0)
            return 0;

        return (int) (startY / 10.0);
    }
}
