package doodlejump.core.networking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class RoomTest {
    private int roomId;
    private Room room;
    private SocketClient socketClientMock1;
    private SocketClient socketClientMock2;
    private SocketClient socketClientMock3;
    private Transaction transactionMock;

    @BeforeEach
    void setup() {
        this.roomId = 1 + (int) (Math.random() * 1000);
        this.room = new Room(roomId);
        this.socketClientMock1 = mock(SocketClient.class);
        this.socketClientMock2 = mock(SocketClient.class);
        this.socketClientMock3 = mock(SocketClient.class);
        this.transactionMock = mock(Transaction.class);
    }

    @Test
    void roomId() {
        assertEquals(roomId, room.getId());
    }

    @Test
    void addClient() {
        assertTrue(room.isEmpty());
        room.addClient(socketClientMock1);
        assertEquals(1, room.getClientList().size());
        room.addClient(socketClientMock2);
        assertEquals(2, room.getClientList().size());
        assertTrue(room.isFull());

        room.addClient(socketClientMock3);
        assertEquals(Room.MAX_CLIENTS, room.getClientList().size());
    }

    @Test
    void removeClient() {
        assertTrue(room.isEmpty());
        room.addClient(socketClientMock1);
        room.addClient(socketClientMock2);
        assertTrue(room.isFull());
        room.removeClient(socketClientMock1);
        room.removeClient(socketClientMock2);
        assertTrue(room.isEmpty());

        assertDoesNotThrow(() -> {
            room.removeClient(socketClientMock3);
        });
    }

    @Test
    void broadcastToAll() {
        room.addClient(socketClientMock1);
        room.addClient(socketClientMock2);

        room.broadcast(null, transactionMock);

        verify(socketClientMock1, times(1)).send(transactionMock);
        verify(socketClientMock2, times(1)).send(transactionMock);
    }

    @Test
    void broadcastToOther() {
        room.addClient(socketClientMock1);
        room.addClient(socketClientMock2);

        room.broadcast(socketClientMock1, transactionMock);

        verify(socketClientMock1, times(0)).send(transactionMock);
        verify(socketClientMock2, times(1)).send(transactionMock);
    }

    @Test
    void broadcastToAllInEmptyRoom() {
        assertDoesNotThrow(() -> room.broadcast(null, transactionMock));
    }

    @Test
    void broadcastToOtherInEmptyRoom() {
        assertDoesNotThrow(() -> room.broadcast(socketClientMock1, transactionMock));
    }

    @Test
    void testToString() {
        room.addClient(socketClientMock1);

        assertEquals(String.format("Room %d: 1/%d players", roomId, Room.MAX_CLIENTS), room.toString());
    }
}
