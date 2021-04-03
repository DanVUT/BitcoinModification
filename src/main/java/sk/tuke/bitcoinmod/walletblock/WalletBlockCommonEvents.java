package sk.tuke.bitcoinmod.walletblock;

import net.minecraft.block.Block;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import sk.tuke.bitcoinmod.EntryPoint;

/**
 * Trieda načuva na modovej zbernici a registruje WalletBlock (Bitcoin Wallet Block) a vsetky jej sucasti
 */
@Mod.EventBusSubscriber(modid= EntryPoint.MODID, bus= Mod.EventBusSubscriber.Bus.MOD)
public class WalletBlockCommonEvents {
    public static WalletBlock walletBlock;
    public static BlockItem walletBlockItem;
    public static TileEntityType<WalletBlockTileEntity> walletBlockTileEntityType;
    public static ContainerType<WalletBlockContainer> walletBlockContainerType;

    /**
     * Obsluha zaregistruje WalletBlock do hry
     * @param event kontext registracie
     */
    @SubscribeEvent
    public static void registerBlock(final RegistryEvent.Register<Block> event){
        walletBlock = new WalletBlock();
        event.getRegistry().register(walletBlock);
    }
    /**
     * Obsluha zaregistruje WalletBlock do hry ako predmet
     * @param event kontext registracie
     */
    @SubscribeEvent
    public static void registerItem(final RegistryEvent.Register<Item> event){
        Item.Properties properties = new Item.Properties().maxStackSize(1).group(ItemGroup.MISC);
        walletBlockItem = new BlockItem(walletBlock, properties);
        walletBlockItem.setRegistryName(walletBlock.getRegistryName());
        event.getRegistry().register(walletBlockItem);
    }

    /**
     * Obsluha zaregistruje WalletBlockTileEntity do hry
     * @param event kontext registracie
     */
    @SubscribeEvent
    public static void registerTileEntity(final RegistryEvent.Register<TileEntityType<?>> event){
        walletBlockTileEntityType = TileEntityType.Builder.create(WalletBlockTileEntity::new, walletBlock).build(null);
        walletBlockTileEntityType.setRegistryName("bitcoinmod:wallet_block_tile_entity");
        event.getRegistry().register(walletBlockTileEntityType);
    }

    /**
     * Obsluha zaregistruje WalletBlockContainer do hry
     * @param event kontext registracie
     */
    @SubscribeEvent
    public static void registerContainer(final RegistryEvent.Register<ContainerType<?>> event){
        walletBlockContainerType = IForgeContainerType.create(WalletBlockContainer::createForClientSide);
        walletBlockContainerType.setRegistryName("wallet_block_container");
        event.getRegistry().register(walletBlockContainerType);
    }
}
