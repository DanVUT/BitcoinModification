package sk.tuke.bitcoinmod.keyscapability;

import net.minecraft.nbt.INBT;
import net.minecraft.nbt.LongArrayNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.Tuple;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;

public class KeysCapability {
    public HashSet<Long> allPrivateKeys;

    public KeysCapability(){
        allPrivateKeys = new HashSet<>();
    }

    public Tuple<Long,Long> generateKeyPair(){
        long privateKey;
        do {
            privateKey = (long)(Integer.MAX_VALUE + (Math.random() * Integer.MAX_VALUE));
        } while(allPrivateKeys.contains(privateKey));
        long bitcoinAddress = generateBitcoinAddress(privateKey);
        return new Tuple<>(privateKey, bitcoinAddress);
    }

    private long generateBitcoinAddress(long privateKey){
        return privateKey >> 2;
    }

    public boolean validateKeyPair(long privateKey, long bitcoinAddress){
        return generateBitcoinAddress(privateKey) == bitcoinAddress;
    }

    public static class KeysCapabilityStorage implements Capability.IStorage<KeysCapability>{

        @Nullable
        @Override
        public INBT writeNBT(Capability<KeysCapability> capability, KeysCapability instance, Direction side) {
            Long[] allPrivateKeys = new Long[0];
            instance.allPrivateKeys.toArray(allPrivateKeys);
            return new LongArrayNBT(Arrays.asList(allPrivateKeys));
        }

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
