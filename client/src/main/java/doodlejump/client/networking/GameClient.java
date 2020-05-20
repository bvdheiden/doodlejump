package doodlejump.client.networking;

import doodlejump.core.networking.payloads.Position;
import doodlejump.core.networking.SocketClient;
import doodlejump.core.networking.Transaction;
import doodlejump.core.networking.TransactionType;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

public class GameClient {
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 10_000;

    private final AtomicBoolean connecting = new AtomicBoolean(false);

    private Socket socket;
    private SocketClient client;

    public void start() {
        if (isRunning()) {
            return;
        }

        connecting.set(true);

        new Thread(() -> {
            while (connecting.get()) {
                try {
                    System.out.println("Connecting to server.");
                    this.socket = new Socket(SERVER_HOST, SERVER_PORT);
                    System.out.println("Connected to server");
                    this.client = new SocketClient(socket);
                    connecting.set(false);
                } catch (IOException exception) {
                    System.out.println("Failed to connect to server.");
                }
            }
        }).start();
    }

    public void stop() {
        if (!isRunning()) {
            return;
        }

        connecting.set(false);

        if (client != null) {
            client.stop();
        }

        try {
            socket.close();
        } catch (IOException exception) {
            System.out.println("Failed to close socket.");
        }
    }

    public boolean isRunning() {
        return connecting.get() || (socket != null && socket.isConnected());
    }

    public void sendPlayerPosition(double newX, double newY) {
        if (!isRunning() || client == null) {
            return;
        }

        client.send(new Transaction(TransactionType.PLAYER_MOVEMENT, new Position(newX, newY)));
    }
}
