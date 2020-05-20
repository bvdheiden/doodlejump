package doodlejump.core.networking;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Room {
    private static final int MAX_CLIENTS = 2;
    private final List<SocketClient> clientList = new CopyOnWriteArrayList<>();
    private int id;

    public Room(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void addClient(SocketClient client) {
        clientList.add(client);
    }

    public void broadcast(SocketClient ignore, Transaction transaction) {
        for (SocketClient client : clientList) {
            if (ignore == null || ignore != client) {
                client.send(transaction);
            }
        }
    }

    public List<SocketClient> getClientList() {
        return clientList;
    }

    public boolean isFull() {
        return clientList.size() == MAX_CLIENTS;
    }

    public boolean isEmpty() {
        return clientList.size() == 0;
    }

    public void removeClient(SocketClient client) {
        clientList.remove(client);
    }

    @Override
    public String toString() {
        return String.format("Room %d: %d/%d players", id, clientList.size(), MAX_CLIENTS);
    }
}
