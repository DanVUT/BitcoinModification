package sk.tuke.bitcoinmod.miningblock;

import net.minecraft.client.gui.ScreenManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import sk.tuke.bitcoinmod.EntryPoint;


/**
 * Trieda nacuvajuca na modovej zbernici, ktora vola obsluhu pre MiningBlock iba na klientskej strane
 */
@Mod.EventBusSubscriber(modid= EntryPoint.MODID, bus= Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class MiningBlockClientEvents {
    /**
     * Metoda prepaja MiningBlockContainerType a MiningBlockScreen aby hra vedela aku obrazovku ma otvorit v pripade pouzitia MiningBlocku
     * @param event nepouzity argument
     */
    @SubscribeEvent
    public static void onClientSetupEvent(FMLClientSetupEvent event){
        ScreenManager.registerFactory(MiningBlockCommonEvents.miningBlockContainerType, MiningBlockScreen::new);
    }
}
