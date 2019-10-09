package powerlessri.harmonics.session.server;

import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.event.TickEvent.ServerTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.server.ServerLifecycleEvent;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import powerlessri.harmonics.Config;
import powerlessri.harmonics.HarmonicsCore;

import java.util.concurrent.atomic.AtomicInteger;

public final class SessionManager {

    public static final SessionManager INSTANCE = new SessionManager();

    private final Int2ObjectMap<ISessionHandler> openSessions = new Int2ObjectOpenHashMap<>();
    private AtomicInteger nextSessionID = new AtomicInteger(0);

    private SessionManager() {
    }

    int nextSessionID() {
        return nextSessionID.getAndIncrement();
    }

    public synchronized int registerSession(ISessionHandler handler) {
        int id = nextSessionID();
        openSessions.put(id, handler);
        return id;
    }

    public synchronized void removeSession(int id) {
        openSessions.remove(id);
    }

    public ISessionHandler getSession(int id) {
        tryRefresh();
        ISessionHandler handler = openSessions.get(id);
        Preconditions.checkState(handler != null);
        return handler;
    }

    private long lastRefresh = 0L;

    private void tryRefresh() {
        long current = ServerLifecycleHooks.getCurrentServer().getWorld(DimensionType.OVERWORLD).getGameTime();
        if (current - lastRefresh >= Config.COMMON.sessionRefreshInterval.get()) {
            refresh();
            lastRefresh = current;
        }
    }

    void refresh() {
        openSessions.values().removeIf(session -> !session.isValid());
    }
}
