package sk.tuke.bitcoinmod.walletitem;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import sk.tuke.bitcoinmod.helpers.ScreenRefresher;

/**
 * Implementacia specialneho Slotu, do ktoreho je mozne vlozit iba predmet typu Wallet Item
 */
public class WalletItemSlot extends Slot {
    private boolean canPut;

    /**
     * @param inventoryIn nepouzity argument
     * @param index nepouzity argument
     * @param xPosition nepouzity argument
     * @param yPosition nepouzity argument
     * @param canPut prepinac indikujuci ci je do daneho slotu mozne vlozit predmet alebo nie
     */
    public WalletItemSlot(IInventory inventoryIn, int index, int xPosition, int yPosition, boolean canPut) {
        super(inventoryIn, index, xPosition, yPosition);
        this.canPut = canPut;
    }

    /**
     * Metoda kontroluje vkladany predmet, ci je takyto predmet mozne vlozit do slotu. Tento slot umoznuje donho vlozit iba predmet typu Wallet Item
     * @param stack stack ktory sa kontroluje na to, ci ho je mozne vlozit do slotu
     * @return vysledok, ci je mozne predmet do slotu vlozit
     */
    @Override
    public boolean isItemValid(ItemStack stack) {
        if(stack.getItem() == WalletItemCommonEvents.walletItem){
            return canPut;
        } else{
            return false;
        }
    }

    /**
     * Udalost, ktora nastava v pripade, ze sa zmeni obsah slotu. V takom pripade sa zavola ScreenRefresher, aby sa prekreslil aktualny stav Bitcoinov v ramci okien Wallet Blocku alebo Mining Blocku
     */
    @Override
    @OnlyIn(Dist.CLIENT)
    public void onSlotChanged() {
        super.onSlotChanged();
        ScreenRefresher.refreshScreen();
    }
}
