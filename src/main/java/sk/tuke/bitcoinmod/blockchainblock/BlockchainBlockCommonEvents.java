package sk.tuke.bitcoinmod.blockchainblock;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import sk.tuke.bitcoinmod.EntryPoint;

/**
 * Trieda nacuvajuca na modovej zbernici. Registruje Blockchain Block do hry
 */
@Mod.EventBusSubscriber(modid = EntryPoint.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class BlockchainBlockCommonEvents {
    public static BlockchainBlock blockchainBlock;
    public static BlockItem blockChainBlockItem;

    /**
     * Metoda ktora oboznami hru s Blockchain Blockom.
     * @param event parameter registracie bloku. Vyuziva sa k registracii instancie bloku
     */
    @SubscribeEvent
    public static void registerBlock(RegistryEvent.Register<Block> event){
        blockchainBlock = new BlockchainBlock();
        event.getRegistry().register(blockchainBlock);
    }

    /**
     * Metoda ktora zaregistruje Blockchain Block aj ako herny predmet.
     * @param event parameter registracie predmetu
     */
    @SubscribeEvent
    public static void registerBlockItem(RegistryEvent.Register<Item> event){
        Item.Properties properties = new Item.Properties().group(ItemGroup.MISC).maxStackSize(1);
        blockChainBlockItem = new BlockItem(blockchainBlock, properties);
        blockChainBlockItem.setRegistryName(blockchainBlock.getRegistryName());
        event.getRegistry().register(blockChainBlockItem);
    }
}
