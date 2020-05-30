package doodlejump.client.game;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class ChunkLoaderTest {
    private ChunkLoader chunkLoader;

    @BeforeEach
    void setup() {
        chunkLoader = new ChunkLoader(1000L);
    }

    @Test
    void testInitializeWithoutGenerators() {
        assertThrows(IllegalStateException.class, () -> {
            chunkLoader.initialize(2000.0);
        });
    }
}
