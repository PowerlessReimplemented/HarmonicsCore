package powerlessri.harmonics.network;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import powerlessri.harmonics.session.server.ISessionHandler;
import powerlessri.harmonics.session.server.SessionManager;

import java.util.function.Supplier;

public class PacketMessage {

    public static void encode(PacketMessage msg, PacketBuffer buf) {
        buf.writeInt(msg.id);
        buf.writeInt(msg.bytes.readableBytes());
        buf.writeBytes(msg.bytes);
    }

    public static PacketMessage decode(PacketBuffer buf) {
        int id = buf.readInt();
        int size = buf.readInt();
        PacketBuffer bytes = new PacketBuffer(buf.readBytes(size));
        return new PacketMessage(id, bytes);
    }

    public static void handle(PacketMessage msg, Supplier<NetworkEvent.Context> ctxSupplier) {
        NetworkEvent.Context ctx = ctxSupplier.get();
        ctx.enqueueWork(() -> {
            ISessionHandler sessionHandler = SessionManager.INSTANCE.getSession(msg.id);
            ctx.setPacketHandled(true);
        });
    }

    private int id;
    private PacketBuffer bytes;

    public PacketMessage(int id, PacketBuffer bytes) {
        this.id = id;
        this.bytes = bytes;
    }
}
