package sk.tuke.bitcoinmod.helpers;

import net.minecraft.client.Minecraft;
import sk.tuke.bitcoinmod.interfaces.IRefreshable;

public class ScreenRefresher {
    public static void refreshScreen(){
        if(Minecraft.getInstance().currentScreen instanceof IRefreshable){
            ((IRefreshable)Minecraft.getInstance().currentScreen).refresh();
        }
    }
}
