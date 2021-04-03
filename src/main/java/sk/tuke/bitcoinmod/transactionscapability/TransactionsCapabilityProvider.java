package sk.tuke.bitcoinmod.transactionscapability;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Capability provider pre TransactionsCapability
 */
public class TransactionsCapabilityProvider implements ICapabilitySerializable<INBT> {
    @CapabilityInject(TransactionsCapability.class)
    public static Capability<TransactionsCapability> CAPABILITY_TRANSACTIONS = null;

    private TransactionsCapability instance = new TransactionsCapability();

    /**
     * Metoda sluzi pre ziskanie instancie TransactionsCapability
     * @param cap typ ziadanej capability
     * @param side nevyuzity argument
     * @param <T> TransactionsCapability
     * @return instancia TransactionsCapability alebo LazyOptional.empty()
     */
    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if(cap == CAPABILITY_TRANSACTIONS) {
            return (LazyOptional<T>) LazyOptional.of(() -> instance);
        }
        return LazyOptional.empty();
    }

    /**
     * Metoda sluzi pre serializaciu TransactionsCapability pomocou NBT struktury pre perzistentne uchovavanie
     * @return NBT struktura so serializovanou instanciou TransactionsCapability
     */
    @Override
    public INBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.put("Transactions", CAPABILITY_TRANSACTIONS.writeNBT(instance, null));
        return nbt;
    }

    /**
     * Sluzi pre deserializaciu TransactionsCapability z NBT ktore bolo nacitane z disku
     * @param nbt NBT so serializovanou TransactionsCapability
     */
    @Override
    public void deserializeNBT(INBT nbt) {
        CompoundNBT compoundNBT = (CompoundNBT) nbt;
        CAPABILITY_TRANSACTIONS.readNBT(instance, null, compoundNBT.getCompound("Transactions"));
    }
}
