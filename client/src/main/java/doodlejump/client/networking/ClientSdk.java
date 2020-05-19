package doodlejump.client.networking;

import doodlejump.core.Player;
import doodlejump.core.Position;
import doodlejump.core.networking.*;
import javafx.application.Platform;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientSdk extends Sdk implements PlayerMovementListener {
    private String serverHost;
    private int serverPort;

    public ClientSdk(String serverHost, int serverPort) {
        this.serverHost = serverHost;
        this.serverPort = serverPort;
    }

    @Override
    public void onPlayerMovement(double newX, double newY) {
        Transaction transaction = new Transaction(TransactionType.PLAYER_MOVEMENT);
        transaction.setPayload(new Position(newX, newY));
        serverConnection.queueTransaction(transaction);
    }

    @Override
    protected doodlejump.core.networking.Connector getConnector() {
        return new Connector() {
            @Override
            protected ServerConnection connect() throws Exception {
                printf("connecting to server on %s:%d", serverHost, serverPort);

                Socket socket = new Socket(serverHost, serverPort);
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

                stop();

                return new ServerConnection(out, new ClientTransactionReader(in));
            }
        };
    }

    private class ClientTransactionReader extends TransactionReader {
        public ClientTransactionReader(ObjectInputStream in) {
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
}
