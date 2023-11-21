
import connection.NetworkThreadFactory;
import core.NetworkManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {

        NetworkManager manager = new NetworkManager();

        manager.connectNetworkThread(
                NetworkThreadFactory.getServerSocketBuilder()
                        .setPort(22035)
                        .setThreadCount(1)
                        .build()
                );
    }

}
