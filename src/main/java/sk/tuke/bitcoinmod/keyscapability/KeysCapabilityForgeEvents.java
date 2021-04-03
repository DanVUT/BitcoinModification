package sk.tuke.bitcoinmod.keyscapability;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import sk.tuke.bitcoinmod.EntryPoint;

/**
 * Trieda nacuvajuca na Forge zbernici ktora sluzi pre prepojenie KeysCapability s instanciou typu World. Takze so svetom ktory bezi na serveri
 */
@Mod.EventBusSubscriber(modid = EntryPoint.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class KeysCapabilityForgeEvents {
    /**
     * Tato metoda prepoji KeysCapability s instanciou typu World
     * @param event pomocou tohoto argumentu sa prepoji KeysCapability s hernym svetom
     */
    @SubscribeEvent
    public static void onAttachCapability(AttachCapabilitiesEvent<World> event) {
        if(event.getObject() != null) {
            event.addCapability(new ResourceLocation(EntryPoint.MODID, "keys_capability_provider"), new KeysCapabilityProvider());
        }
    }
}
