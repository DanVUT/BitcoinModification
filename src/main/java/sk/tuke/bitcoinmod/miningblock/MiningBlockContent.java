package sk.tuke.bitcoinmod.miningblock;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.items.ItemStackHandler;

import java.util.function.Predicate;

/**
 * Trieda implementujuca obsah Mining Blocku
 */
public class MiningBlockContent implements IInventory {
    private ItemStackHandler content;
    private Predicate<PlayerEntity> canPlayerAccess;
    private Runnable markDirty;

    public static MiningBlockContent createForTileEntity(int slots, Predicate<PlayerEntity> canPlayerAccess, Runnable markDirty){
        return new MiningBlockContent(slots, canPlayerAccess, markDirty);
    }

    public static MiningBlockContent createForClient(int slots){
        return new MiningBlockContent(slots, x -> true, () -> {});
    }

    /**
     * @param slots pocet slotov v bloku (2)
     * @param canPlayerAccess lambda funkcia, ktora sluzi pre zistenie ci hrac moze interagovat s obsahom.
     * @param markDirty lambda funkcia, ktora sluzi k oznaceniu, ze sa s obsahom manipulovalo. Ako mark dirty sa pouziva funkcia mark dirty z Tile Entity
     */
    private MiningBlockContent(int slots, Predicate<PlayerEntity> canPlayerAccess, Runnable markDirty) {
        this.content = new ItemStackHandler(slots);
        this.canPlayerAccess = canPlayerAccess;
        this.markDirty = markDirty;
    }

    @Override
    public int getSizeInventory() {
        return content.getSlots();
    }

    @Override
    public boolean isEmpty() {
        for(int i = 0; i < content.getSlots(); i++){
            if(!content.getStackInSlot(i).isEmpty()){
                return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        return content.getStackInSlot(index);
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        return content.extractItem(index, count, false);
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        return content.extractItem(index, content.getSlotLimit(index), false);
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        content.setStackInSlot(index, stack);
    }

    @Override
    public void markDirty() {
        this.markDirty.run();
    }

    @Override
    public boolean isUsableByPlayer(PlayerEntity player) {
        return this.canPlayerAccess.test(player);
    }

    @Override
    public void clear() {
        for(int i = 0; i < this.content.getSlots(); i++){
            this.setInventorySlotContents(i, ItemStack.EMPTY);
        }
    }

    public CompoundNBT serializeNBT(){
        return this.content.serializeNBT();
    }

    public void deserializeNBT(CompoundNBT nbt){
        this.content.deserializeNBT(nbt);
    }

}
