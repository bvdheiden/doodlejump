package doodlejump.core.networking;

import java.io.ObjectOutputStream;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicBoolean;

public class ServerConnection implements Runnable {
    private final AtomicBoolean running = new AtomicBoolean(true);
    private LinkedList<Transaction> transactionQueue = new LinkedList<>();
    private final ObjectOutputStream out;
    private final TransactionReader transactionReader;

    public ServerConnection(ObjectOutputStream out, TransactionReader transactionReader) {
        this.out = out;
        this.transactionReader = transactionReader;
    }

    @Override
    public void run() {
        Thread transactionReaderThread = new Thread(transactionReader);
        transactionReaderThread.start();

        while (running.get()) {
            try {
                while (transactionQueue.size() > 0) {
                    Transaction transaction = transactionQueue.poll();

                    Sdk.printf("sending transaction: %s with payload: %s", transaction.getType(), transaction.getPayload());

                    out.writeObject(transaction);
                }

                Thread.sleep(100);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }

        Sdk.print("stopping server connection...");
        transactionReader.kill();
        Thread.yield();
    }

    public synchronized void disconnect() {
        running.set(false);
    }

    public synchronized void queueTransaction(Transaction transaction) {
        transactionQueue.add(transaction);
    }
}