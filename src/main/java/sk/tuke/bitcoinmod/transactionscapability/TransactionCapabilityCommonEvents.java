package sk.tuke.bitcoinmod.transactionscapability;

import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import sk.tuke.bitcoinmod.EntryPoint;

/**
 * Trieda nacuva na modovej zbernici a registruje TransactionsCapability do hry
 */
@Mod.EventBusSubscriber(modid = EntryPoint.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class TransactionCapabilityCommonEvents {
    /**
     * Obsluha registruje Transactions Capability do hry
     * @param event nevyuzity argument
     */
    @SubscribeEvent
    public static void onCommonSetupEvent(FMLCommonSetupEvent event) {
        CapabilityManager.INSTANCE.register(TransactionsCapability.class, new TransactionsCapability.TransactionsCapabilityStorage(), TransactionsCapability::new);
    }
}
