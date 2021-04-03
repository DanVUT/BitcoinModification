package sk.tuke.bitcoinmod.blockchainblock;

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
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import sk.tuke.bitcoinmod.EntryPoint;


/**
 * Trieda implementujuca chovanie Blockchain Blocku
 */
public class BlockchainBlock extends Block {
    /**
     * Bezny konstruktor. Nastavuje tvrdost bloku aby sa nedal rozbit jednou ranou a RegistryName na "blockchain_block"
     */
    public BlockchainBlock() {
        super(Block.Properties.create(Material.IRON).hardnessAndResistance(1.15f));
        this.setRegistryName(EntryPoint.MODID, "blockchain_block");
    }

    /**
     * Metoda aktivovania bloku (ked ho hrac pouzije v hre). Na serverovej strane sa nic nestane. Na strane klienta sa zobrazi Blockchain GUI
     * @param state nepouzity argument
     * @param worldIn instancia World ktora sa pouziva na zistenie kontextu na ktorom sa spusta metoda
     * @param pos nepouzity argument
     * @param player nepouzity argument
     * @param handIn nepouzity argument
     * @param hit nepouzity argument
     * @return vysledok akcie. V tomto pripade vracia vzdy SUCCESS
     */
    @Override
    @OnlyIn(Dist.CLIENT)
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if(!worldIn.isRemote){
            return ActionResultType.SUCCESS;
        }

        Minecraft.getInstance().displayGuiScreen(new BlockchainBlockScreen(new StringTextComponent("Blockchain")));
        return ActionResultType.SUCCESS;
    }
}
