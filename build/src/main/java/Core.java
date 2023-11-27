
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Core {
    private static final Logger LOGGER = LoggerFactory.getLogger(Core.class);

    private final NetworkManager networkManager = new NetworkManager();
    private final DatabaseManager databaseManager = new DatabaseManager();

    public DatabaseManager getDatabaseManager() {
        return this.databaseManager;
    }

    public NetworkManager getNetworkManager() {
        return this.networkManager;
    }
}
