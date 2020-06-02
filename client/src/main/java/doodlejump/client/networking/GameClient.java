package doodlejump.client.networking;

import doodlejump.core.networking.*;
import doodlejump.core.networking.listeners.*;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

public enum GameClient {
    INSTANCE;
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 10_000;

    private final AtomicBoolean connecting = new AtomicBoolean(false);

    private Socket socket;
    private SocketClient client;

    private Player player;

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
                                        Player player = (Player) transaction.getPayload();
                                        this.player = player;
                                        playerLoginListener.onPlayerLogin(player);
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
                                    gameStartListener.onGameStart((long) transaction.getPayload());
                            }

                            case PLAYER_POSITION -> {
                                if (playerPositionListener != null)
                                    playerPositionListener.onNewPlayerPosition((Player) transaction.getPayload());
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

        this.player = null;

        if (client != null) {
            client.stop();
        }

        if (socket == null) {
            return;
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

    /**
     * Log in with username.
     * @param name username
     */
    public void login(String name) {
        if (!canSendTransaction()) {
            return;
        }

        client.send(new Transaction(TransactionType.PLAYER_LOGIN, new Player(name)));
    }

    /**
     * Connect to a room.
     */
    public void connectRoom() {
        if (!canSendTransaction()) {
            return;
        }

        client.send(new Transaction(TransactionType.ROOM_CONNECT));
    }

    /**
     * Disconnect from the room.
     */
    public void disconnectRoom() {
        if (!canSendTransaction()) {
            return;
        }

        client.send(new Transaction(TransactionType.ROOM_DISCONNECT));
    }

    /**
     * Give ready signal.
     * When all players are ready, the game will be started.
     */
    public void ready() {
        if (!canSendTransaction()) {
            return;
        }

        client.send(new Transaction(TransactionType.PLAYER_READY));
    }

    /**
     * Send player position to server.
     */
    public void sendPosition() {
        if (!isRunning() || client == null || player == null) {
            return;
        }

        System.out.printf("x: %f y: %f%n", player.getX(), player.getY());

        client.send(new Transaction(TransactionType.PLAYER_POSITION, player));
    }

    /**
     * On server connection callback.
     * @param listener callback
     */
    public void setOnConnection(ConnectionListener listener) {
        this.connectionListener = listener;
    }

    /**
     * On server disconnection callback.
     * @param listener callback
     */
    public void setOnDisconnection(DisconnectionListener listener) {
        this.disconnectionListener = listener;
    }

    /**
     * On player login callback.
     * @param listener callback
     */
    public void setOnPlayerLogin(PlayerLoginListener listener) {
        this.playerLoginListener = listener;
    }

    /**
     * On room connection callback.
     * @param listener callback
     */
    public void setOnRoomConnection(ConnectionListener listener) {
        this.roomConnectionListener = listener;
    }

    /**
     * On room disconnection callback.
     * @param listener callback
     */
    public void setOnRoomDisconnection(DisconnectionListener listener) {
        this.roomDisconnectionListener = listener;
    }

    /**
     * On player in room connection callback.
     * @param listener callback
     */
    public void setOnPlayerConnection(PlayerConnectionListener listener) {
        this.playerConnectionListener = listener;
    }

    /**
     * On player in room disconnection callback.
     * @param listener callback
     */
    public void setOnPlayerDisconnection(PlayerDisconnectionListener listener) {
        this.playerDisconnectionListener = listener;
    }

    /**
     * On player in room ready callback.
     * @param listener callback
     */
    public void setOnPlayerReady(PlayerReadyListener listener) {
        this.playerReadyListener = listener;
    }

    /**
     * On player in room new position callback.
     * @param listener callback
     */
    public void setOnNewPlayerPosition(PlayerPositionListener listener) {
        this.playerPositionListener = listener;
    }

    /**
     * On game in room start callback.
     * @param listener callback
     */
    public void setOnGameStart(GameStartListener listener) {
        this.gameStartListener = listener;
    }

    private boolean canSendTransaction() {
        return isRunning() && client != null;
    }
}
