package sk.tuke.bitcoinmod.miningblock;

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
 * Trieda nacuva na modovej zbernici a registruje vsetky potrebne zavislosti MiningBlocku
 */
@Mod.EventBusSubscriber(modid= EntryPoint.MODID, bus= Mod.EventBusSubscriber.Bus.MOD)
public class MiningBlockCommonEvents {
    public static MiningBlock miningBlock;
    public static BlockItem miningBlockItem;
    public static ContainerType<MiningBlockContainer> miningBlockContainerType;
    public static TileEntityType<MiningBlockTileEntity> miningBlockTileEntityType;

    /**
     * Obsluha registruje do hry Mining Block
     * @param event kontext obsluhy registracie bloku
     */
    @SubscribeEvent
    public static void registerBlock(final RegistryEvent.Register<Block> event){
        miningBlock = new MiningBlock();
        event.getRegistry().register(miningBlock);
    }

    /**
     * Obsluha registruje do hry Mining Block ako predmet
     * @param event kontext obsluhy registracie bloku ako predmetu
     */
    @SubscribeEvent
    public static void registerItem(final RegistryEvent.Register<Item> event){
        Item.Properties properties = new Item.Properties().maxStackSize(1).group(ItemGroup.MISC);
        miningBlockItem = new BlockItem(miningBlock, properties);
        miningBlockItem.setRegistryName(miningBlock.getRegistryName());
        event.getRegistry().register(miningBlockItem);
    }

    /**
     * Obsluha registruje do hry TileEntity pre Mining Block
     * @param event kontext registracie TileEntity
     */
    @SubscribeEvent
    public static void registerTileEntity(final RegistryEvent.Register<TileEntityType<?>> event){
        miningBlockTileEntityType = TileEntityType.Builder.create(MiningBlockTileEntity::new, miningBlock).build(null);
        miningBlockTileEntityType.setRegistryName(EntryPoint.MODID, "mining_block_tile_entity");
        event.getRegistry().register(miningBlockTileEntityType);
    }

    /**
     * Obsluha registruje do hry Container pre Mining Block
     * @param event kontext registracie Containeru
     */
    @SubscribeEvent
    public static void registerContainer(final RegistryEvent.Register<ContainerType<?>> event){
        miningBlockContainerType = IForgeContainerType.create(MiningBlockContainer::createForClient);
        miningBlockContainerType.setRegistryName(EntryPoint.MODID, "mining_block_container");
        event.getRegistry().register(miningBlockContainerType);
    }
}