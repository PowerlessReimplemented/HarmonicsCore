package powerlessri.harmonics;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.*;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import powerlessri.harmonics.network.NetworkHandler;

@Mod(HarmonicsCore.MODID)
public class HarmonicsCore {

    public static final String MODID = "harmonics";

    public static HarmonicsCore instance;

    public static Logger logger = LogManager.getLogger(MODID);

    public HarmonicsCore() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ClientConfig.CLIENT_SPEC);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.COMMON_SPEC);

        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        eventBus.addListener(this::setup);
        eventBus.addListener(this::loadComplete);
        eventBus.addListener(Config::onLoad);

        DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> eventBus.addListener(this::clientSetup));
    }

    private void setup(final FMLCommonSetupEvent event) {
        HarmonicsCore mod = (HarmonicsCore) ModLoadingContext.get().getActiveContainer().getMod();
        Validate.isTrue(mod == this);
        instance = this;

        NetworkHandler.register();
    }

    private void clientSetup(final FMLClientSetupEvent event) {
    }

    private void loadComplete(final FMLLoadCompleteEvent event) {
    }
}
