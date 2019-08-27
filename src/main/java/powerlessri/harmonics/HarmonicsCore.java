package powerlessri.harmonics;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.*;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.Logger;
import powerlessri.harmonics.gui.debug.Inspections;

@Mod(HarmonicsCore.MODID)
public class HarmonicsCore {

    public static final String MODID = "harmonicscore";
    public static final String NAME = "Harmonics Core";
    public static final String VERSION = "1.0.0";

    public static HarmonicsCore instance;

    public static Logger logger;

    public HarmonicsCore() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        eventBus.addListener(this::setup);
        eventBus.addListener(this::loadComplete);

        MinecraftForge.EVENT_BUS.addListener(this::serverStarting);

        DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> {
            eventBus.addListener(this::clientSetup);
        });
    }

    private void setup(final FMLCommonSetupEvent event) {
        HarmonicsCore mod = (HarmonicsCore) ModLoadingContext.get().getActiveContainer().getMod();
        Validate.isTrue(mod == this);
        instance = this;
    }

    private void clientSetup(final FMLClientSetupEvent event) {
    }

    private void loadComplete(final FMLLoadCompleteEvent event) {
    }

    private void serverStarting(final FMLServerStartingEvent event) {
        event.getCommandDispatcher().register(Commands.literal(MODID)
                .then(settingsCommand()));
    }

    ///////////////////////////////////////////////////////////////////////////
    // Commands
    ///////////////////////////////////////////////////////////////////////////

    private static LiteralArgumentBuilder<CommandSource> settingsCommand() {
        return Commands.literal("settings")
                .then(inspectionBoxHighlighting());
    }

    private static LiteralArgumentBuilder<CommandSource> inspectionBoxHighlighting() {
        return Commands
                .literal("InspectionBoxHighlighting")
                // Query setting
                .executes(context -> {
                    context.getSource().sendFeedback(new StringTextComponent("Entry InspectionBoxHighlighting is currently set to: " + Inspections.enabled), true);
                    return 0;
                })
                .then(Commands
                        .argument("value", BoolArgumentType.bool())
                        // Set setting
                        .executes(context -> {
                            Inspections.enabled = BoolArgumentType.getBool(context, "value");
                            context.getSource().sendFeedback(new StringTextComponent("Entry InspectionBoxHighlighting is now set to " + Inspections.enabled), true);
                            return 0;
                        }));
    }
}
