package sk.tuke.bitcoinmod.transactionscapability.model;

import java.util.ArrayList;
import java.util.List;

public class Transaction {
    private int transactionID;
    private long senderBitcoinAddress;
    private long recipientBitcoinAddress;
    private boolean isBaseTransaction;
    private List<TransactionOutput> transactionOutputs;

    public Transaction(int transactionID, long senderBitcoinAddress, long recipientBitcoinAddress, boolean isBaseTransaction) {
        this.transactionID = transactionID;
        transactionOutputs = new ArrayList<>();
        this.senderBitcoinAddress = senderBitcoinAddress;
        this.recipientBitcoinAddress = recipientBitcoinAddress;
        this.isBaseTransaction = isBaseTransaction;
    }

    public Transaction(int transactionID, long senderBitcoinAddress, long recipientBitcoinAddress, List<TransactionOutput> transactionOutputs, boolean isBaseTransaction) {
        this(transactionID, senderBitcoinAddress, recipientBitcoinAddress, isBaseTransaction);
        this.transactionOutputs = transactionOutputs;
    }

    public int getTransactionID() {
        return transactionID;
    }

    public long getSenderBitcoinAddress() {
        return senderBitcoinAddress;
    }

    public long getRecipientBitcoinAddress() {
        return recipientBitcoinAddress;
    }

    public boolean isBaseTransaction() {
        return isBaseTransaction;
    }

    public List<TransactionOutput> getTransactionOutputs() {
        return transactionOutputs;
    }

    public void setTransactionOutput(TransactionOutput transactionOutput){
        this.transactionOutputs.add(transactionOutput);
    }

    public void setTransactionOutputSpent(int index, boolean isSpent){
        transactionOutputs.get(index).setSpent(isSpent);
    }
}
