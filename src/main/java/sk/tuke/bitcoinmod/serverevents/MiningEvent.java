package sk.tuke.bitcoinmod.serverevents;

import com.google.common.collect.Collections2;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Tuple;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.PacketDistributor;
import sk.tuke.bitcoinmod.EntryPoint;
import sk.tuke.bitcoinmod.communication.CommunicationChannel;
import sk.tuke.bitcoinmod.communication.miningmessage.MiningMessageToClient;
import sk.tuke.bitcoinmod.communication.newtransactionresponse.NewTransactionResponseToClient;
import sk.tuke.bitcoinmod.miningblock.MiningBlockTileEntity;
import sk.tuke.bitcoinmod.transactionscapability.TransactionsCapability;
import sk.tuke.bitcoinmod.transactionscapability.TransactionsCapabilityProvider;
import sk.tuke.bitcoinmod.transactionscapability.model.Transaction;
import sk.tuke.bitcoinmod.walletitem.WalletItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


@Mod.EventBusSubscriber(modid = EntryPoint.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class MiningEvent {
    private static final int SECOND = 40;
    private static final int TARGET = SECOND * 10;
    private static int totalTicks = 0;

    private static List<MiningBlockTileEntity> getRelevantMiningBlockTileEntities(List<TileEntity> allTileEntities){
        List<MiningBlockTileEntity> result = new ArrayList<>();
        for(TileEntity te : allTileEntities){
            if(te instanceof MiningBlockTileEntity){
                MiningBlockTileEntity miningBlockTE = (MiningBlockTileEntity) te;

                if(miningBlockTE.getWalletItemStack() != ItemStack.EMPTY){
                    result.add(miningBlockTE);
                }
            }
        }
        return result;
    }

    private static List<Integer> getRaffleList(List<MiningBlockTileEntity> miningBlockTileEntities){
        List<Integer> result = new ArrayList<>();

        for(MiningBlockTileEntity te : miningBlockTileEntities){
            int diamondsCount = te.getDiamondsCount();
            result.add(miningBlockTileEntities.indexOf(te));
            for(int i = 0; i < diamondsCount; i++){
                result.add(miningBlockTileEntities.indexOf(te));
            }
        }
        return result;
    }

    @SubscribeEvent
    public static void onTickEvent(TickEvent.WorldTickEvent event){
        totalTicks += 1;
        if((totalTicks % TARGET) != 0){
            return;
        }
        TransactionsCapability transactionsCapability = event.world.getCapability(TransactionsCapabilityProvider.CAPABILITY_TRANSACTIONS, null).orElse(null);

        List<MiningBlockTileEntity> miningTileEntities = getRelevantMiningBlockTileEntities(event.world.loadedTileEntityList);
        if(miningTileEntities.size() <= 0){
            return;
        }
        List<Integer> raffleList = getRaffleList(miningTileEntities);
        int pickedNumber = new Random().nextInt(raffleList.size());
        MiningBlockTileEntity pickedTE = miningTileEntities.get(pickedNumber);
        Tuple<Long, Long> keyPair = WalletItem.getKeyPairFromWalletItemStack(pickedTE.getWalletItemStack());
        Transaction transaction = transactionsCapability.createBaseTransaction(keyPair.getB());

        CommunicationChannel.SIMPLECHANNEL.send(PacketDistributor.ALL.noArg(), new MiningMessageToClient(transaction));

        for(PlayerEntity player: event.world.getPlayers()){
            player.sendMessage(new StringTextComponent("Block Mined by Bitcoin Address: " + Long.toHexString(keyPair.getB())));
        }
    }
}
