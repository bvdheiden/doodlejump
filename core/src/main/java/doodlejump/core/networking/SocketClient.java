package doodlejump.core.networking;

import doodlejump.core.networking.listeners.DisconnectionListener;
import doodlejump.core.networking.listeners.TransactionListener;

import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class SocketClient {
    private final Socket socket;
    private final ObjectOutputStream out;
    private final ObjectInputStream in;
    private final List<TransactionListener> transactionListenerList = new CopyOnWriteArrayList<>();
    private final List<DisconnectionListener> disconnectionListenerList = new CopyOnWriteArrayList<>();

    private Player player;
    private Room room;

    public SocketClient(Socket socket) throws IOException {
        this.socket = socket;
        this.out = new ObjectOutputStream(socket.getOutputStream());
        this.in = new ObjectInputStream(socket.getInputStream());

        new Thread(() -> {
            while (socket.isConnected() && !socket.isClosed()) {
                try {
                    Transaction transaction = (Transaction) in.readObject();

                    Log.printf("Received transaction %s with payload: %s from client: %s.", transaction.getType(), transaction.getPayload(), hashCode());

                    for (TransactionListener listener : transactionListenerList) {
                        listener.onTransaction(transaction);
                    }
                } catch (IOException | ClassNotFoundException exception) {
                    Log.printf("Failed to receive data from client: %s.", hashCode());
                    stop();
                }
            }

            Log.printf("Receiving thread died for client: %s.", hashCode());
        }).start();
    }

    public void send(Transaction transaction) {
        if (!socket.isConnected() || socket.isClosed()) {
            return;
        }

        try {
            out.reset();
            out.writeObject(transaction);

            Log.printf("send transaction %s with payload: %s to client: %s.", transaction.getType(), transaction.getPayload(), hashCode());
        } catch (IOException exception) {
            Log.printf("Failed to send data to client: %s.", hashCode());
            stop();
        }
    }

    public void addTransactionListener(TransactionListener listener) {
        transactionListenerList.add(listener);
    }

    public void addDisconnectionListener(DisconnectionListener listener) {
        disconnectionListenerList.add(listener);
    }

    public void stop() {
        if (socket.isClosed()) {
            Log.printf("Socket of client: %s is already closed.", hashCode());
            return;
        }

        Log.printf("Closing socket for client: %s.", hashCode());

        try {
            socket.close();
        } catch (IOException exception) {
            Log.printf("Failed to close socket for client: %s.", hashCode());
        }

        for (DisconnectionListener listener : disconnectionListenerList) {
            listener.onDisconnection();
        }
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }
}
