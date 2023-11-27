package network.connection;

public interface NetworkMaster {

    void connectNetworkThread(NetworkThread thread);
    Class<? extends Request> getRequestClass();
    Completion process(Request request);
}
