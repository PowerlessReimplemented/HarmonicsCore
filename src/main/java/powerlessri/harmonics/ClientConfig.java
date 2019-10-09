package powerlessri.harmonics;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.*;

public final class ClientConfig {

    private ClientConfig() {
    }

    public static final ClientCategory CLIENT;

    public static final class ClientCategory {

        public final BooleanValue inspectionsHighlighting;
        public final IntValue scrollSpeed;
        public final IntValue dialogMessageMaxWidth;

        private ClientCategory(Builder builder) {
            builder.comment("General client config options").push("client");

            inspectionsHighlighting = builder
                    .comment("Enables box highlight when mouse over widgets")
                    .translation("config.harmonics.client.inspectionsHighlighting")
                    .define("inspectionsHighlighting", false);
            scrollSpeed = builder
                    .comment("How long one move wheel movement for scrolling lists")
                    .translation("config.harmonics.client.scrollSpeed")
                    .defineInRange("scrollSpeed", 20, 1, 256);
            dialogMessageMaxWidth = builder
                    .comment("Maximum text width before splitting into a new line for dialog messages")
                    .translation("config.harmonics.client.dialogMsgMaxWidth")
                    .defineInRange("dialogMessageMaxWidth", 160, 0, Integer.MAX_VALUE);

            builder.pop();
        }
    }

    static final ForgeConfigSpec CLIENT_SPEC;

    static {
        Builder builder = new Builder();
        CLIENT = new ClientCategory(builder);
        CLIENT_SPEC = builder.build();
    }

}
