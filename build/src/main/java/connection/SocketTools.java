package connection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.Socket;

public final class SocketTools {
    private static final Logger logger = LoggerFactory.getLogger(SocketTools.class);

    public static void closeSilently(Socket socket) {
        try {
            socket.close();
        } catch (IOException e) {
            logger.atDebug().log("Ignoring exception during silent close", e);
        }
    }
}
