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

/**
 * Trieda implementujuca predmet Wallet Item (V hre pomenovany Bitcoin Key Pair)
 */
public class WalletItem extends Item {
    private static final String PRIVATE_KEY_TAG = "PRIVATE_KEY";
    private static final String BITCOIN_ADDRESS_KEY_TAG = "BITCOIN_ADDRESS";

    /**
     * Konstruktor nastavi maximalne mnozstvo tohoto predmetu v jednom "stacku" na 1 a nastavi predmetu RegistryName na "wallet_item"
     */
    public WalletItem() {
        super(new Item.Properties().group(ItemGroup.MISC).maxStackSize(1));
        this.setRegistryName(EntryPoint.MODID, "wallet_item");
    }

    /**
     * Tato metoda vlozi Privatny kluc a Bitcoin Adresu do tooltipu. Tento tooltip sa ukaze pri ukazani mysou na predmet Bitcoin key pair v hre.
     * @param stack pouzity stack predmetu typu Wallet Item
     * @param worldIn nepouzity argument
     * @param tooltip instancia tooltipu ktora sa upravuje v ramci metody
     * @param flagIn nepouzity argument
     */
    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        Tuple<Long, Long> pair = getKeyPairFromWalletItemStack(stack);
        long privateKey = pair.getA();
        long bitcoinAddress = pair.getB();
        tooltip.add(new StringTextComponent("Private Key: " + Long.toHexString(privateKey)));
        tooltip.add(new StringTextComponent("Bitcoin Address: " + Long.toHexString(bitcoinAddress)));
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }

    /**
     * Vytvori stack predmetu Wallet Item do ktoreho vlozi privatny kluc a bitcoin adresu vo forme NBT
     * @param privateKey privatny kluc
     * @param bitcoinAddress bitcoin adresa
     * @return vrati novy stack typu Wallet Item
     */
    public static ItemStack createWalletItemStack(long privateKey, long bitcoinAddress){
        ItemStack newStack = new ItemStack(WalletItemCommonEvents.walletItem);
        CompoundNBT nbt = newStack.getOrCreateTag();
        nbt.putLong(PRIVATE_KEY_TAG, privateKey);
        nbt.putLong(BITCOIN_ADDRESS_KEY_TAG, bitcoinAddress);
        return newStack;
    }

    /**
     * Vrati par klucov z konkretneho stacku typu Wallet Item
     * @param stack stack typu Wallet Item z ktoreho chceme ziskat par klucov
     * @return par privatneho a verejneho kluca ako Tuple
     */
    public static Tuple<Long, Long> getKeyPairFromWalletItemStack(ItemStack stack){
        CompoundNBT nbt = stack.getOrCreateTag();
        long privateKey = nbt.getLong(PRIVATE_KEY_TAG);
        long bitcoinAddress = nbt.getLong(BITCOIN_ADDRESS_KEY_TAG);
        return new Tuple<>(privateKey, bitcoinAddress);
    }
}
