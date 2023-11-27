import network.connection.NetworkThreadFactory;

public class Main {
    public static void main(String[] args) {

        //TODO: Replace with ENV for docker-compose
        int port = 22035;
        int threads = 1;

        Core core = CoreSingleton.getInstance();

        core.getNetworkManager().connectNetworkThread(
                NetworkThreadFactory.getServerSocketBuilder()
                        .setPort(port)
                        .setThreadCount(threads)
                        .build()
        );




    }
}
