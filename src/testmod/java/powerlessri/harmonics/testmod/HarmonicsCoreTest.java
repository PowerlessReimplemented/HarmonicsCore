package powerlessri.harmonics.testmod;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.registry.CommandRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import powerlessri.harmonics.gui.screen.WidgetScreen;
import powerlessri.harmonics.testmod.gui.*;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class HarmonicsCoreTest implements ModInitializer {

    public static final String MODID = "hctest";
    public static HarmonicsCoreTest instance;

    public static void openGUI(String id) {
        MinecraftClient.getInstance().openScreen(instance.guiTests.get(id).get());
    }

    private Map<String, Supplier<WidgetScreen>> guiTests = new HashMap<>();

    public HarmonicsCoreTest() {
        instance = this;
    }

    @Override
    public void onInitialize() {
        guiTests.put("context_menu", ContextMenuTest::new);
        guiTests.put("background", BackgroundTest::new);
        guiTests.put("dialog", DialogTest::new);
        guiTests.put("searchable_list", SearchableListTest::new);
        guiTests.put("radio_button", RadioButtonTest::new);
        guiTests.put("text_button", TextButtonTest::new);
        guiTests.put("dockable_window", DockableWindowTest::new);
        guiTests.put("switch&checkbox", Switch8CheckboxTest::new);
        guiTests.put("scrolling_list", ScrollingListTest::new);
        guiTests.put("slider", SliderTest::new);
        guiTests.put("dropdown", DropdownTest::new);

        CommandRegistry.INSTANCE.register(false, dispatcher -> {
            dispatcher.register(CommandManager.literal(MODID)
                    .then(guiTesting()));
        });
    }

    private LiteralArgumentBuilder<ServerCommandSource> guiTesting() {
        LiteralArgumentBuilder<ServerCommandSource> cmd = CommandManager.literal("gui");
        for (Map.Entry<String, Supplier<WidgetScreen>> entry : guiTests.entrySet()) {
            cmd.then(CommandManager
                    .literal(entry.getKey())
                    .executes(context -> {
                        ServerPlayerEntity player = context.getSource().getPlayer();
                        player.networkHandler.sendPacket(new PacketOpenGUI(entry.getKey()));
                        return 0;
                    }));
        }
        return cmd;
    }
}
