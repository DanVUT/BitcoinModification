package sk.tuke.bitcoinmod.miningblock;

import net.minecraft.client.gui.ScreenManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import sk.tuke.bitcoinmod.EntryPoint;


@Mod.EventBusSubscriber(modid= EntryPoint.MODID, bus= Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class MiningBlockClientEvents {
    @SubscribeEvent
    public static void onClientSetupEvent(FMLClientSetupEvent event){
        ScreenManager.registerFactory(MiningBlockCommonEvents.miningBlockContainerType, MiningBlockScreen::new);
    }
}
