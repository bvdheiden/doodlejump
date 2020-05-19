package doodlejump.core.networking;

import java.util.concurrent.atomic.AtomicBoolean;

public abstract class Connector implements Runnable {
    private SdkConnectionListener sdkConnectionListener;
    protected final AtomicBoolean running = new AtomicBoolean(true);

    @Override
    public void run() {
        while (running.get()) {
            try {
                ServerConnection serverConnection = connect();
                sdkConnectionListener.onConnection(serverConnection);
            } catch (Exception exception) {
                // nothing
            }
        }

        Thread.yield();
    }

    public void stop() {
        running.set(false);
    }

    void setOnConnection(SdkConnectionListener listener) {
        this.sdkConnectionListener = listener;
    }

    protected abstract ServerConnection connect() throws Exception;
}
