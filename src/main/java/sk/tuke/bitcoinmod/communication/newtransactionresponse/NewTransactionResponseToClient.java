package sk.tuke.bitcoinmod.communication.newtransactionresponse;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Tuple;
import sk.tuke.bitcoinmod.transactionscapability.model.Transaction;
import sk.tuke.bitcoinmod.transactionscapability.model.TransactionOutput;

import java.util.ArrayList;
import java.util.List;

/**
 * Trieda reprezentujuca odpoved servera na ziadost vytvorenia novej transakcie
 */
public class NewTransactionResponseToClient {
    private boolean newTransactionCreated;
    private Transaction newTransaction;
    private List<Tuple<Integer, Integer>> usedTransactions;
    private boolean isValid;

    /**
     * @param newTransactionCreated informacia ci server vytvoril transakciu
     * @param newTransaction nova transakcia
     * @param usedTransactions pouzite transakcie pri procese vytvarania
     */
    public NewTransactionResponseToClient(boolean newTransactionCreated, Transaction newTransaction, List<Tuple<Integer, Integer>> usedTransactions) {
        this.newTransactionCreated = newTransactionCreated;
        this.newTransaction = newTransaction;
        this.usedTransactions = usedTransactions;
        this.isValid = true;
    }

    public boolean isNewTransactionCreated() {
        return newTransactionCreated;
    }

    public Transaction getNewTransaction() {
        return newTransaction;
    }

    public List<Tuple<Integer, Integer>> getUsedTransactions() {
        return usedTransactions;
    }

    public boolean isValid() {
        return isValid;
    }

    /**
     * Metoda zakoduje obsah spravy do Packet Buffera. Vola sa na strane servera
     * @param buffer Packet Buffer do ktoreho sa zakoduje obsah spravy
     */
    public void encode(PacketBuffer buffer){
        buffer.writeBoolean(newTransactionCreated);

        if(!newTransactionCreated){
            return;
        }
        buffer.writeInt(newTransaction.getTransactionID());
        buffer.writeLong(newTransaction.getSenderBitcoinAddress());
        buffer.writeLong(newTransaction.getRecipientBitcoinAddress());
        buffer.writeBoolean(newTransaction.isBaseTransaction());
        buffer.writeInt(newTransaction.getTransactionOutputs().size());
        for(TransactionOutput to : newTransaction.getTransactionOutputs()){
            buffer.writeLong(to.getRecipientBitcoinAddress());
            buffer.writeFloat(to.getBitcoinAmount());
            buffer.writeBoolean(to.isChange());
            buffer.writeBoolean(to.isSpent());
        }

        buffer.writeInt(usedTransactions.size());
        for(Tuple<Integer, Integer> pair : usedTransactions){
            buffer.writeInt(pair.getA());
            buffer.writeInt(pair.getB());
        }
    }

    /**
     * Metoda precita obsah spravy z buffera. Vola sa na strane klienta.
     * @param buffer Packet Buffer ktory obsahuje obsah spravy
     * @return instancia NewTransactionResponseToClient
     */
    public static NewTransactionResponseToClient decode(PacketBuffer buffer){
        boolean newTransactionCreated = buffer.readBoolean();

        if(!newTransactionCreated){
            return new NewTransactionResponseToClient(false, null, null);
        }
        int transactionID = buffer.readInt();
        long senderBitcoinAddress = buffer.readLong();
        long recipientBitcoinAddress = buffer.readLong();
        boolean isBaseTransaction = buffer.readBoolean();
        Transaction newTransaction = new Transaction(transactionID, senderBitcoinAddress, recipientBitcoinAddress, isBaseTransaction);
        int transactionOutputsCount = buffer.readInt();

        for(int i = 0; i < transactionOutputsCount; i++){
            long recipientBitcoinAddressOutput = buffer.readLong();
            float bitcoinAmount = buffer.readFloat();
            boolean isChange = buffer.readBoolean();
            boolean isSpent = buffer.readBoolean();

            newTransaction.setTransactionOutput(new TransactionOutput(recipientBitcoinAddressOutput, bitcoinAmount, isChange, isSpent));
        }

        List<Tuple<Integer, Integer>> usedTransactions = new ArrayList<>();
        int usedTransactionsCount = buffer.readInt();

        for(int i = 0; i < usedTransactionsCount; i++){
            int usedTransactionID = buffer.readInt();
            int usedTransactionOutputIndex = buffer.readInt();
            usedTransactions.add(new Tuple<>(usedTransactionID, usedTransactionOutputIndex));
        }

        return new NewTransactionResponseToClient(true, newTransaction, usedTransactions);
    }
}
