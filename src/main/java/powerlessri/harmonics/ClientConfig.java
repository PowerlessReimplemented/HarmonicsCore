package powerlessri.harmonics;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.Builder;
import net.minecraftforge.fml.config.ModConfig;

public final class ClientConfig {

    private ClientConfig() {
    }

    ///////////////////////////////////////////////////////////////////////////
    // General
    ///////////////////////////////////////////////////////////////////////////

    public static final GeneralCategory GENERAL;

    public static final class GeneralCategory {

        private GeneralCategory(Builder builder) {
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // GUI Inspections
    ///////////////////////////////////////////////////////////////////////////

    public static final InspectionsCategory INSPECTIONS;

    public static final class InspectionsCategory {

        public final BooleanValue boxHighlighting;

        private InspectionsCategory(Builder builder) {
            builder.comment("GUI Inspections Config").push("inspections");

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

    static final ForgeConfigSpec CONFIG_SPEC;
    static {
        Builder builder = new Builder();
        GENERAL = new GeneralCategory(builder);
        INSPECTIONS = new InspectionsCategory(builder);
        CONFIG_SPEC = builder.build();
    }

    static void onLoad(ModConfig.Loading event) {
        HarmonicsCore.logger.debug("Loaded {} client config file {}", HarmonicsCore.MODID, event.getConfig().getFileName());
    }
}
