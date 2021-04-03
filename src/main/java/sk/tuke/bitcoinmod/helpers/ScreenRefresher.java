package sk.tuke.bitcoinmod.helpers;

import net.minecraft.client.Minecraft;
import sk.tuke.bitcoinmod.interfaces.IRefreshable;

/**
 * Trieda ktora implementuje obnovenie (refresh) okien v pripade, ze implementujú IRefreshable rozhranie
 */
public class ScreenRefresher {
    /**
     * Metoda obnovi (refresh) hracove okno v pripade ze toto okno implementuje IRefreshable rozhranie
     */
    public static void refreshScreen(){
        if(Minecraft.getInstance().currentScreen instanceof IRefreshable){
            ((IRefreshable)Minecraft.getInstance().currentScreen).refresh();
        }
    }
}
