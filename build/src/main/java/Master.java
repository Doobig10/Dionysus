
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Master {
    private static final Logger LOGGER = LoggerFactory.getLogger(Master.class);

    private final NetworkManager networkManager = new NetworkManager();
    private final ResourceManager resourceManager = new ResourceManager();

    public ResourceManager getResourceManager() {
        return this.resourceManager;
    }

    public NetworkManager getNetworkManager() {
        return this.networkManager;
    }

}
