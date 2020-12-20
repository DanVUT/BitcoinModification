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

public class TransactionsCapabilityProvider implements ICapabilitySerializable<INBT> {
    @CapabilityInject(TransactionsCapability.class)
    Capability<TransactionsCapability> CAPABILITY_TRANSACTIONS = null;

    private TransactionsCapability instance = new TransactionsCapability();

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if(cap == CAPABILITY_TRANSACTIONS) {
            return (LazyOptional<T>) LazyOptional.of(() -> instance);
        }
        return LazyOptional.empty();
    }

    @Override
    public INBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.put("Transactions", CAPABILITY_TRANSACTIONS.writeNBT(instance, null));
        return nbt;
    }

    @Override
    public void deserializeNBT(INBT nbt) {
        CompoundNBT compoundNBT = (CompoundNBT) nbt;
        CAPABILITY_TRANSACTIONS.readNBT(instance, null, compoundNBT);
    }
}
