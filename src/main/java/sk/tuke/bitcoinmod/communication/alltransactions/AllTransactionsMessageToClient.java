package sk.tuke.bitcoinmod.communication.alltransactions;

import net.minecraft.network.PacketBuffer;
import sk.tuke.bitcoinmod.transactionscapability.model.Transaction;
import sk.tuke.bitcoinmod.transactionscapability.model.TransactionOutput;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Sprava obsahujuca vsetky transakcie na serveri. Server odosiela tuto spravu hracovi ked sa pripoji na server
 */
public class AllTransactionsMessageToClient {
    private List<Transaction> allTransactions;
    private boolean isValid;

    /**
     * Konstruktor skopiruje transakcie do lokalneho listu. Tento konstruktor sa vyuziva na strane serveru
     * @param transactionsMap mapa vsetkych transakcii na serveri
     */
    public AllTransactionsMessageToClient(Map<Integer, Transaction> transactionsMap){
        this.allTransactions = new ArrayList<>();
        this.allTransactions.addAll(transactionsMap.values());
        this.isValid = true;
    }

    /**
     * Tento konstrutor sa vyuziva na strane klienta
     * @param transactionList List transakcii obdrzany klientom
     */
    public AllTransactionsMessageToClient(List<Transaction> transactionList){
        this.allTransactions = transactionList;
        this.isValid = true;
    }

    /**
     * @return vrati list vsetkych transakcii
     */
    public List<Transaction> getAllTransactions() {
        return allTransactions;
    }

    public boolean isValid() {
        return isValid;
    }

    /**
     * Metoda, ktora zakoduje vsetky transakcie do packetoveho buffera. Pouziva sa na strane serveru.
     * @param buffer buffer do ktoreho sa zakoduju vsetky transakcie
     */
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

    /**
     * Metoda dekoduje vsetky transakcie z packetoveho buffera. Pouziva sa na strane klienta
     * @param buffer buffer z ktoreho sa citaju vsetky transakcie
     * @return vrati instanciu spravy typu AllTransactionsMessageToClient
     */
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
