package sk.tuke.bitcoinmod.keyscapability;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import sk.tuke.bitcoinmod.EntryPoint;

@Mod.EventBusSubscriber(modid = EntryPoint.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class KeysCapabilityForgeEvents {
    @SubscribeEvent
    public static void onAttachCapability(AttachCapabilitiesEvent<World> event) {
        if(event.getObject() != null) {
            event.addCapability(new ResourceLocation(EntryPoint.MODID, "keys_capability_provider"), new KeysCapabilityProvider());
        }
    }
}
