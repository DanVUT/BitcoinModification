package sk.tuke.bitcoinmod.transactionscapability.model;

public class TransactionOutput {
    private long recipientBitcoinAddress;
    private float bitcoinAmount;
    private boolean isChange;
    private boolean isSpent;

    public TransactionOutput(long recipientBitcoinAddress, float bitcoinAmount, boolean isChange) {
        this.recipientBitcoinAddress = recipientBitcoinAddress;
        this.bitcoinAmount = bitcoinAmount;
        this.isSpent = false;
        this.isChange = isChange;
    }

    public TransactionOutput(long recipientBitcoinAddress, float bitcoinAmount, boolean isChange, boolean isSpent) {
        this(recipientBitcoinAddress, bitcoinAmount, isChange);
        this.isSpent = isSpent;
    }

    public long getRecipientBitcoinAddress() {
        return recipientBitcoinAddress;
    }

    public float getBitcoinAmount() {
        return bitcoinAmount;
    }

    public boolean isChange() {
        return isChange;
    }

    public boolean isSpent() {
        return isSpent;
    }

    public void setSpent(boolean spent) {
        isSpent = spent;
    }
}
