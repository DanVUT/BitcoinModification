package sk.tuke.bitcoinmod.communication.newtransactionrequest;

import javafx.util.Pair;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.Tuple;
import net.minecraft.world.World;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.PacketDistributor;
import sk.tuke.bitcoinmod.EntryPoint;
import sk.tuke.bitcoinmod.communication.CommunicationChannel;
import sk.tuke.bitcoinmod.communication.newtransactionresponse.NewTransactionResponseToClient;
import sk.tuke.bitcoinmod.keyscapability.KeysCapability;
import sk.tuke.bitcoinmod.keyscapability.KeysCapabilityProvider;
import sk.tuke.bitcoinmod.transactionscapability.TransactionsCapability;
import sk.tuke.bitcoinmod.transactionscapability.TransactionsCapabilityProvider;
import sk.tuke.bitcoinmod.transactionscapability.model.Transaction;

import java.util.List;
import java.util.function.Supplier;

public class NewTransactionRequestHandlerOnServer {
    public static void handleMessage(final NewTransactionRequestToServer message, Supplier<NetworkEvent.Context> ctxSupplier){
        NetworkEvent.Context context = ctxSupplier.get();
        context.setPacketHandled(true);

        if(!context.getDirection().getReceptionSide().isServer()){
            return;
        }

        if(!message.isValid()){
            return;
        }
        context.enqueueWork(() ->{
            World world = context.getSender().world;
            KeysCapability keysCapability = world.getCapability(KeysCapabilityProvider.CAPABILITY_KEYS, null).orElse(null);
            TransactionsCapability transactionsCapability = world.getCapability(TransactionsCapabilityProvider.CAPABILITY_TRANSACTIONS, null).orElse(null);
            if(!keysCapability.validateKeyPair(message.getSenderPrivateKey(), message.getSenderBitcoinAddress())){
                return;
            }

            try{
                Tuple<Transaction, List<Tuple<Integer, Integer>>> result = transactionsCapability.createNewTransaction(message.getSenderBitcoinAddress(), message.getRecipientBitcoinAddress(), message.getBitcoinAmount());
                CommunicationChannel.SIMPLECHANNEL.send(PacketDistributor.ALL.noArg(), new NewTransactionResponseToClient(true, result.getA(), result.getB()));
            } catch (RuntimeException e){
                ServerPlayerEntity player = context.getSender();
                CommunicationChannel.SIMPLECHANNEL.send(PacketDistributor.PLAYER.with(() -> player), new NewTransactionResponseToClient(false, null, null));
            }
        });
    }
}
