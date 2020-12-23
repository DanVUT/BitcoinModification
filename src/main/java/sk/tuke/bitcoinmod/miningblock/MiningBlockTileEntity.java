package sk.tuke.bitcoinmod.miningblock;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nullable;

public class MiningBlockTileEntity extends TileEntity implements INamedContainerProvider {
    public static final int NUMBER_OF_SLOTS = 2;
    MiningBlockContent content;

    public MiningBlockTileEntity() {
        super(MiningBlockCommonEvents.miningBlockTileEntityType);
        this.content = MiningBlockContent.createForTileEntity(NUMBER_OF_SLOTS, this::canPlayerAccessInventory, this::markDirty);
    }

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

    @Nullable
    @Override
    public Container createMenu(int windowID, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        return MiningBlockContainer.createForServer(windowID, playerInventory, content);
    }


    @Override
    public void read(CompoundNBT compound) {
        super.read(compound);
        this.content.deserializeNBT(compound.getCompound("MiningBlockContent"));
    }

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

    public ItemStack getWalletItemStack(){
        return this.content.getStackInSlot(0);
    }

    public int getDiamondsCount(){
        ItemStack stack = this.content.getStackInSlot(1);
        if(stack == ItemStack.EMPTY){
            return 0;
        } else {
            return stack.getCount();
        }

    }
}
