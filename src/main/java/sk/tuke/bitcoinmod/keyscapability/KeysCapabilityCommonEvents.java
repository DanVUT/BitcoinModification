package sk.tuke.bitcoinmod.keyscapability;

import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import sk.tuke.bitcoinmod.EntryPoint;

/**
 * Trieda nacuvajuca na modovej zbernici, ktora sluzi k registracii KeysCapability do hry
 */
@Mod.EventBusSubscriber(modid = EntryPoint.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class KeysCapabilityCommonEvents {
    /**
     * V ramci tohoto eventu sa zaregistruje KeysCapability do hry
     * @param event nepouzity argument
     */
    @SubscribeEvent
    public static void onCommonSetupEvent(FMLCommonSetupEvent event) {
        CapabilityManager.INSTANCE.register(KeysCapability.class, new KeysCapability.KeysCapabilityStorage(), KeysCapability::new);
    }
}
