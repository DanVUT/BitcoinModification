package sk.tuke.bitcoinmod.communication;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import sk.tuke.bitcoinmod.EntryPoint;
import sk.tuke.bitcoinmod.communication.generatewallet.GenerateWalletMessageHandlerOnServer;
import sk.tuke.bitcoinmod.communication.generatewallet.GenerateWalletMessageToServer;

import java.util.Optional;

import static net.minecraftforge.fml.network.NetworkDirection.PLAY_TO_SERVER;

@Mod.EventBusSubscriber(modid = EntryPoint.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CommunicationChannel {
    public static final String MESSAGE_PROTOCOL_VERSION = "1";
    public static SimpleChannel SIMPLECHANNEL;

    @SubscribeEvent
    public static void onCommonSetupEvent(FMLCommonSetupEvent event){
        SIMPLECHANNEL = NetworkRegistry.newSimpleChannel(
                new ResourceLocation(EntryPoint.MODID, "mainchannel"),
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
    }
}
