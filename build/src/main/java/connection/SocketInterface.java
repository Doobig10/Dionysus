package connection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class SocketInterface implements Runnable{
    private static final Logger logger = LoggerFactory.getLogger(SocketInterface.class);

    private final int port;

    private int maxThreads = 1;

    public SocketInterface(int port) {
        logger.atInfo().log("Created SocketInterface on port: "+port);
        this.port = port;
    }

    public void setMaxThreads(int maxThreads) {
        this.maxThreads = maxThreads;
    }

    @Override
    public void run() {
        try (
                ServerSocket serverSocket = new ServerSocket(port))
        {
            ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(maxThreads);
            Socket clientSocket = serverSocket.accept();
            executor.execute(new ConnectionHandler(clientSocket));
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
