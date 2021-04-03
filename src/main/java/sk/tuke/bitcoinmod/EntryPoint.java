package sk.tuke.bitcoinmod;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Vstupny bod pre modifikaciu. Tato trieda neobsahuje ziadnu implementaciu
 */
@Mod(EntryPoint.MODID)
public class EntryPoint {
    public static final String MODID = "bitcoinmod";
    public static final Logger LOGGER = LogManager.getLogger();

}
