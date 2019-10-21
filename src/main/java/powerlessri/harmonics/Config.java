package powerlessri.harmonics;

import java.util.function.Supplier;

// TODO fabric
public final class Config {

    private Config() {
    }

    ///////////////////////////////////////////////////////////////////////////
    // Common config
    ///////////////////////////////////////////////////////////////////////////

//    public static final CommonCategory COMMON;
//
//    public static final class CommonCategory {
//
//        private CommonCategory(ForgeConfigSpec.Builder builder) {
//            builder.comment("General config options").push("common");
//            builder.pop();
//        }
//    }
//
//    static final ForgeConfigSpec COMMON_SPEC;
//
//    static {
//        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
//        COMMON = new CommonCategory(builder);
//        COMMON_SPEC = builder.build();
//    }

    public static final CommonCategory COMMON = new CommonCategory();

    public static final class CommonCategory {
    }

    ///////////////////////////////////////////////////////////////////////////
    // Client config
    ///////////////////////////////////////////////////////////////////////////

//    public static final ClientCategory CLIENT;
//
//    public static final class ClientCategory {
//
//        public final ForgeConfigSpec.BooleanValue inspectionsHighlighting;
//        public final ForgeConfigSpec.IntValue scrollSpeed;
//        public final ForgeConfigSpec.IntValue dialogMessageMaxWidth;
//        public final ForgeConfigSpec.IntValue minBorderDistance;
//
//        private ClientCategory(ForgeConfigSpec.Builder builder) {
//            builder.comment("General client config options").push("client");
//
//            inspectionsHighlighting = builder
//                    .comment("Enables box highlight when mouse over widgets")
//                    .translation("config.harmonics.client.inspectionsHighlighting")
//                    .define("InspectionsHighlighting", false);
//            scrollSpeed = builder
//                    .comment("How long one move wheel movement for scrolling lists")
//                    .translation("config.harmonics.client.scrollSpeed")
//                    .defineInRange("ScrollSpeed", 20, 1, 256);
//            dialogMessageMaxWidth = builder
//                    .comment("Maximum text width before splitting into a new line for dialog messages")
//                    .translation("config.harmonics.client.dialogMsgMaxWidth")
//                    .defineInRange("DialogMessageMaxWidth", 160, 0, Integer.MAX_VALUE);
//            minBorderDistance = builder
//                    .comment("Minimum distance from the border of a context menu to the border of the screen",
//                            "If the context menu is created too close to the screen border, it will shift towards the center")
//                    .translation("config.harmonics.client.ctxMenuMinBorderDistance")
//                    .defineInRange("minBorderDistance", 4, 0, Integer.MAX_VALUE);
//
//            builder.pop();
//        }
//    }
//
//    static final ForgeConfigSpec CLIENT_SPEC;
//
//    static {
//        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
//        CLIENT = new ClientCategory(builder);
//        CLIENT_SPEC = builder.build();
//    }
//
//    static void onLoad(ModConfig.Loading event) {
//        HarmonicsCore.logger.debug("Loaded {} config file {}", HarmonicsCore.MODID, event.getConfig().getFileName());
//    }

    public static final ClientCategory CLIENT = new ClientCategory();

    public static final class ClientCategory {

        public final Supplier<Boolean> inspectionsHighlighting = () -> false;
        public final Supplier<Integer> scrollSpeed = () -> 20;
        public final Supplier<Integer> dialogMessageMaxWidth = () -> 160;
        public final Supplier<Integer> minBorderDistance = () -> 4;
    }
}
