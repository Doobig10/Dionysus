package connection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;

public class ConnectionHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(ConnectionHandler.class);

    private final Socket clientSocket;

    ConnectionHandler(Socket clientSocket) {
        logger.atInfo().log("ConnectionHandler initialised for connection @ "+clientSocket.toString());
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try (
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(this.clientSocket.getOutputStream()));
                BufferedReader reader = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()))
        ) {
            String line;
            while (((line = reader.readLine()) != null) && (!(line.equals("")))) {
                System.out.println(line);
            }
            if (Thread.interrupted()) {
                throw new InterruptedException();
            }
        }
        catch (IOException e) {
            logger.atDebug().log("IOException");
        }
        catch (InterruptedException e) {
            logger.atDebug().log("ConnectionHandler Interrupted");
        }
        finally {
            SocketTools.closeSilently(this.clientSocket);
        }
    }
}
