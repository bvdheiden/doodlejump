package doodlejump.core.networking;

import javafx.application.Platform;

public abstract class Sdk {
    protected ConnectionListener connectionListener;
    protected DisconnectionListener disconnectionListener;
    protected PlayerConnectionListener playerConnectionListener;
    protected PlayerDisconnectionListener playerDisconnectionListener;
    protected PlayerMovementListener playerMovementListener;
    protected ServerConnection serverConnection;

    private Connector connector;
    private Thread connectorThread;
    private Thread serverConnectionThread;

    public void connect() {
        this.connector = getConnector();
        this.connectorThread = new Thread(connector);

        connector.setOnConnection((serverConnection) -> {
            if (connectionListener != null) {
                Platform.runLater(() -> {
                    connectionListener.onConnection();
                });
            }

            this.serverConnection = serverConnection;
            serverConnectionThread = new Thread(serverConnection);
            serverConnectionThread.start();
        });

        connectorThread.start();
    }

    public void disconnect() {
        if (serverConnectionThread != null) {
            print("disconnecting");
            serverConnection.disconnect();
        }

        if (connectorThread != null) {
            print("will stop connecting");
            connector.stop();
        }
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

    public void setOnPlayerMovement(PlayerMovementListener listener) {
        this.playerMovementListener = listener;
    }

    public static void print(String message) {
        System.out.println("SDK > " + message);
    }

    public static void printf(String message, Object ...params) {
        System.out.println("SDK > " + String.format(message, params));
    }

    abstract protected Connector getConnector();
}
