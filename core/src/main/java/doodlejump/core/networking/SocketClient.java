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

    public SocketClient(Socket socket) throws IOException {
        this.socket = socket;
        this.out = new ObjectOutputStream(socket.getOutputStream());
        this.in = new ObjectInputStream(socket.getInputStream());

        new Thread(() -> {
            while (socket.isConnected() && !socket.isClosed()) {
                try {
                    Transaction transaction = (Transaction) in.readObject();

                    System.out.printf("Received transaction %s with payload: %s%n", transaction.getType(), transaction.getPayload());

                    for (TransactionListener listener : transactionListenerList) {
                        listener.onTransaction(transaction);
                    }
                } catch (IOException | ClassNotFoundException exception) {
                    System.out.println("Failed to receive data.");
                    stop();
                }
            }

            System.out.println("Receiving thread died.");
        }).start();
    }

    public void send(Transaction transaction) {
        if (!socket.isConnected() || socket.isClosed()) {
            return;
        }

        try {
            out.writeObject(transaction);

            System.out.printf("Send transaction %s with payload: %s%n", transaction.getType(), transaction.getPayload());
        } catch (IOException exception) {
            System.out.println("Failed to send data.");
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
            System.out.println("Socket is already closed.");
            return;
        }

        System.out.println("Closing socket.");

        try {
            socket.close();
        } catch (IOException exception) {
            System.out.println("Failed to close socket.");
        }

        for (DisconnectionListener listener : disconnectionListenerList) {
            listener.onDisconnection();
        }
    }
}
