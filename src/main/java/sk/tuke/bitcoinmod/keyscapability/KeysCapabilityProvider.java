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

/**
 * Capability Provider pre KeysCapability. Ulohou tejto triedy je poskytovat instanciu KeysCapability a perzistentne ukladat takuto instanciu
 */
public class KeysCapabilityProvider implements ICapabilitySerializable<INBT> {
    @CapabilityInject(KeysCapability.class)
    public static Capability<KeysCapability> CAPABILITY_KEYS = null;
    private static final Direction NO_SPECIFIC_SIDE = null;

    private KeysCapability instance = new KeysCapability();

    /**
     * Metoda skontroluje, ci sa od nej ziada KeysCapability. Ak ano, vrati instanciu.
     * @param cap typ Capability
     * @param side nevyuzity argument
     * @param <T> kontroluje sa, ci T je KeysCapability
     * @return Instancia KeysCapability alebo LazyOptional.empty()
     */
    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if(cap == CAPABILITY_KEYS) {
            return (LazyOptional<T>) LazyOptional.of(() -> instance);
        }
        return LazyOptional.empty();
    }

    /**
     * Sluzi pre serializaciu za ucelom perzistentneho ulozenia.
     * @return KeysCapability serializovana do NBT
     */
    @Override
    public INBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.put("GeneratedKeys", CAPABILITY_KEYS.writeNBT(instance, NO_SPECIFIC_SIDE));
        return nbt;
    }

    /**
     * Sluzi pre deserializovanie KeysCapability z NBT ktore bolo ulozene na disku
     * @param nbt NBT obsahujuce serializovanu KeysCapability
     */
    @Override
    public void deserializeNBT(INBT nbt) {
        CompoundNBT compoundNBT = (CompoundNBT) nbt;
        CAPABILITY_KEYS.readNBT(instance, NO_SPECIFIC_SIDE, compoundNBT.get("GeneratedKeys"));
    }
}
