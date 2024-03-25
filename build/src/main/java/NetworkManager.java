import com.google.gson.Gson;
import network.connection.Completion;
import network.connection.NetworkMaster;
import network.connection.NetworkThread;
import network.connection.Request;
import network.connection.exceptions.AlreadyBoundException;
import network.interactions.Body;
import network.interactions.NetworkInteraction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class NetworkManager implements NetworkMaster {
    private final static Logger LOGGER = LoggerFactory.getLogger(NetworkManager.class);

    private final List<NetworkThread> networkThreads = new ArrayList<>();

    @Override
    public void connectNetworkThread(NetworkThread thread) {
        try {
            LOGGER.atInfo().log("Connecting "+thread+" to NetworkManager");
            thread.bind(this);
            networkThreads.add(thread);
            thread.start();
        } catch (AlreadyBoundException alreadyBoundException) {
            throw new RuntimeException(alreadyBoundException);
        }
    }

    public void close(){
        for (NetworkThread thread: this.networkThreads) {
            thread.close();
        }
    }

    @Override
    public Class<? extends Request> getRequestClass() {
        return NetworkInteraction.class;
    }

    @Override
    public Completion process(Request rawRequest) {
        Gson gson = new Gson();
        ResourceManager resourceManager = MasterSingleton.getInstance().getResourceManager();

        NetworkInteraction interaction = (NetworkInteraction) rawRequest;
        interaction.updateJson(
            switch (interaction.getHeader()) {
                case POPULATE -> {
                    LOGGER.atTrace().log("Received Populate request");
                    Body.PopulateRequest contents = gson.fromJson(interaction.getJson(), Body.PopulateRequest.class);
                    contents.setPopulation(resourceManager.getPopulation(contents.getRequiredPopulation()));
                    yield contents;
                }
                case FINALISE -> {
                    LOGGER.atTrace().log("Received Finalise request");
                    Body.FinaliseRequest contents = gson.fromJson(interaction.getJson(), Body.FinaliseRequest.class);
                    contents.setSuccess(resourceManager.insertResults(contents.getResults()));
                    yield contents;
                }

                case DEBUG_ECHO -> {
                    Object contents = gson.fromJson(interaction.getJson(), Object.class);
                    LOGGER.atDebug().log("Debug Echoing: "+contents.toString());
                    yield contents;
                }
            }
        );
        LOGGER.atDebug().log("Returning interaction: "+interaction.getJson());
        return interaction;
    }
}