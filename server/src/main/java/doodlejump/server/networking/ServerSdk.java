package doodlejump.server.networking;

import doodlejump.core.Player;
import doodlejump.core.Position;
import doodlejump.core.networking.*;
import javafx.application.Platform;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerSdk extends Sdk {
    private int serverPort;

    private ServerSocket serverSocket;

    private ConnectionListener connectionListener;
    private DisconnectionListener disconnectionListener;
    private PlayerConnectionListener playerConnectionListener;
    private PlayerDisconnectionListener playerDisconnectionListener;
    private PlayerMovementListener playerMovementListener;

    public ServerSdk(int serverPort) {
        this.serverPort = serverPort;
    }

    @Override
    protected doodlejump.core.networking.Connector getConnector() {
        return new Connector() {
            @Override
            protected ServerConnection connect() throws Exception {
                printf("listening for clients on port: %d", serverPort);

                if (serverSocket == null) {
                    serverSocket = new ServerSocket(serverPort);
                }

                Socket socket = serverSocket.accept();

                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

                print("client connected");

                return new ServerConnection(out, new ServerTransactionReader(in));
            }
        };
    }

    @Override
    public void disconnect() {
        super.disconnect();

        if (serverSocket != null) {
            try {
                serverSocket.close();
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
    }

    private class ServerTransactionReader extends TransactionReader {
        public ServerTransactionReader(ObjectInputStream in) {
            super(in);
        }

        @Override
        protected void handleTransaction(Transaction transaction) throws Exception {
            switch (transaction.getType()) {
                case PLAYER_CONNECTED -> {
                    if (playerConnectionListener == null) {
                        return;
                    }

                    Player player = (Player) transaction.getPayload();
                    Platform.runLater(() -> {
                        playerConnectionListener.onPlayerConnection(player);
                    });
                }

                case PLAYER_DISCONNECTED -> {
                    if (playerDisconnectionListener == null) {
                        return;
                    }

                    Platform.runLater(() -> {
                        playerDisconnectionListener.onPlayerDisconnection();
                    });
                }

                case PLAYER_MOVEMENT -> {
                    if (playerMovementListener == null) {
                        return;
                    }

                    Position position = (Position) transaction.getPayload();
                    Platform.runLater(() -> {
                        playerMovementListener.onPlayerMovement(position.getX(), position.getY());
                    });
                }
            }
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
}
