package sk.tuke.bitcoinmod.keyscapability;

import net.minecraft.nbt.INBT;
import net.minecraft.nbt.LongArrayNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.Tuple;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.HashSet;

/**
 * Trieda implementujuca funkcionalitu "kryptografickych" klucov.
 */
public class KeysCapability {
    /**
     * Field obsahujuci vsetky vygenerovane privatne kluce. Sluzi k tomu, aby nenastavala kolizia klucov
     */
    public HashSet<Long> allPrivateKeys;

    public KeysCapability(){
        allPrivateKeys = new HashSet<>();
    }

    /**
     * Vygeneruje par klucov. Privatny kluc si ulozi ze uz bol vygenerovany aby nenastavala kolizia klucov
     * @return Tuple privatneho a verejneho kluca
     */
    public Tuple<Long,Long> generateKeyPair(){
        long privateKey;
        do {
            privateKey = (long)(Integer.MAX_VALUE + (Math.random() * (2 * (long)Integer.MAX_VALUE)));
        } while(allPrivateKeys.contains(privateKey));
        long bitcoinAddress = generateBitcoinAddress(privateKey);
        allPrivateKeys.add(privateKey);
        return new Tuple<>(privateKey, bitcoinAddress);
    }


    /**
     * Metoda vygeneruje verejny kluc z privatneho kluca
     * @param privateKey privatny kluc z ktoreho sa bude generovat verejny kluc (Bitcoin adresa)
     * @return vygenerovany verejny kluc (Bitcoin Adresa)
     */
    private long generateBitcoinAddress(long privateKey){
        return (long)(privateKey * 1.42);
    }


    /**
     * Skontroluje, ci sa privatny kluc hashuje do verejneho kluca
     * @param privateKey privatny kluc
     * @param bitcoinAddress verejny kluc
     * @return vysledok ci sa privatny kluc zahashuje do verejneho kluca
     */
    public boolean validateKeyPair(long privateKey, long bitcoinAddress){
        return generateBitcoinAddress(privateKey) == bitcoinAddress;
    }

    /**
     * Trieda implementujuca ukladanie instancie KeysCapability na disk
     */
    public static class KeysCapabilityStorage implements Capability.IStorage<KeysCapability>{

        /**
         *
         * @param capability  nepouzity argument
         * @param instance instancia KeysCapability, ktora sa ma serializovat
         * @param side nepouzity argument
         * @return NBT struktura ktora serializuje poskytnutu instanciu do NBT struktury
         */
        @Nullable
        @Override
        public INBT writeNBT(Capability<KeysCapability> capability, KeysCapability instance, Direction side) {
            Long[] allPrivateKeys = new Long[0];
            allPrivateKeys = instance.allPrivateKeys.toArray(allPrivateKeys);
            return new LongArrayNBT(Arrays.asList(allPrivateKeys));
        }

        /**
         * Metoda precita pole Long cisel z NBT struktury a vlozi ich do instancie KeysCapability poskytnutej ako argument
         * @param capability nepouzity argument
         * @param instance instancia, ktora sa naplni pomocou tejto metody
         * @param side nepouzity argument
         * @param nbt NBT struktura ktora sa cita
         */
        @Override
        public void readNBT(Capability<KeysCapability> capability, KeysCapability instance, Direction side, INBT nbt) {
            LongArrayNBT allPrivateKeysNbt = (LongArrayNBT) nbt;
            long[] loadedKeys = allPrivateKeysNbt.getAsLongArray();
            for(long key: loadedKeys){
                instance.allPrivateKeys.add(key);
            }
        }
    }
}
