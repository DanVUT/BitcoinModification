package sk.tuke.bitcoinmod.communication.miningmessage;

import net.minecraft.network.PacketBuffer;
import sk.tuke.bitcoinmod.transactionscapability.model.Transaction;
import sk.tuke.bitcoinmod.transactionscapability.model.TransactionOutput;

public class MiningMessageToClient {
    private Transaction baseTransaction;
    private boolean isValid;

    public MiningMessageToClient(Transaction baseTransaction) {
        this.baseTransaction = baseTransaction;
        this.isValid = true;
    }

    public Transaction getBaseTransaction() {
        return baseTransaction;
    }

    public boolean isValid() {
        return isValid;
    }

    public void encode(PacketBuffer buffer){
        buffer.writeInt(baseTransaction.getTransactionID());
        buffer.writeLong(baseTransaction.getSenderBitcoinAddress());
        buffer.writeLong(baseTransaction.getRecipientBitcoinAddress());
        buffer.writeBoolean(baseTransaction.isBaseTransaction());
        buffer.writeInt(baseTransaction.getTransactionOutputs().size());
        for(TransactionOutput to : baseTransaction.getTransactionOutputs()){
            buffer.writeLong(to.getRecipientBitcoinAddress());
            buffer.writeFloat(to.getBitcoinAmount());
            buffer.writeBoolean(to.isChange());
            buffer.writeBoolean(to.isSpent());
        }
    }

    public static MiningMessageToClient decode(PacketBuffer buffer){
        int transactionID = buffer.readInt();
        long senderBitcoinAddress = buffer.readLong();
        long recipientBitcoinAddress = buffer.readLong();
        boolean isBaseTransaction = buffer.readBoolean();
        Transaction baseTransaction = new Transaction(transactionID, senderBitcoinAddress, recipientBitcoinAddress, isBaseTransaction);
        int transactionOutputsCount = buffer.readInt();

        for(int i = 0; i < transactionOutputsCount; i++){
            long recipientBitcoinAddressOutput = buffer.readLong();
            float bitcoinAmount = buffer.readFloat();
            boolean isChange = buffer.readBoolean();
            boolean isSpent = buffer.readBoolean();

            baseTransaction.setTransactionOutput(new TransactionOutput(recipientBitcoinAddressOutput, bitcoinAmount, isChange, isSpent));
        }

        return new MiningMessageToClient(baseTransaction);
    }

}
