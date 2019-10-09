package powerlessri.harmonics.network;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import powerlessri.harmonics.session.server.ISessionHandler;
import powerlessri.harmonics.session.server.SessionManager;

import java.util.function.Supplier;

public class PacketMessage {

    public static void encode(PacketMessage msg, PacketBuffer buf) {
        buf.writeInt(msg.id);
        buf.writeInt(msg.messageType);
        buf.writeInt(msg.data.readableBytes());
        buf.writeBytes(msg.data);
    }

    public static PacketMessage decode(PacketBuffer buf) {
        int id = buf.readInt();
        int messageType = buf.readInt();
        int size = buf.readInt();
        PacketBuffer bytes = new PacketBuffer(buf.readBytes(size));
        return new PacketMessage(id, messageType, bytes);
    }

    public static void handle(PacketMessage msg, Supplier<NetworkEvent.Context> ctxSupplier) {
        NetworkEvent.Context ctx = ctxSupplier.get();
        ctx.enqueueWork(() -> {
            ISessionHandler handler = SessionManager.INSTANCE.getSession(msg.id);
            handler.getConnectionHandler().receiveData(msg.messageType, msg.data);
            ctx.setPacketHandled(true);
        });
    }

    private int id;
    private int messageType;
    private PacketBuffer data;

    public PacketMessage(int id, int messageType, PacketBuffer data) {
        this.id = id;
        this.messageType = messageType;
        this.data = data;
    }
}
