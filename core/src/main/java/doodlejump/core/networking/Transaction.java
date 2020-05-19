package doodlejump.core.networking;

import java.io.Serializable;

public class Transaction implements Serializable {
    private TransactionType type;
    private Serializable payload;

    public Transaction(TransactionType type) {
        this.type = type;
    }

    public Object getPayload() {
        return payload;
    }

    public void setPayload(Serializable payload) {
        this.payload = payload;
    }

    public TransactionType getType() {
        return type;
    }

    public boolean isType(TransactionType type) {
        return this.type == type;
    }
}
