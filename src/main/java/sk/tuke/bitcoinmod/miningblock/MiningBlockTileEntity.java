package sk.tuke.bitcoinmod.miningblock;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * Trieda implementujuca Tile Entity Mining Blocku
 */
public class MiningBlockTileEntity extends TileEntity implements INamedContainerProvider {
    public static final int NUMBER_OF_SLOTS = 2;
    MiningBlockContent content;

    /**
     * V konstruktore sa vytvara instancia triedy MiningBlockContent
     */
    public MiningBlockTileEntity() {
        super(MiningBlockCommonEvents.miningBlockTileEntityType);
        this.content = MiningBlockContent.createForTileEntity(NUMBER_OF_SLOTS, this::canPlayerAccessInventory, this::markDirty);
    }

    /**
     * Podmienka pre interakciu je ze hrac musi byt v rozsahu 8x8 blokov od Mining Blocku
     * @param player instancia hraca interagujuceho s Blokom
     * @return bool hodnota ci hrac moze pouzit blok
     */
    public boolean canPlayerAccessInventory(PlayerEntity player) {
        if (this.world.getTileEntity(this.pos) != this) return false;
        final double X_CENTRE_OFFSET = 0.5;
        final double Y_CENTRE_OFFSET = 0.5;
        final double Z_CENTRE_OFFSET = 0.5;
        final double MAXIMUM_DISTANCE_SQ = 8.0 * 8.0;
        return player.getDistanceSq(pos.getX() + X_CENTRE_OFFSET, pos.getY() + Y_CENTRE_OFFSET, pos.getZ() + Z_CENTRE_OFFSET) < MAXIMUM_DISTANCE_SQ;
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent("container.bitcoinmod.mining_block_container");
    }

    /**
     * Vytvori novu instanciu MiningBlockContaineru
     * @param windowID id okna
     * @param playerInventory hracov inventar
     * @param playerEntity instancia hraca
     * @return nova instancia MiningBlock Containeru
     */
    @Nullable
    @Override
    public Container createMenu(int windowID, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        return MiningBlockContainer.createForServer(windowID, playerInventory, content);
    }


    /**
     * precita stav TileEntity z NBT struktury
     * @param compound Stav TileEntity serializovany do NBT struktury
     */
    @Override
    public void read(CompoundNBT compound) {
        super.read(compound);
        this.content.deserializeNBT(compound.getCompound("MiningBlockContent"));
    }

    /**
     * Metoda zapise stav Tile Entity do NBT struktury
     * @param compound NBT struktura do ktorej sa zapise stav Tile Entity
     * @return NBT struktura so stavom TileEntity
     */
    @Override
    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);
        compound.put("MiningBlockContent", this.content.serializeNBT());
        return compound;
    }

    @Override
    public CompoundNBT getUpdateTag()
    {
        CompoundNBT nbtTagCompound = new CompoundNBT();
        write(nbtTagCompound);
        return nbtTagCompound;
    }

    @Override
    public void handleUpdateTag(CompoundNBT tag)
    {
        this.read(tag);
    }

    @Override
    @Nullable
    public SUpdateTileEntityPacket getUpdatePacket()
    {
        return new SUpdateTileEntityPacket(this.pos, -1, getUpdateTag());
    }
    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        handleUpdateTag(pkt.getNbtCompound());
    }

    /**
     * Metoda vyhodi obsah MiningBlocku na zem
     * @param world instancia triedy World
     * @param blockPos pozicia bloku
     */
    public void dropAllContents(World world, BlockPos blockPos){
        InventoryHelper.dropInventoryItems(world, blockPos, this.content);
    }

    /**
     * @return vrati Wallet Block v lavom slote Mining Blocku
     */
    public ItemStack getWalletItemStack(){
        return this.content.getStackInSlot(0);
    }

    /**
     * @return vrati pocet diamantov v pravom slote Mining Blocku
     */
    public int getDiamondsCount(){
        ItemStack stack = this.content.getStackInSlot(1);
        if(stack == ItemStack.EMPTY){
            return 0;
        } else {
            return stack.getCount();
        }

    }
}
