package doodlejump.core.networking;

@FunctionalInterface
public interface TransactionListener {
    void onTransaction(Transaction transaction);
}
