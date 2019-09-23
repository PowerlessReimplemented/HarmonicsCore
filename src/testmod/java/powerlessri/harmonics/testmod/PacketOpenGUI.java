package powerlessri.harmonics.testmod;

import com.google.common.base.Preconditions;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.thread.EffectiveSide;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketOpenGUI {

    public static void encode(PacketOpenGUI msg, PacketBuffer buf) {
        buf.writeString(msg.id);
    }

    public static PacketOpenGUI decode(PacketBuffer buf) {
        return new PacketOpenGUI(buf.readString());
    }

    public static void handle(PacketOpenGUI msg, Supplier<NetworkEvent.Context> ctxSupplier) {
        NetworkEvent.Context ctx = ctxSupplier.get();
        ctx.enqueueWork(() -> {
            Preconditions.checkState(EffectiveSide.get() == LogicalSide.CLIENT);
            HarmonicsCoreTest.openGUI(msg.id);
            ctx.setPacketHandled(true);
        });
    }

    private String id;

    public PacketOpenGUI(String id) {
        this.id = id;
    }
}
