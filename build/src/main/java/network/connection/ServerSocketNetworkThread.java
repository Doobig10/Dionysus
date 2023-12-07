package network.connection;

import network.connection.exceptions.AlreadyBoundException;
import network.connection.exceptions.UnboundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class ServerSocketNetworkThread implements NetworkThread{
    private final static Logger LOGGER = LoggerFactory.getLogger(ServerSocketNetworkThread.class);

    private NetworkMaster master;

    private final int port;
    private final int threadCount;

    private final Thread thread = new Thread(this);

    ServerSocketNetworkThread(ServerSocketNetworkThreadBuilder builder) {
        this.port = builder.getPort();
        this.threadCount = builder.getThreadCount();
        LOGGER.atInfo().log("Created ServerSocketNetworkThread for port: ["+this.port+"]");
    }

    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(this.port);
        ) {
            if (this.master == null) {
                throw new UnboundException();
            }

            Socket clientSocket;
            ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(this.threadCount);
            while ((clientSocket = serverSocket.accept()) != null) {
                executor.execute(new ServerSocketConnectionHandler(clientSocket, this.master));
            }

        } catch (IOException ioException) {
            LOGGER.atDebug().log("IOException in ServerSocketNetworkThread, closing");
        } catch (UnboundException e) {
            LOGGER.atDebug().log("UnboundException, Object must be bound to master");
        } finally {

        }
    }

    @Override
    public void bind(NetworkMaster master) throws AlreadyBoundException {
        if (this.master == null) {
            LOGGER.atTrace().log("Bound new NetworkMaster");
            this.master = master;
        }
        else {
            throw new AlreadyBoundException();
        }
    }

    @Override
    public boolean isBound() {
        return (this.master != null);
    }

    @Override
    public void start() {
        if (!this.thread.isAlive()) {
            this.thread.start();
        }
    }

    @Override
    public void close() {
        if (!this.thread.isAlive()) {
            this.thread.interrupt();
        }
    }
}
