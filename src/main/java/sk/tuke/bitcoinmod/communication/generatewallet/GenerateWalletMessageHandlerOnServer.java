package sk.tuke.bitcoinmod.communication.generatewallet;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Tuple;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent;
import sk.tuke.bitcoinmod.keyscapability.KeysCapability;
import sk.tuke.bitcoinmod.keyscapability.KeysCapabilityProvider;
import sk.tuke.bitcoinmod.walletblock.WalletBlockContainer;
import sk.tuke.bitcoinmod.walletitem.WalletItem;
import sk.tuke.bitcoinmod.walletitem.WalletItemCommonEvents;

import java.util.function.Supplier;


/**
 * Trieda implementujuca obsluhu na strane servera pre prijatie spravy typu GenerateWalletMessageToServer. Tato sprava sa obdrzi ked hrac klikne vo svojej klientskej aplikacii na tlacitko New Bitcoin Key Pair
 */
public class GenerateWalletMessageHandlerOnServer {

    /**
     * Obsluha, ktora v hracovom Bitcoin Wallet Blocku vygeneruje novy Bitcoin Key Pair
     * @param message prijata sprava typu GenerateWalletMessageToServer
     * @param ctxSupplier network context poskytujuci informacie o komunikacii
     */
    public static void handleMessage(final GenerateWalletMessageToServer message, Supplier<NetworkEvent.Context> ctxSupplier){
        NetworkEvent.Context context = ctxSupplier.get();
        context.setPacketHandled(true);

        if(context.getDirection().getReceptionSide() != LogicalSide.SERVER){
            return;
        }

        if(!message.isMessageValid()){
            return;
        }
        ServerPlayerEntity player = context.getSender();
        if(player == null){
            return;
        }

        context.enqueueWork(() -> {
            WalletBlockContainer container = (WalletBlockContainer) player.openContainer;
            KeysCapability keysCapability = player.world.getCapability(KeysCapabilityProvider.CAPABILITY_KEYS, null).orElse(null);
            Tuple<Long,Long> keyPair = keysCapability.generateKeyPair();
            ItemStack newWallet = WalletItem.createWalletItemStack(keyPair.getA(), keyPair.getB());
            container.setGeneratedWalletSlot(newWallet);
        });

    }
}
