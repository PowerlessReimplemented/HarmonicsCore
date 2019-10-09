package powerlessri.harmonics.session.server;

public interface ISessionHandler {

    ClientConnections getConnectionHandler();

    void registerDataTypes(ClientConnections connections);

    /**
     * Whether this session handler is still valid or not. This will be called every interval, and if it returns
     * {@code false}, the handler will be closed.
     *
     * This is designed for game objects that does not have an onRemove event available or the event is unreliable, such
     * as tile entities. The interval in which every session handler gets revisited can be configured in the config.
     */
    boolean isValid();
}
