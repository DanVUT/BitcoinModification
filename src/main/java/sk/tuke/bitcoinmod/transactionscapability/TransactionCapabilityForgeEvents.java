package sk.tuke.bitcoinmod.transactionscapability;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import sk.tuke.bitcoinmod.EntryPoint;

/**
 * Trieda nacuva na Forge zbernici a prepaja Transactions Capability s hernou entitou World
 */
@Mod.EventBusSubscriber(modid = EntryPoint.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class TransactionCapabilityForgeEvents {
    /**
     * Prepoji Transactions Capability s hernym svetom (World)
     * @param event kontext registracie capabilities
     */
    @SubscribeEvent
    public static void onAttachCapability(AttachCapabilitiesEvent<World> event) {
        if(event.getObject() != null) {
            event.addCapability(new ResourceLocation(EntryPoint.MODID, "transactions_capability_provider"), new TransactionsCapabilityProvider());
        }
    }
}
