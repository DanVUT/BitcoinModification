package sk.tuke.bitcoinmod.walletblock;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.items.ItemStackHandler;
import sk.tuke.bitcoinmod.helpers.ScreenRefresher;
import sk.tuke.bitcoinmod.interfaces.IRefreshable;

import java.util.function.Predicate;

public class WalletBlockContent implements IInventory {
    private final ItemStackHandler content;
    private Predicate<PlayerEntity> canPlayerAccess;
    private Runnable markDirty;

    private WalletBlockContent(int size){
        this.content = new ItemStackHandler(size);
        this.canPlayerAccess = x -> true;
        this.markDirty = () -> {};
    }
    private WalletBlockContent(int size, Predicate<PlayerEntity> canPlayerAccess, Runnable markDirty) {
        this(size);
        this.canPlayerAccess = canPlayerAccess;
        this.markDirty = markDirty;
    }
    public static WalletBlockContent createForTileEntity(int size, Predicate<PlayerEntity> canPlayerAccess, Runnable markDirty){
        return new WalletBlockContent(size, canPlayerAccess, markDirty);
    }
    public static WalletBlockContent createForClientSideContainer(int size){
        return new WalletBlockContent(size);
    }

    @Override
    public int getSizeInventory() {
        return this.content.getSlots();
    }

    @Override
    public boolean isEmpty() {
        for(int i = 0; i < this.content.getSlots(); i++){
            if(!this.content.getStackInSlot(i).isEmpty()){
                return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        return this.content.getStackInSlot(index);
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        return this.content.extractItem(index, count, false);
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        int maxItemStackSize = this.content.getSlotLimit(index);
        return this.content.extractItem(index, maxItemStackSize, false);
    }


    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        this.content.setStackInSlot(index, stack);
    }

    @Override
    public void clear() {
        for (int i = 0; i < this.content.getSlots(); ++i) {
            this.content.setStackInSlot(i, ItemStack.EMPTY);
        }
    }

    @Override
    public void markDirty() {
        markDirty.run();
    }

    @Override
    public boolean isUsableByPlayer(PlayerEntity player) {
        return canPlayerAccess.test(player);
    }

    public CompoundNBT serializeNBT(){
        return this.content.serializeNBT();
    }

    public void deserializeNBT(CompoundNBT nbt){
        this.content.deserializeNBT(nbt);
    }
}
