package sk.tuke.bitcoinmod.communication.miningmessage;

import net.minecraft.network.PacketBuffer;
import sk.tuke.bitcoinmod.transactionscapability.model.Transaction;
import sk.tuke.bitcoinmod.transactionscapability.model.TransactionOutput;

/**
 * Trieda reprezentujuca spravu ktoru server odosle klientom po "vytazeni" Bitcoinov. Obsah tejto spravy obsahuje len jednu transakciu.
 */
public class MiningMessageToClient {
    private Transaction baseTransaction;
    private boolean isValid;

    /**
     * @param baseTransaction Coinbase transakcia, ktora je vysledkom posledneho tazenia
     */
    public MiningMessageToClient(Transaction baseTransaction) {
        this.baseTransaction = baseTransaction;
        this.isValid = true;
    }

    /**
     * Getter Coinbase transakcie
     * @return Coinbase transakcia
     */
    public Transaction getBaseTransaction() {
        return baseTransaction;
    }

    public boolean isValid() {
        return isValid;
    }


    /**
     * Metoda zakoduje obsah spravy do Packet Buffera. Vola sa na strane servera
     * @param buffer Packet Buffer do ktoreho sa zakoduje Coinbase transakcia
     */
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

    /**
     * Metoda dekoduje obsah spravy z Packet Buffera. Vola sa na strane klienta
     * @param buffer Packet Buffer z ktoreho sa cita obsah spravy
     * @return instancia triedy MiningMessageToClient
     */
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
