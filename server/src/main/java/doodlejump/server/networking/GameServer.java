package doodlejump.server.networking;

import doodlejump.core.networking.Room;
import doodlejump.core.networking.SocketClient;
import doodlejump.core.networking.Transaction;
import doodlejump.core.networking.TransactionType;

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

    public void start() {
        if (isRunning()) {
            return;
        }

        connecting.set(true);

        new Thread(() -> {
            try {
                System.out.println("Creating server socket.");
                this.serverSocket = new ServerSocket(SERVER_PORT);
                while (connecting.get()) {
                    try {
                        System.out.println("Waiting for clients.");
                        Socket socket = serverSocket.accept();
                        System.out.println("Client accepted.");
                        SocketClient client = new SocketClient(socket);
                        socketClientList.add(client);

                        Room room = findRoom();
                        room.addClient(client);
                        client.send(new Transaction(TransactionType.ROOM_JOINED, room.isFull()));
                        room.broadcast(client, new Transaction(TransactionType.PLAYER_CONNECTED));

                        client.addTransactionListener((transaction) -> {
                            room.broadcast(client, transaction);
                        });

                        client.addDisconnectionListener(() -> {
                            room.broadcast(client, new Transaction(TransactionType.PLAYER_DISCONNECTED));
                            room.removeClient(client);

                            if (room.isEmpty()) {
                                System.out.printf("Removing room %d%n", room.getId());

                                roomList.remove(room);
                            }

                            socketClientList.remove(client);
                        });
                    } catch (IOException exception) {
                        System.out.println("Failed to accept and create client.");
                    }
                }
            } catch (IOException exception) {
                System.out.println("Failed to create server socket.");
            }
        }).start();
    }

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
            System.out.println("Failed to close server socket.");
        }
    }

    public boolean isRunning() {
        return connecting.get() || (serverSocket != null && !serverSocket.isClosed());
    }

    private Room findRoom() {
        for (Room room : roomList) {
            if (!room.isFull()) {
                return room;
            }
        }

        System.out.println("New room created with id: " + (lastRoomId + 1));

        Room room = new Room(++lastRoomId);
        roomList.add(room);
        return room;
    }
}
