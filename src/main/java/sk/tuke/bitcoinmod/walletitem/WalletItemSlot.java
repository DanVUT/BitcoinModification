package sk.tuke.bitcoinmod.walletitem;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import sk.tuke.bitcoinmod.helpers.ScreenRefresher;

public class WalletItemSlot extends Slot {
    private boolean canPut;
    public WalletItemSlot(IInventory inventoryIn, int index, int xPosition, int yPosition, boolean canPut) {
        super(inventoryIn, index, xPosition, yPosition);
        this.canPut = canPut;
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        if(stack.getItem() == WalletItemCommonEvents.walletItem){
            return canPut;
        } else{
            return false;
        }
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void onSlotChanged() {
        super.onSlotChanged();
        ScreenRefresher.refreshScreen();
    }
}
