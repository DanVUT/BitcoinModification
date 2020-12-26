package sk.tuke.bitcoinmod.communication.miningmessage;

import net.minecraft.client.world.ClientWorld;
import net.minecraft.world.World;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.LogicalSidedProvider;
import net.minecraftforge.fml.network.NetworkEvent;
import sk.tuke.bitcoinmod.EntryPoint;
import sk.tuke.bitcoinmod.helpers.ScreenRefresher;
import sk.tuke.bitcoinmod.transactionscapability.TransactionsCapability;
import sk.tuke.bitcoinmod.transactionscapability.TransactionsCapabilityProvider;

import java.util.Optional;
import java.util.function.Supplier;

public class MiningMessageHandlerOnClient {
    public static void handleMessage(MiningMessageToClient message, Supplier<NetworkEvent.Context> ctxSupplier){
        NetworkEvent.Context context = ctxSupplier.get();
        context.setPacketHandled(true);
        if(context == null){
            return;
        }

        if(!context.getDirection().getReceptionSide().isClient()){
            return;
        }

        if(!message.isValid()){
            return;
        }

        Optional<ClientWorld> clientWorld = LogicalSidedProvider.CLIENTWORLD.get(LogicalSide.CLIENT);
        context.enqueueWork(() -> {
            World world = clientWorld.get();
            TransactionsCapability transactionsCapability = world.getCapability(TransactionsCapabilityProvider.CAPABILITY_TRANSACTIONS, null).orElse(null);
            transactionsCapability.addTransaction(message.getBaseTransaction());
            ScreenRefresher.refreshScreen();
        });
    }
}
