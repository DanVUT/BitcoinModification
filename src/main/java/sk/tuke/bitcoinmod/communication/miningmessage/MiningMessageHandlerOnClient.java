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


/**
 * Obsluha spravy MiningMessageToClient, ktoru prijme klientska aplikacia ked server vyberie Bitcoin Adresu, ktora vytazi Bitcoiny.
 * */
public class MiningMessageHandlerOnClient {

    /**
     * Obsluha spravy MiningMessageToClient prijme jednu Coinbase transakciu, ktoru prida do svojej kopie TransactionsCapability
     * @param message prijata sprava typu MiningMessageToClient
     * @param ctxSupplier network context obsahujuci informacie o komunikacii
     */
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
