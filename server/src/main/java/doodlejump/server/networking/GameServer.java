package doodlejump.server.networking;

import doodlejump.core.networking.*;
import doodlejump.server.networking.listeners.RoomCreationListener;
import doodlejump.server.networking.listeners.RoomDestructionListener;
import doodlejump.server.networking.listeners.RoomUpdateListener;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class GameServer {
    private static final int SERVER_PORT = 10_000;
    private final AtomicBoolean connecting = new AtomicBoolean(false);
    private final List<Room> roomList = new CopyOnWriteArrayList<>();
    private final List<SocketClient> socketClientList = new CopyOnWriteArrayList<>();
    private ServerSocket serverSocket;

    private int lastRoomId;
    private RoomCreationListener roomCreationListener;
    private RoomDestructionListener roomDestructionListener;
    private RoomUpdateListener roomUpdateListener;

    /**
     * Start the server socket and start listening for clients.
     */
    public void start() {
        if (isRunning()) {
            return;
        }

        connecting.set(true);

        new Thread(() -> {
            try {
                Log.print("Creating server socket.");
                this.serverSocket = new ServerSocket(SERVER_PORT);
                while (connecting.get()) {
                    try {
                        Log.print("Waiting for clients.");
                        Socket socket = serverSocket.accept();
                        Log.print("Client accepted.");
                        SocketClient client = new SocketClient(socket);
                        socketClientList.add(client);

                        Room room = findRoom();
                        room.addClient(client);

                        if (roomUpdateListener != null) {
                            roomUpdateListener.onRoomUpdate(room);
                        }

                        client.send(new Transaction(TransactionType.ROOM_JOINED, room.isFull()));
                        room.broadcast(client, new Transaction(TransactionType.PLAYER_CONNECTED));

                        client.addTransactionListener((transaction) -> {
                            room.broadcast(client, transaction);
                        });

                        client.addDisconnectionListener(() -> {
                            room.broadcast(client, new Transaction(TransactionType.PLAYER_DISCONNECTED));
                            room.removeClient(client);

                            if (roomUpdateListener != null) {
                                roomUpdateListener.onRoomUpdate(room);
                            }

                            if (room.isEmpty()) {
                                Log.printf("Removing room %d.", room.getId());

                                if (roomDestructionListener != null) {
                                    roomDestructionListener.onRoomDestruction(room);
                                }

                                roomList.remove(room);
                            }

                            socketClientList.remove(client);
                        });
                    } catch (IOException exception) {
                        Log.print("Failed to accept and create client.");
                    }
                }
            } catch (IOException exception) {
                Log.print("Failed to create server socket.");
            }
        }).start();
    }

    /**
     * Stop the server socket and stop listening for clients.
     */
    public void stop() {
        if (!isRunning()) {
            return;
        }

        connecting.set(false);

        for (SocketClient serverClient : socketClientList) {
            serverClient.stop();
        }

        try {
            serverSocket.close();
        } catch (IOException exception) {
            Log.print("Failed to close server socket.");
        }
    }

    public boolean isRunning() {
        return connecting.get() || (serverSocket != null && !serverSocket.isClosed());
    }

    public void setOnRoomCreation(RoomCreationListener listener) {
        this.roomCreationListener = listener;
    }

    public void setOnRoomDestruction(RoomDestructionListener listener) {
        this.roomDestructionListener = listener;
    }

    public void setOnRoomUpdate(RoomUpdateListener listener) {
        this.roomUpdateListener = listener;
    }

    private Room findRoom() {
        for (Room room : roomList) {
            if (!room.isFull()) {
                return room;
            }
        }

        Log.printf("New room created with id: %d.", lastRoomId + 1);

        Room room = new Room(++lastRoomId);

        if (roomCreationListener != null) {
            roomCreationListener.onRoomCreated(room);
        }

        roomList.add(room);
        return room;
    }
}
