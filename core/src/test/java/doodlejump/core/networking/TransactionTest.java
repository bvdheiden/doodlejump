package doodlejump.core.networking;

import org.junit.jupiter.api.Test;

import java.io.Serializable;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public class TransactionTest {
    @Test
    void testCreation() {
        Serializable serializableMock = mock(Serializable.class);

        assertDoesNotThrow(() -> {
            Transaction transaction = new Transaction(TransactionType.PLAYER_READY);

            assertEquals(TransactionType.PLAYER_READY, transaction.getType());
            assertNull(transaction.getPayload());
            assertTrue(transaction.isType(TransactionType.PLAYER_READY));
        });

        assertDoesNotThrow(() -> {
            Transaction transaction = new Transaction(TransactionType.PLAYER_READY, serializableMock);

            assertEquals(TransactionType.PLAYER_READY, transaction.getType());
            assertEquals(serializableMock, transaction.getPayload());
            assertTrue(transaction.isType(TransactionType.PLAYER_READY));
        });
    }
}
