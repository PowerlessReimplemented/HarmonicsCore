package powerlessri.harmonics.session.server;

import com.google.common.base.Preconditions;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;

import java.util.HashSet;
import java.util.Set;

public final class ClientConnections {

    private Set<ServerPlayerEntity> clients = new HashSet<>();

    public void establishConnection(ServerPlayerEntity client) {
        Preconditions.checkArgument(!clients.contains(client), "Illegal to create a connection twice");
        clients.add(client);
    }

    public void receiveData(int messageType, PacketBuffer data) {
        // TODO
    }
}
