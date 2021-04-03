package sk.tuke.bitcoinmod.communication.generatewallet;

import net.minecraft.network.PacketBuffer;


/**
 * Trieda reprezentujuca spravu, ktora sa posle ked hrac v Bitcoin Wallet Blocku klikne na tlacitko New Bitcoin Key Pair. Tato sprava nema vo vysledku ziadny obsah. Jej samotne prijatie je znak toho, ze server ma hracovi vygenerovat novy par klucov.
 */
public class GenerateWalletMessageToServer {
    private boolean isMessageValid;

    public GenerateWalletMessageToServer(){
        isMessageValid = true;
    }

    public boolean isMessageValid() {
        return isMessageValid;
    }

    public void encode(PacketBuffer buffer){
        if(!isMessageValid){
            return;
        }
        buffer.writeBoolean(true);
    }

    public static GenerateWalletMessageToServer decode(PacketBuffer buffer){
        GenerateWalletMessageToServer returnValue = new GenerateWalletMessageToServer();
        if(buffer.readBoolean()){
            return returnValue;
        }
        return null;
    }
}
