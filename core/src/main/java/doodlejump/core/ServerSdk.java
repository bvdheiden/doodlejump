package doodlejump.core;

import java.io.IOException;
import java.net.Socket;

public class ServerSdk {
    private Socket serverSocket;

    private ServerDisconnectListener serverDisconnectListener;

    public ServerSdk() {

    }

    public void connectToServer(String serverAddress, int serverPort) {
        try {
            this.serverSocket = new Socket(serverAddress, serverPort);
        } catch (IOException e) {
            if (this.serverDisconnectListener != null)
                this.serverDisconnectListener.onServerDisconnect();
        }
    }

    public void setOnServerDisconnect(ServerDisconnectListener listener) {
        serverDisconnectListener = listener;
    }
}
