package sk.tuke.bitcoinmod.communication.newtransactionrequest;

import net.minecraft.network.PacketBuffer;

public class NewTransactionRequestToServer {
    private long senderPrivateKey;
    private long senderBitcoinAddress;
    private long recipientBitcoinAddress;
    private float bitcoinAmount;
    private boolean isValid;

    public NewTransactionRequestToServer(long senderPrivateKey, long senderBitcoinAddress, long recipientBitcoinAddress, float bitcoinAmount) {
        this.senderPrivateKey = senderPrivateKey;
        this.senderBitcoinAddress = senderBitcoinAddress;
        this.recipientBitcoinAddress = recipientBitcoinAddress;
        this.bitcoinAmount = bitcoinAmount;
        isValid = true;
    }

    public long getSenderPrivateKey() {
        return senderPrivateKey;
    }

    public long getSenderBitcoinAddress() {
        return senderBitcoinAddress;
    }

    public long getRecipientBitcoinAddress() {
        return recipientBitcoinAddress;
    }

    public float getBitcoinAmount() {
        return bitcoinAmount;
    }

    public boolean isValid() {
        return isValid;
    }

    public void encode(PacketBuffer buffer){
        buffer.writeLong(senderPrivateKey);
        buffer.writeLong(senderBitcoinAddress);
        buffer.writeLong(recipientBitcoinAddress);
        buffer.writeFloat(bitcoinAmount);
    }

    public static NewTransactionRequestToServer decode(PacketBuffer buffer){
        long senderPrivateKey = buffer.readLong();
        long senderBitcoinAddress = buffer.readLong();
        long recipientBitcoinAddress = buffer.readLong();
        float bitcoinAmount = buffer.readFloat();

        return new NewTransactionRequestToServer(senderPrivateKey, senderBitcoinAddress, recipientBitcoinAddress, bitcoinAmount);
    }
}
