package sk.tuke.bitcoinmod.walletblock;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.PlayerInvWrapper;
import sk.tuke.bitcoinmod.walletitem.WalletItemSlot;

import javax.annotation.Nullable;

public class WalletBlockContainer extends Container {
    private WalletBlockContent walletBlockContent;
    private final int COLUMNS_COUNT = 9;
    private final int ROWS_COUNT = 3;
    private final int SLOT_WIDTH = 15;
    private final int SLOT_HEIGHT = 15;
    private final int SLOT_SPACING = 18;
    private final int FIRST_SLOT_X_POSITION = 8;
    private final int FIRST_SLOT_Y_POSITION = 119;
    private final int HOTBAR_FIRST_SLOT_X_POSITION = 8;
    private final int HOTBAR_FIRST_SLOT_Y_POSITION = 177;
    private final int GENERATED_ITEM_X_POSITION = 8;
    private final int GENERATED_ITEM_Y_POSITION = 11;
    private final int MANAGED_ITEM_X_POSITION = 8;
    private final int MANAGED_ITEM_Y_POSITION = 39;

    public static WalletBlockContainer createForClientSide(int windowID, PlayerInventory playerInventory, PacketBuffer extraData){
        WalletBlockContent content = WalletBlockContent.createForClientSideContainer(WalletBlockTileEntity.NUMBER_OF_SLOTS);
        return new WalletBlockContainer(windowID, playerInventory, content);
    }

    public static WalletBlockContainer createForServerSide(int windowID, PlayerInventory playerInventory, WalletBlockContent content){
        return new WalletBlockContainer(windowID, playerInventory, content);
    }

    protected WalletBlockContainer( int windowID, PlayerInventory playerInventory, WalletBlockContent walletBlockContent) {
        super(WalletBlockCommonEvents.walletBlockContainerType, windowID);
        this.walletBlockContent = walletBlockContent;
        PlayerInvWrapper inventoryWrapper = new PlayerInvWrapper(playerInventory);
        generateInventorySlots(inventoryWrapper);
        generateWalletSlots();
    }

    private void generateInventorySlots(PlayerInvWrapper inventoryWrapper){
        for(int i = 0; i < COLUMNS_COUNT; i++){
            int xpos = HOTBAR_FIRST_SLOT_X_POSITION + i * SLOT_SPACING;
            addSlot(new SlotItemHandler(inventoryWrapper, i, xpos, HOTBAR_FIRST_SLOT_Y_POSITION));
        }

        for(int row = 0; row < ROWS_COUNT; row++){
            for(int column = 0; column < COLUMNS_COUNT; column++){
                int xpos = FIRST_SLOT_X_POSITION + column * SLOT_SPACING;
                int ypos = FIRST_SLOT_Y_POSITION + row * SLOT_SPACING;
                int slotNumber = COLUMNS_COUNT + row * COLUMNS_COUNT + column;
                addSlot(new SlotItemHandler(inventoryWrapper, slotNumber, xpos, ypos));
            }
        }
    }

    private void generateWalletSlots(){
        addSlot(new WalletItemSlot(walletBlockContent, 0, GENERATED_ITEM_X_POSITION, GENERATED_ITEM_Y_POSITION, false));
        addSlot(new WalletItemSlot(walletBlockContent, 1, MANAGED_ITEM_X_POSITION, MANAGED_ITEM_Y_POSITION, true));
    }
    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return this.walletBlockContent.isUsableByPlayer(playerIn);
    }

    @Override
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
        return ItemStack.EMPTY;
    }

    public void setGeneratedWalletSlot(ItemStack stack){
        this.walletBlockContent.setInventorySlotContents(0, stack);
    }

    public ItemStack getManagedWalletSlot(){
        return this.walletBlockContent.getStackInSlot(1);
    }
}
