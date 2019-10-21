package powerlessri.harmonics;

import net.fabricmc.api.ModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HarmonicsCore implements ModInitializer {

    public static final String MODID = "harmonics";

    public static HarmonicsCore instance = new HarmonicsCore();
    public static final Logger logger = LogManager.getLogger(MODID);

    public HarmonicsCore() {
        instance = this;
        // TODO fabric
//        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Config.CLIENT_SPEC);
//        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.COMMON_SPEC);
    }

    @Override
    public void onInitialize() {

    }
}
