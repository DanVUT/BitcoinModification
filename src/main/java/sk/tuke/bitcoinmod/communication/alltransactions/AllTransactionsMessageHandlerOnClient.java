package sk.tuke.bitcoinmod.communication.alltransactions;

import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.world.World;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.LogicalSidedProvider;
import net.minecraftforge.fml.network.NetworkEvent;
import sk.tuke.bitcoinmod.helpers.ScreenRefresher;
import sk.tuke.bitcoinmod.interfaces.IRefreshable;
import sk.tuke.bitcoinmod.transactionscapability.TransactionsCapability;
import sk.tuke.bitcoinmod.transactionscapability.TransactionsCapabilityProvider;

import java.util.Optional;
import java.util.function.Supplier;

public class AllTransactionsMessageHandlerOnClient {
    public static void handleMessage(AllTransactionsMessageToClient message, Supplier<NetworkEvent.Context> ctxSupplier){
        NetworkEvent.Context context = ctxSupplier.get();
        context.setPacketHandled(true);

        if(context == null){
            return;
        }

        if(context.getDirection().getReceptionSide() != LogicalSide.CLIENT){
            return;
        }

        if(!message.isValid()){
            return;
        }

        Optional<ClientWorld> clientWorld = LogicalSidedProvider.CLIENTWORLD.get(LogicalSide.CLIENT);

        context.enqueueWork(() -> {
            TransactionsCapability transactionsCapability = clientWorld.get().getCapability(TransactionsCapabilityProvider.CAPABILITY_TRANSACTIONS).orElse(null);
            transactionsCapability.mapTransactions(message.getAllTransactions());
            ScreenRefresher.refreshScreen();
        });
    }
}
