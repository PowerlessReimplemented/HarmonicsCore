package powerlessri.harmonics.integration;

import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

public final class IntegrationHandler {

    private IntegrationHandler() {
    }

    public static void setup(FMLCommonSetupEvent event) {
    }

    public @interface IntegratedMod {

        /**
         * Mod ID of the integrated mod. Loader will only load the class if the mod with given mod id is installed.
         */
        String value();
    }
}
