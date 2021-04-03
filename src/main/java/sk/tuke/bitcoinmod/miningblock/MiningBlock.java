package sk.tuke.bitcoinmod.miningblock;

import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.ContainerBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import sk.tuke.bitcoinmod.EntryPoint;

import javax.annotation.Nullable;

/**
 * Trieda implementujuca Mining Block
 */
public class MiningBlock extends ContainerBlock {

    /**
     * Tradicny konstruktor ktory nastavuje tvrdost bloku a nastavuje RegistryName na "mining_block"
     */
    public MiningBlock() {
        super(Properties.create(Material.IRON).hardnessAndResistance(1.15f));
        this.setRegistryName(EntryPoint.MODID, "mining_block");
    }

    /**
     * getter ktory vracia vzdy true, pretoze tento blok obsahuje TileEntity
     * @return vracia True
     */
    @Override
    public boolean hasTileEntity() {
        return true;
    }

    /**
     * @param worldIn nepouzity argument
     * @return vracia novu instanciu triedy MiningBlockTileEntity
     */
    @Nullable
    @Override
    public TileEntity createNewTileEntity(IBlockReader worldIn) {
        return new MiningBlockTileEntity();
    }

    /**
     * Metoda ktora sa vyvola ked sa pouzije Mining Block. Tato metoda hracovi zo strany serveru otvori GUI okno
     * @param state nepouzity argument
     * @param worldIn instancia triedy World
     * @param pos nepouzity argument
     * @param player hrac ktory pouzil blok
     * @param handIn nepouzity argument
     * @param hit nepouzity argument
     * @return ActionResultType.SUCCESS alebo ActionResultType.FAIL
     */
    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if(worldIn.isRemote){
            return ActionResultType.SUCCESS;
        }
        INamedContainerProvider namedContainerProvider = this.getContainer(state, worldIn, pos);

        if(namedContainerProvider != null){
            if(!(player instanceof ServerPlayerEntity)){
                return ActionResultType.FAIL;
            }
            ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) player;

            NetworkHooks.openGui(serverPlayerEntity, namedContainerProvider, (packetBuffer -> {}));
        }
        return ActionResultType.SUCCESS;
    }

    /**
     * Tato trieda sluzi k tomu, aby pri zniceni bloku z neho vypadol cely obsah
     * @param state stary stav bloku
     * @param worldIn instancia triedy World
     * @param pos pozicia bloku
     * @param newState novy stav bloku
     * @param isMoving nepouzity argument
     */
    @Override
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if(state.getBlock() != newState.getBlock()){
            TileEntity tileEntity = worldIn.getTileEntity(pos);
            if(tileEntity instanceof MiningBlockTileEntity){
                MiningBlockTileEntity miningBlockTileEntity = (MiningBlockTileEntity) tileEntity;
                miningBlockTileEntity.dropAllContents(worldIn, pos);
            }
        }
        super.onReplaced(state, worldIn, pos, newState, isMoving);
    }

    /**
     * Tato metoda definuje, akym sposobom by sa mal blok rednerovat. V tomto pripade definujeme, aby sa blok vykresloval podla modelu
     * @param state nepouzity argument
     * @return BlockRenderType.MODEL
     */
    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

}
