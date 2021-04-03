package sk.tuke.bitcoinmod.walletitem;

import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import sk.tuke.bitcoinmod.EntryPoint;

/**
 * Trieda nacuvajuca na modovej zbernici. Sluzi k zaregistrovaniu predmetu typu Wallet Item do hry
 */
@Mod.EventBusSubscriber(modid = EntryPoint.MODID, bus= Mod.EventBusSubscriber.Bus.MOD)
public class WalletItemCommonEvents {
    public static WalletItem walletItem;

    /**
     * Obsluha, ktora zaregistruje predmet typu Wallet Item do hry
     * @param event parameter pomocou ktoreho sa zaregistruje predmet do hry
     */
    @SubscribeEvent
    public static void registerItem(RegistryEvent.Register<Item> event){
        walletItem = new WalletItem();
        event.getRegistry().register(walletItem);
    }

}
