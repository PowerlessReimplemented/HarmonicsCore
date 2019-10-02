package powerlessri.harmonics;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.Builder;
import net.minecraftforge.fml.config.ModConfig;

public final class Config {

    private Config() {
    }

    ///////////////////////////////////////////////////////////////////////////
    // General
    ///////////////////////////////////////////////////////////////////////////

    public static final CommonCategory COMMON;

    public static final class CommonCategory {

        private CommonCategory(Builder builder) {
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // GUI Inspections
    ///////////////////////////////////////////////////////////////////////////

    public static final ClientCategory CLIENT;

    public static final class ClientCategory {

        public final BooleanValue boxHighlighting;

        private ClientCategory(Builder builder) {
            builder.comment("GUI Inspections Config").push("client");

            boxHighlighting = builder
                    .comment("Enables box highlight when mouse over widgets")
                    .translation("config.harmonics.client.inspections.highlighting")
                    .define("BoxHighlighting", false);

            builder.pop();
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // Construction
    ///////////////////////////////////////////////////////////////////////////

    static final ForgeConfigSpec CLIENT_SPEC;
    static final ForgeConfigSpec COMMON_SPEC;

    static {
        Builder builder = new Builder();
        COMMON = new CommonCategory(builder);
        COMMON_SPEC = builder.build();
    }

    static {
        Builder builder = new Builder();
        CLIENT = new ClientCategory(builder);
        CLIENT_SPEC = builder.build();
    }

    static void onLoad(ModConfig.Loading event) {
        HarmonicsCore.logger.debug("Loaded {} config file {}", HarmonicsCore.MODID, event.getConfig().getFileName());
    }
}
