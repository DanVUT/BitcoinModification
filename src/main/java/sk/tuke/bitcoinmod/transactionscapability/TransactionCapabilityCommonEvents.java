package sk.tuke.bitcoinmod.transactionscapability;

import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import sk.tuke.bitcoinmod.EntryPoint;

@Mod.EventBusSubscriber(modid = EntryPoint.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class TransactionCapabilityCommonEvents {
    @SubscribeEvent
    public static void onCommonSetupEvent(FMLCommonSetupEvent event) {
        CapabilityManager.INSTANCE.register(TransactionsCapability.class, new TransactionsCapability.TransactionsCapabilityStorage(), TransactionsCapability::new);
    }
}
