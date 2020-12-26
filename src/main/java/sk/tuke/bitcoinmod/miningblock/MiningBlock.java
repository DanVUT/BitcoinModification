package sk.tuke.bitcoinmod.miningblock;

import net.minecraft.block.Block;
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

public class MiningBlock extends ContainerBlock {
    public MiningBlock() {
        super(Properties.create(Material.IRON));
        this.setRegistryName(EntryPoint.MODID, "mining_block");
    }

    @Override
    public boolean hasTileEntity() {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(IBlockReader worldIn) {
        return new MiningBlockTileEntity();
    }

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

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

}
