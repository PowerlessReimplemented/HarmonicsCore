package powerlessri.harmonics;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.config.ModConfig;

public final class Config {

    private Config() {
    }

    public static final CommonCategory COMMON;

    public static final class CommonCategory {

        private CommonCategory(ForgeConfigSpec.Builder builder) {
            builder.comment("General config options").push("common");
            builder.pop();
        }
    }

    static final ForgeConfigSpec COMMON_SPEC;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        COMMON = new CommonCategory(builder);
        COMMON_SPEC = builder.build();
    }

    static void onLoad(ModConfig.Loading event) {
        HarmonicsCore.logger.debug("Loaded {} config file {}", HarmonicsCore.MODID, event.getConfig().getFileName());
    }
}
