package sk.tuke.bitcoinmod.miningblock;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.PlayerInvWrapper;
import sk.tuke.bitcoinmod.walletitem.WalletItemSlot;

public class MiningBlockContainer extends Container {
    private MiningBlockContent content;
    private static final int COLUMNS_COUNT = 9;
    private static final int ROWS_COUNT = 3;
    private static final int SLOT_SPACING = 18;
    private static final int FIRST_SLOT_X_POSITION = 8;
    private static final int FIRST_SLOT_Y_POSITION = 84;
    private static final int HOTBAR_FIRST_SLOT_X_POSITION = 8;
    private static final int HOTBAR_FIRST_SLOT_Y_POSITION = 142;
    private static final int WALLET_ITEM_X_POSITION = 35;
    private static final int WALLET_ITEM_Y_POSITION = 32;
    private static final int DIAMONDS_X_POSITION = 125;
    private static final int DIAMONDS_Y_POSITION = 32;

    public static MiningBlockContainer createForServer(int windowID, PlayerInventory playerInventory, MiningBlockContent miningBlockContent){
        return new MiningBlockContainer(windowID, playerInventory, miningBlockContent);
    }

    public static MiningBlockContainer createForClient(int windowID, PlayerInventory playerInventory, PacketBuffer extraData){
        MiningBlockContent content = MiningBlockContent.createForClient(MiningBlockTileEntity.NUMBER_OF_SLOTS);
        return new MiningBlockContainer(windowID, playerInventory, content);
    }

    private MiningBlockContainer(int windowID, PlayerInventory playerInventory, MiningBlockContent miningBlockContent){
        super(MiningBlockCommonEvents.miningBlockContainerType, windowID);
        PlayerInvWrapper inventoryWrapper = new PlayerInvWrapper(playerInventory);
        this.content = miningBlockContent;
        generateInventorySlots(inventoryWrapper);
        generateContentSlots();
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

    private void generateContentSlots(){
        addSlot(new WalletItemSlot(this.content, 0, WALLET_ITEM_X_POSITION, WALLET_ITEM_Y_POSITION, true));
        Slot diamondSlot = new Slot(this.content, 1, DIAMONDS_X_POSITION, DIAMONDS_Y_POSITION){
            @Override
            public boolean isItemValid(ItemStack stack) {
                return stack.getItem() == Items.DIAMOND;
            }
        };
        addSlot(diamondSlot);
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return this.content.isUsableByPlayer(playerIn);
    }

    @Override
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
        return ItemStack.EMPTY;
    }

    public ItemStack getWalletItemStack(){
        return this.content.getStackInSlot(0);
    }

    public ItemStack getDiamondsStack(){
        return this.content.getStackInSlot(1);
    }
}
