package connection;

public interface NetworkMaster {

    void connectNetworkThread(NetworkThread thread);
    Class<? extends Request> getRequestClass();
    void process(Request request);
}
