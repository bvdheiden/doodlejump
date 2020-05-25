package doodlejump.core.networking;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class Room {
    public static final int MAX_CLIENTS = 2;

    private final List<SocketClient> clientList = new CopyOnWriteArrayList<>();
    private final Map<SocketClient, Boolean> readyMap = new ConcurrentHashMap<>();
    private final int id;

    private boolean started;

    public Room(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void addClient(SocketClient client) {
        if (clientList.size() < MAX_CLIENTS) {
            clientList.add(client);
        }
    }

    public void setReady(SocketClient client, boolean ready) {
        if (clientList.contains(client)) {
            readyMap.put(client, ready);
        }
    }

    public boolean isReady() {
        if (!isFull()) {
            return false;
        }

        if (readyMap.size() != clientList.size()) {
            return false;
        }

        for (boolean ready : readyMap.values()) {
            if (!ready) {
                return false;
            }
        }

        return true;
    }

    public void start() {
        this.started = true;
        readyMap.clear();
    }

    public boolean isStarted() {
        return started;
    }

    public void reset() {
        this.started = false;
        readyMap.clear();
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
