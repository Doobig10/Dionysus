import connection.SocketInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        logger.atInfo().log("Main Initialised");
        Thread localInterface = new Thread(
                new SocketInterface(22035)
        );
        localInterface.start();
    }

}
