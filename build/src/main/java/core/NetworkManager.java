package core;

import connection.NetworkMaster;
import connection.NetworkThread;
import connection.Request;
import connection.exceptions.AlreadyBoundException;
import interactions.NetworkRequest;
import interactions.NetworkRequestFinalise;
import interactions.NetworkRequestSetup;
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

    @Override
    public Class<? extends Request> getRequestClass() {
        return NetworkRequest.class;
    }

    @Override
    public void process(Request rawRequest) {
        NetworkRequest request = (NetworkRequest) rawRequest;
        switch (request.getNature()) {
            case SETUP -> {
                LOGGER.atTrace().log("Received setup request");
                NetworkRequestSetup qualifiedRequest = (NetworkRequestSetup) request;

            }
            case FINALISE -> {
                LOGGER.atTrace().log("Received finalise request");
                NetworkRequestFinalise qualifiedRequest = (NetworkRequestFinalise) request;

            }
            default -> {
                LOGGER.atDebug().log("Received unexpected request type");
            }
        }
    }
}
;