package powerlessri.harmonics.testmod;

import com.google.common.base.Preconditions;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DeferredWorkQueue;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.*;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import powerlessri.harmonics.gui.screen.WidgetScreen;
import powerlessri.harmonics.gui.widget.RadioButton;
import powerlessri.harmonics.testmod.gui.*;

import java.util.HashMap;
import java.util.Map;
import java.util.function.*;

@Mod(HarmonicsCoreTest.MODID)
public class HarmonicsCoreTest {

    public static final String MODID = "hctest";

    public static HarmonicsCoreTest instance;

    public HarmonicsCoreTest() {
        instance = this;

        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(this::setup);

        MinecraftForge.EVENT_BUS.addListener(this::serverStarting);
    }

    private Map<String, Supplier<WidgetScreen>> guiTests = new HashMap<>();

    public static void openGUI(String id) {
        Minecraft.getInstance().displayGuiScreen(instance.guiTests.get(id).get());
    }

    private void setup(FMLCommonSetupEvent event) {
        guiTests.put("dialog", DialogTest::new);
        guiTests.put("background", BackgroundTest::new);
        guiTests.put("searchable_list", SearchableListTest::new);
        guiTests.put("radio_button", RadioButtonTest::new);

        DeferredWorkQueue.runLater(() -> {
            registerPackets();
        });
    }

    private void serverStarting(FMLServerStartingEvent event) {
        event.getCommandDispatcher().register(Commands.literal(MODID)
                .then(guiTesting()));
    }

    ///////////////////////////////////////////////////////////////////////////
    // Commands
    ///////////////////////////////////////////////////////////////////////////

    private LiteralArgumentBuilder<CommandSource> guiTesting() {
        LiteralArgumentBuilder<CommandSource> cmd = Commands.literal("gui");
        for (Map.Entry<String, Supplier<WidgetScreen>> entry : guiTests.entrySet()) {
            cmd.then(Commands
                    .literal(entry.getKey())
                    .executes(context -> {
                        sendTo(context.getSource().asPlayer(), new PacketOpenGUI(entry.getKey()));
                        return 0;
                    }));
        }
        return cmd;
    }

    ///////////////////////////////////////////////////////////////////////////
    // Packets
    ///////////////////////////////////////////////////////////////////////////

    public static final String PROTOCOL_VERSION = "0";
    public static final SimpleChannel CHANNEL = NetworkRegistry.ChannelBuilder
            .named(new ResourceLocation(MODID, "main_channel"))
            .clientAcceptedVersions(PROTOCOL_VERSION::equals)
            .serverAcceptedVersions(PROTOCOL_VERSION::equals)
            .networkProtocolVersion(() -> PROTOCOL_VERSION)
            .simpleChannel();

    public static void sendTo(ServerPlayerEntity player, Object msg) {
        if (!(player instanceof FakePlayer)) {
            CHANNEL.sendTo(msg, player.connection.netManager, NetworkDirection.PLAY_TO_CLIENT);
        }
    }

    public static void sendToServer(Object msg) {
        CHANNEL.sendToServer(msg);
    }

    private static void registerPackets() {
        registerMessage(PacketOpenGUI.class, PacketOpenGUI::encode, PacketOpenGUI::decode, PacketOpenGUI::handle);
    }

    private static int nextID = 0;

    private static <MSG> void registerMessage(Class<MSG> messageType, BiConsumer<MSG, PacketBuffer> encoder, Function<PacketBuffer, MSG> decoder, BiConsumer<MSG, Supplier<NetworkEvent.Context>> handler) {
        Preconditions.checkState(nextID < 0xFF, "Too many messages!");
        CHANNEL.registerMessage(nextID, messageType, encoder, decoder, handler);
        nextID++;
    }
}
