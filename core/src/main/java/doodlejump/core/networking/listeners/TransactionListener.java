package doodlejump.core.networking.listeners;

import doodlejump.core.networking.Transaction;

@FunctionalInterface
public interface TransactionListener {
    void onTransaction(Transaction transaction);
}
