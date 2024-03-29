package sk.tuke.bitcoinmod.communication.newtransactionresponse;

import net.minecraft.client.world.ClientWorld;
import net.minecraft.world.World;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.LogicalSidedProvider;
import net.minecraftforge.fml.network.NetworkEvent;
import sk.tuke.bitcoinmod.helpers.ScreenRefresher;
import sk.tuke.bitcoinmod.transactionscapability.TransactionsCapability;
import sk.tuke.bitcoinmod.transactionscapability.TransactionsCapabilityProvider;

import java.util.Optional;
import java.util.function.Supplier;

/**
 * Trieda implementujuca obsluhu pre NewTransactionResponseToClient. Tato obsluha sa vykonáva na strane klienta
 */
public class NewTransactionResponseHandlerOnClient {

    /**
     * Ak server vytvoril novu transakciu, tak udaje o nej odosle vsetkym hracom. Klientske aplikacie hracov si tuto transakciu pridaju do svojej kopie TransactionCapability a oznacia minute transakcie.
     * @param message prijata sprava typu NewTransactionResponseToClient
     * @param ctxSupplier network context ktory obsahuje informacie o komunikacii
     */
    public static void handleMessage(NewTransactionResponseToClient message, Supplier<NetworkEvent.Context> ctxSupplier){
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
            if(!message.isNewTransactionCreated()){
                return;
            }
            World world = clientWorld.get();
            TransactionsCapability transactionsCapability = world.getCapability(TransactionsCapabilityProvider.CAPABILITY_TRANSACTIONS).orElse(null);

            transactionsCapability.addTransaction(message.getNewTransaction());
            transactionsCapability.markTransactionsAsSpent(message.getUsedTransactions());
            ScreenRefresher.refreshScreen();
        });
    }
}
