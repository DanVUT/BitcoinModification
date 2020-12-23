package sk.tuke.bitcoinmod.communication.alltransactions;

import net.minecraft.network.PacketBuffer;
import sk.tuke.bitcoinmod.transactionscapability.model.Transaction;
import sk.tuke.bitcoinmod.transactionscapability.model.TransactionOutput;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AllTransactionsMessageToClient {
    private List<Transaction> allTransactions;
    private boolean isValid;

    public AllTransactionsMessageToClient(Map<Integer, Transaction> transactionsMap){
        this.allTransactions = new ArrayList<>();
        this.allTransactions.addAll(transactionsMap.values());
        this.isValid = true;
    }

    public AllTransactionsMessageToClient(List<Transaction> transactionList){
        this.allTransactions = transactionList;
        this.isValid = true;
    }

    public List<Transaction> getAllTransactions() {
        return allTransactions;
    }

    public boolean isValid() {
        return isValid;
    }

    public void encode(PacketBuffer buffer){
        buffer.writeInt(this.allTransactions.size());
        for(Transaction transaction : allTransactions){
            buffer.writeInt(transaction.getTransactionID());
            buffer.writeLong(transaction.getSenderBitcoinAddress());
            buffer.writeLong(transaction.getRecipientBitcoinAddress());
            buffer.writeBoolean(transaction.isBaseTransaction());
            buffer.writeInt(transaction.getTransactionOutputs().size());
            for(TransactionOutput to : transaction.getTransactionOutputs()){
                buffer.writeLong(to.getRecipientBitcoinAddress());
                buffer.writeFloat(to.getBitcoinAmount());
                buffer.writeBoolean(to.isChange());
                buffer.writeBoolean(to.isSpent());
            }
        }
    }

    public static AllTransactionsMessageToClient decode(PacketBuffer buffer){
        ArrayList<Transaction> allTransactions = new ArrayList<>();
        int transactionsCount = buffer.readInt();
        for(int i = 0; i < transactionsCount; i++){
            int transactionID = buffer.readInt();
            long senderBitcoinAddress = buffer.readLong();
            long recipientBitcoinAddress = buffer.readLong();
            boolean isBaseTransaction = buffer.readBoolean();
            Transaction newTransaction = new Transaction(transactionID, senderBitcoinAddress, recipientBitcoinAddress, isBaseTransaction);
            int transactionOutputsCount = buffer.readInt();

            for(int j = 0; j < transactionOutputsCount; j++){
                long recipientBitcoinAddressOutput = buffer.readLong();
                float bitcoinAmount = buffer.readFloat();
                boolean isChange = buffer.readBoolean();
                boolean isSpent = buffer.readBoolean();

                newTransaction.setTransactionOutput(new TransactionOutput(recipientBitcoinAddressOutput, bitcoinAmount, isChange, isSpent));
            }
            allTransactions.add(newTransaction);
        }
        return new AllTransactionsMessageToClient(allTransactions);
    }
}
