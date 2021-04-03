package sk.tuke.bitcoinmod.communication.newtransactionrequest;

import net.minecraft.network.PacketBuffer;


/**
 * Trieda reprezentujuca poziadavku na server o vytvorenie novej transakcie
 */
public class NewTransactionRequestToServer {
    private long senderPrivateKey;
    private long senderBitcoinAddress;
    private long recipientBitcoinAddress;
    private float bitcoinAmount;
    private boolean isValid;


    /**
     * @param senderPrivateKey privatny kluc odosielatela na kontrolu hashovania privatneho kluca na verejny kluc
     * @param senderBitcoinAddress verejny kluc odosielatela
     * @param recipientBitcoinAddress verejny kluc prijimatela
     * @param bitcoinAmount pozadovana suma na odoslanie
     */
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

    /**
     * Metoda ktora zakoduje obsah spravy do Packet Buffera
     * @param buffer Packet Buffer do ktoreho sa zakoduje obsah spravy
     */
    public void encode(PacketBuffer buffer){
        buffer.writeLong(senderPrivateKey);
        buffer.writeLong(senderBitcoinAddress);
        buffer.writeLong(recipientBitcoinAddress);
        buffer.writeFloat(bitcoinAmount);
    }

    /**
     * Metoda ktora precita obsah spravy z Packet Buffera a vrati novu instanciu typu NewTransactionRequestToServer
     * @param buffer Packet Buffer obsahujuci obsah spravy
     * @return instancia triedy NewTransactionRequestToServer
     */
    public static NewTransactionRequestToServer decode(PacketBuffer buffer){
        long senderPrivateKey = buffer.readLong();
        long senderBitcoinAddress = buffer.readLong();
        long recipientBitcoinAddress = buffer.readLong();
        float bitcoinAmount = buffer.readFloat();

        return new NewTransactionRequestToServer(senderPrivateKey, senderBitcoinAddress, recipientBitcoinAddress, bitcoinAmount);
    }
}
