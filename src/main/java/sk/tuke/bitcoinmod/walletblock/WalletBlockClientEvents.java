package sk.tuke.bitcoinmod.walletblock;

import net.minecraft.client.gui.ScreenManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import sk.tuke.bitcoinmod.EntryPoint;

/**
 * Trieda nacuvajuca na modovej zbernici, ktora prepaja WalletBlockContainer s WalletBlockScreenom. Spusta sa iba na klientskej strane
 */
@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = EntryPoint.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class WalletBlockClientEvents {
    /**
     * Prepoji WalletBlockContainer s WalletBlockScreenom aby hra vedela, aku obrazovku ma zobrazit pri pouziti bloku
     * @param event nevyuzity argument
     */
    @SubscribeEvent
    public static void onClientSetupEvent(FMLClientSetupEvent event){
        ScreenManager.registerFactory(WalletBlockCommonEvents.walletBlockContainerType, WalletBlockScreen::new);
    }
}
