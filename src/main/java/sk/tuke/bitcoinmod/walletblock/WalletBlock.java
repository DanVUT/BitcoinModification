package sk.tuke.bitcoinmod.walletblock;

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
 * Trieda implementujuca WalletBlock (Bitcoin Wallet Block)
 */
public class WalletBlock extends ContainerBlock {
    /**
     * Konstruktor nastavuje tvrdost bloku aby sa nedal rozbit na jednu ranu a RegistryName na "wallet_block"
     */
    public WalletBlock() {
        super(Properties.create(Material.IRON).hardnessAndResistance(1.15f));
        this.setRegistryName(EntryPoint.MODID, "wallet_block");
    }

    /**
     * Informacia ci blok obsahuje TileEntity
     * @return vzdy true
     */
    @Override
    public boolean hasTileEntity() {
        return true;
    }

    /**
     * @param worldIn nevyuzity argument
     * @return vrati novu instanciu WalletBlockTileEntity
     */
    @Nullable
    @Override
    public TileEntity createNewTileEntity(IBlockReader worldIn) {
        return new WalletBlockTileEntity();
    }

    /**
     * Pri pouziti bloku hracom mu otvori GUI
     * @param state stav bloku
     * @param worldIn instancia svetu
     * @param pos pozicia bloku
     * @param player hrac ktory interaguje s blokom
     * @param handIn nevyuzity argument
     * @param hit nevyuzity argument
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
     * V pripade, ze je blok rozbity, tak tato metoda sa stara o vyhodenie jeho obsahu na zem
     * @param state predchadzajuci stav bloku
     * @param worldIn instancia sveta
     * @param pos pozicia
     * @param newState novy stav bloku
     * @param isMoving nevyuzity argument
     */
    @Override
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if(state.getBlock() != newState.getBlock()){
            TileEntity tileEntity = worldIn.getTileEntity(pos);
            if(tileEntity instanceof WalletBlockTileEntity){
                WalletBlockTileEntity walletBlockTileEntity = (WalletBlockTileEntity) tileEntity;
                walletBlockTileEntity.dropAllContents(worldIn, pos);
            }
        }
        super.onReplaced(state, worldIn, pos, newState, isMoving);
    }


    /**
     * Definuje, aby sa tento blok vzdy vykreslil ako model
     * @param state nevyuzity argument
     * @return vzdy BlockRenderType.MODEL
     */
    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }
}
