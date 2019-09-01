package powerlessri.harmonics.session.server;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

public final class SessionManager {

    private int nextSessionID = 0;

    public int nextSessionID() {
        return nextSessionID++;
    }

    private final Int2ObjectMap<ISessionHandler> openSessions = new Int2ObjectOpenHashMap<>();

//    public void openSession()
}
