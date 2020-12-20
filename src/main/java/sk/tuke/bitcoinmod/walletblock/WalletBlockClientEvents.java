package sk.tuke.bitcoinmod.walletblock;

import net.minecraft.client.gui.ScreenManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import sk.tuke.bitcoinmod.EntryPoint;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = EntryPoint.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class WalletBlockClientEvents {
    @SubscribeEvent
    public static void onClientSetupEvent(FMLClientSetupEvent event){
        ScreenManager.registerFactory(WalletBlockCommonEvents.walletBlockContainerType, WalletBlockScreen::new);
    }
}
