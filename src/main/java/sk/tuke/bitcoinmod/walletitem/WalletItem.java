package sk.tuke.bitcoinmod.walletitem;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Tuple;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import sk.tuke.bitcoinmod.EntryPoint;

import javax.annotation.Nullable;
import java.util.List;

public class WalletItem extends Item {
    private static final String PRIVATE_KEY_TAG = "PRIVATE_KEY";
    private static final String BITCOIN_ADDRESS_KEY_TAG = "BITCOIN_ADDRESS";
    public WalletItem() {
        super(new Item.Properties().group(ItemGroup.MISC).maxStackSize(1));
        this.setRegistryName(EntryPoint.MODID, "wallet_item");
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        Tuple<Long, Long> pair = getKeyPairFromWalletItemStack(stack);
        long privateKey = pair.getA();
        long bitcoinAddress = pair.getB();
        tooltip.add(new StringTextComponent("Private Key: " + Long.toHexString(privateKey)));
        tooltip.add(new StringTextComponent("Bitcoin Address: " + Long.toHexString(bitcoinAddress)));
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }

    public static ItemStack createWalletItemStack(long privateKey, long bitcoinAddress){
        ItemStack newStack = new ItemStack(WalletItemCommonEvents.walletItem);
        CompoundNBT nbt = newStack.getOrCreateTag();
        nbt.putLong(PRIVATE_KEY_TAG, privateKey);
        nbt.putLong(BITCOIN_ADDRESS_KEY_TAG, bitcoinAddress);
        return newStack;
    }

    public static Tuple<Long, Long> getKeyPairFromWalletItemStack(ItemStack stack){
        CompoundNBT nbt = stack.getOrCreateTag();
        long privateKey = nbt.getLong(PRIVATE_KEY_TAG);
        long bitcoinAddress = nbt.getLong(BITCOIN_ADDRESS_KEY_TAG);
        return new Tuple<>(privateKey, bitcoinAddress);
    }
}
