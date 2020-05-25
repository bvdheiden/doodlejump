package doodlejump.client.networking;

import doodlejump.core.networking.*;
import doodlejump.core.networking.listeners.*;
import doodlejump.core.networking.payloads.Position;

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
    private ConnectionListener roomConnectionListener;
    private DisconnectionListener roomDisconnectionListener;

    private GameStartListener gameStartListener;
    private PlayerConnectionListener playerConnectionListener;
    private PlayerDisconnectionListener playerDisconnectionListener;
    private PlayerPositionListener playerPositionListener;
    private PlayerLoginListener playerLoginListener;
    private PlayerReadyListener playerReadyListener;


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
                    Log.print("Connecting to server.");
                    this.socket = new Socket(SERVER_HOST, SERVER_PORT);
                    Log.print("Connected to server");
                    this.client = new SocketClient(socket);

                    if (connectionListener != null)
                        connectionListener.onConnection();

                    this.client.addTransactionListener((transaction -> {
                        switch (transaction.getType()) {
                            case PLAYER_LOGIN -> {
                                if (playerLoginListener != null) {
                                    if (transaction.getPayload() instanceof Player) {
                                        playerLoginListener.onPlayerLogin((Player) transaction.getPayload());
                                    } else {
                                        playerLoginListener.onPlayerLoginNameInUse();
                                    }
                                }
                            }

                            case PLAYER_CONNECTED -> {
                                if (playerConnectionListener != null)
                                    playerConnectionListener.onPlayerConnection((Player) transaction.getPayload());
                            }

                            case PLAYER_DISCONNECTED -> {
                                if (playerDisconnectionListener != null)
                                    playerDisconnectionListener.onPlayerDisconnection((Player) transaction.getPayload());
                            }

                            case ROOM_CONNECTED -> {
                                if (roomConnectionListener != null)
                                    roomConnectionListener.onConnection();
                            }

                            case ROOM_DISCONNECTED -> {
                                if (roomDisconnectionListener != null)
                                    roomDisconnectionListener.onDisconnection();
                            }

                            case PLAYER_READY -> {
                                if (playerReadyListener != null)
                                    playerReadyListener.onPlayerReady((Player) transaction.getPayload());
                            }

                            case GAME_STARTED -> {
                                if (gameStartListener != null)
                                    gameStartListener.onGameStart();
                            }

                            case PLAYER_POSITION -> {
                                if (playerPositionListener != null)
                                    playerPositionListener.onNewPlayerPosition();
                            }
                        }
                    }));

                    this.client.addDisconnectionListener(() -> {
                        if (disconnectionListener != null)
                            disconnectionListener.onDisconnection();
                    });

                    connecting.set(false);
                } catch (IOException exception) {
                    Log.print("Failed to connect to server.");
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
            Log.print("Failed to close socket.");
        }
    }

    /**
     * Get whether the connection is running.
     */
    public boolean isRunning() {
        return connecting.get() || (socket != null && socket.isConnected());
    }

    public void login(String name) {
        if (!canSendTransaction()) {
            return;
        }

        client.send(new Transaction(TransactionType.PLAYER_LOGIN, new Player(name)));
    }

    public void connectRoom() {
        if (!canSendTransaction()) {
            return;
        }

        client.send(new Transaction(TransactionType.ROOM_CONNECT));
    }

    public void disconnectRoom() {
        if (!canSendTransaction()) {
            return;
        }

        client.send(new Transaction(TransactionType.ROOM_DISCONNECT));
    }

    public void ready() {
        if (!canSendTransaction()) {
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

    public void setOnPlayerLogin(PlayerLoginListener listener) {
        this.playerLoginListener = listener;
    }

    public void setOnRoomConnection(ConnectionListener listener) {
        this.roomConnectionListener = listener;
    }

    public void setOnRoomDisconnection(DisconnectionListener listener) {
        this.roomDisconnectionListener = listener;
    }

    public void setOnPlayerConnection(PlayerConnectionListener listener) {
        this.playerConnectionListener = listener;
    }

    public void setOnPlayerDisconnection(PlayerDisconnectionListener listener) {
        this.playerDisconnectionListener = listener;
    }

    public void setOnPlayerReady(PlayerReadyListener listener) {
        this.playerReadyListener = listener;
    }

    public void setOnNewPlayerPosition(PlayerPositionListener listener) {
        this.playerPositionListener = listener;
    }

    public void setOnGameStart(GameStartListener listener) {
        this.gameStartListener = listener;
    }

    private boolean canSendTransaction() {
        return isRunning() && client != null;
    }
}
