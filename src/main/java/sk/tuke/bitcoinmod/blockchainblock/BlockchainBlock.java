package sk.tuke.bitcoinmod.blockchainblock;

import com.sun.org.apache.xpath.internal.operations.String;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import sk.tuke.bitcoinmod.EntryPoint;

public class BlockchainBlock extends Block {
    public BlockchainBlock() {
        super(Block.Properties.create(Material.IRON));
        this.setRegistryName(EntryPoint.MODID, "blockchain_block");
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if(!worldIn.isRemote){
            return ActionResultType.SUCCESS;
        }

        Minecraft.getInstance().displayGuiScreen(new BlockchainBlockScreen(new StringTextComponent("Blockchain")));
        return ActionResultType.SUCCESS;
    }
}
