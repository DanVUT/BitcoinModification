package sk.tuke.bitcoinmod.communication;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import sk.tuke.bitcoinmod.EntryPoint;
import sk.tuke.bitcoinmod.communication.alltransactions.AllTransactionsMessageHandlerOnClient;
import sk.tuke.bitcoinmod.communication.alltransactions.AllTransactionsMessageToClient;
import sk.tuke.bitcoinmod.communication.generatewallet.GenerateWalletMessageHandlerOnServer;
import sk.tuke.bitcoinmod.communication.generatewallet.GenerateWalletMessageToServer;
import sk.tuke.bitcoinmod.communication.miningmessage.MiningMessageHandlerOnClient;
import sk.tuke.bitcoinmod.communication.miningmessage.MiningMessageToClient;
import sk.tuke.bitcoinmod.communication.newtransactionrequest.NewTransactionRequestHandlerOnServer;
import sk.tuke.bitcoinmod.communication.newtransactionrequest.NewTransactionRequestToServer;
import sk.tuke.bitcoinmod.communication.newtransactionresponse.NewTransactionResponseHandlerOnClient;
import sk.tuke.bitcoinmod.communication.newtransactionresponse.NewTransactionResponseToClient;

import java.util.Optional;

import static net.minecraftforge.fml.network.NetworkDirection.PLAY_TO_CLIENT;
import static net.minecraftforge.fml.network.NetworkDirection.PLAY_TO_SERVER;

@Mod.EventBusSubscriber(modid = EntryPoint.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CommunicationChannel {
    public static final String MESSAGE_PROTOCOL_VERSION = "1";
    public static SimpleChannel SIMPLECHANNEL;

    @SubscribeEvent
    public static void onCommonSetupEvent(FMLCommonSetupEvent event){
        SIMPLECHANNEL = NetworkRegistry.newSimpleChannel(
                new ResourceLocation(EntryPoint.MODID, "mainchanel"),
                () -> MESSAGE_PROTOCOL_VERSION,
                MESSAGE_PROTOCOL_VERSION::equals,
                MESSAGE_PROTOCOL_VERSION::equals
        );

        SIMPLECHANNEL.registerMessage(42,
                GenerateWalletMessageToServer.class,
                GenerateWalletMessageToServer::encode,
                GenerateWalletMessageToServer::decode,
                GenerateWalletMessageHandlerOnServer::handleMessage,
                Optional.of(PLAY_TO_SERVER));

        SIMPLECHANNEL.registerMessage(43,
                AllTransactionsMessageToClient.class,
                AllTransactionsMessageToClient::encode,
                AllTransactionsMessageToClient::decode,
                AllTransactionsMessageHandlerOnClient::handleMessage,
                Optional.of(PLAY_TO_CLIENT));

        SIMPLECHANNEL.registerMessage(44,
                NewTransactionRequestToServer.class,
                NewTransactionRequestToServer::encode,
                NewTransactionRequestToServer::decode,
                NewTransactionRequestHandlerOnServer::handleMessage,
                Optional.of(PLAY_TO_SERVER));

        SIMPLECHANNEL.registerMessage(45,
                NewTransactionResponseToClient.class,
                NewTransactionResponseToClient::encode,
                NewTransactionResponseToClient::decode,
                NewTransactionResponseHandlerOnClient::handleMessage,
                Optional.of(PLAY_TO_CLIENT));

        SIMPLECHANNEL.registerMessage(46,
                MiningMessageToClient.class,
                MiningMessageToClient::encode,
                MiningMessageToClient::decode,
                MiningMessageHandlerOnClient::handleMessage,
                Optional.of(PLAY_TO_CLIENT));
    }
}
