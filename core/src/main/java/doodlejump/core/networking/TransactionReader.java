package doodlejump.core.networking;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class TransactionReader implements Runnable {
    protected AtomicBoolean running = new AtomicBoolean(true);
    protected ObjectInputStream in;

    public TransactionReader(ObjectInputStream in) {
        this.in = in;
    }

    @Override
    public void run() {
        while (running.get()) {
            try {
                Transaction transaction = (Transaction) in.readObject();

                Sdk.printf("received transaction: %s with payload: %s", transaction.getType(), transaction.getPayload());

                handleTransaction(transaction);
            } catch (Exception exception) {
                // do nothing
            }
        }

        Thread.yield();
    }

    public void kill() {
        try {
            in.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        running.set(false);
    }

    abstract protected void handleTransaction(Transaction transaction) throws Exception;
}
