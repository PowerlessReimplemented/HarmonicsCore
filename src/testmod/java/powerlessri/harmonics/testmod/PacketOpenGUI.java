package powerlessri.harmonics.testmod;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.Packet;
import net.minecraft.util.PacketByteBuf;

import java.io.IOException;

public class PacketOpenGUI implements Packet<ClientPlayNetworkHandler> {

    private String id = "";

    public PacketOpenGUI() {
    }

    public PacketOpenGUI(String id) {
        this.id = id;
    }

    @Override
    public void read(PacketByteBuf buf) throws IOException {
        buf.writeString(id);
    }

    @Override
    public void write(PacketByteBuf buf) throws IOException {
        id = buf.readString();
    }

    @Override
    public void apply(ClientPlayNetworkHandler handler) {
        MinecraftClient.getInstance().method_18858(() -> HarmonicsCoreTest.openGUI(id));
    }
}
