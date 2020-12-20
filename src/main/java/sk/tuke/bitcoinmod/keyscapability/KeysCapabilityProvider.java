package sk.tuke.bitcoinmod.keyscapability;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class KeysCapabilityProvider implements ICapabilitySerializable<INBT> {
    @CapabilityInject(KeysCapability.class)
    public static Capability<KeysCapability> CAPABILITY_KEYS = null;
    private static final Direction NO_SPECIFIC_SIDE = null;

    private KeysCapability instance = new KeysCapability();
    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if(cap == CAPABILITY_KEYS) {
            return (LazyOptional<T>) LazyOptional.of(() -> instance);
        }
        return LazyOptional.empty();
    }

    @Override
    public INBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.put("GeneratedKeys", CAPABILITY_KEYS.writeNBT(instance, NO_SPECIFIC_SIDE));
        return nbt;
    }

    @Override
    public void deserializeNBT(INBT nbt) {
        CompoundNBT compoundNBT = (CompoundNBT) nbt;
        CAPABILITY_KEYS.readNBT(instance, NO_SPECIFIC_SIDE, compoundNBT.get("GeneratedKeys"));
    }
}
