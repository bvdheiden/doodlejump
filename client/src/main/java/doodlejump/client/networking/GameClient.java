package doodlejump.client.networking;

import doodlejump.core.networking.listeners.*;
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

    private ConnectionListener connectionListener;
    private DisconnectionListener disconnectionListener;
    private GameStartListener gameStartListener;
    private RoomJoinedListener roomJoinedListener;
    private PlayerConnectionListener playerConnectionListener;
    private PlayerDisconnectionListener playerDisconnectionListener;
    private PlayerPositionListener playerPositionListener;

    /**
     * Start the socket to the server.
     */
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

                    if (connectionListener != null)
                        connectionListener.onConnection();

                    this.client.addTransactionListener((transaction -> {
                        switch (transaction.getType()) {
                            case PLAYER_CONNECTED -> {
                                if (playerConnectionListener != null)
                                    playerConnectionListener.onPlayerConnection();
                            }

                            case PLAYER_DISCONNECTED -> {
                                if (playerDisconnectionListener != null)
                                    playerDisconnectionListener.onPlayerDisconnection();
                            }

                            case PLAYER_POSITION -> {
                                if (playerPositionListener != null)
                                    playerPositionListener.onNewPlayerPosition();
                            }

                            case ROOM_JOINED -> {
                                if (roomJoinedListener != null)
                                    roomJoinedListener.onRoomJoined();
                            }

                            case GAME_STARTED -> {
                                if (gameStartListener != null)
                                    gameStartListener.onGameStart();
                            }
                        }
                    }));

                    this.client.addDisconnectionListener(() -> {
                        if (disconnectionListener != null)
                            disconnectionListener.onDisconnection();
                    });

                    connecting.set(false);
                } catch (IOException exception) {
                    System.out.println("Failed to connect to server.");
                }
            }
        }).start();
    }

    /**
     * Stop the socket to the server.
     */
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

    /**
     * Get whether the connection is running.
     */
    public boolean isRunning() {
        return connecting.get() || (socket != null && socket.isConnected());
    }

    /**
     * Send a ready signal to begin the game.
     */
    public void sendReadySignal() {
        if (!isRunning() || client == null) {
            return;
        }

        client.send(new Transaction(TransactionType.PLAYER_READY));
    }

    /**
     * Send player position to server.
     * @param newX new x position
     * @param newY new y position
     */
    public void sendPlayerPosition(double newX, double newY) {
        if (!isRunning() || client == null) {
            return;
        }

        client.send(new Transaction(TransactionType.PLAYER_POSITION, new Position(newX, newY)));
    }

    public void setOnConnection(ConnectionListener listener) {
        this.connectionListener = listener;
    }

    public void setOnDisconnection(DisconnectionListener listener) {
        this.disconnectionListener = listener;
    }

    public void setOnPlayerConnection(PlayerConnectionListener listener) {
        this.playerConnectionListener = listener;
    }

    public void setOnPlayerDisconnection(PlayerDisconnectionListener listener) {
        this.playerDisconnectionListener = listener;
    }

    public void setOnNewPlayerPosition(PlayerPositionListener listener) {
        this.playerPositionListener = listener;
    }

    public void setOnRoomJoined(RoomJoinedListener listener) {
        this.roomJoinedListener = listener;
    }

    public void setOnGameStart(GameStartListener listener) {
        this.gameStartListener = listener;
    }
}
