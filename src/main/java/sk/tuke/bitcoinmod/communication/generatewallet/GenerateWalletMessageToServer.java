package sk.tuke.bitcoinmod.communication.generatewallet;

import net.minecraft.network.PacketBuffer;

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
