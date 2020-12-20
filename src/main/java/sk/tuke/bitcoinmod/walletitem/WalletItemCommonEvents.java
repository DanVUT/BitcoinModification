package sk.tuke.bitcoinmod.walletitem;

import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import sk.tuke.bitcoinmod.EntryPoint;

@Mod.EventBusSubscriber(modid = EntryPoint.MODID, bus= Mod.EventBusSubscriber.Bus.MOD)
public class WalletItemCommonEvents {
    public static WalletItem walletItem;

    @SubscribeEvent
    public static void registerItem(RegistryEvent.Register<Item> event){
        walletItem = new WalletItem();
        event.getRegistry().register(walletItem);
    }

}
