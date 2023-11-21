package connection;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;

public class ServerSocketConnectionHandler implements Runnable{
    private final static Logger LOGGER = LoggerFactory.getLogger(ServerSocketConnectionHandler.class);

    private final NetworkMaster networkMaster;
    private final Socket clientSocket;

    ServerSocketConnectionHandler(Socket clientSocket, NetworkMaster networkMaster) {
        this.networkMaster = networkMaster;
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try (
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(this.clientSocket.getOutputStream()));
                BufferedReader reader = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()))
        ) {
            String data;
            Gson gson = new Gson();
            while (((data = reader.readLine()) != null) && (!this.clientSocket.isClosed())) {
                Request request = gson.fromJson(data, networkMaster.getRequestClass());
                networkMaster.process(request);
                //TODO: Implement Response
            }
            if (Thread.interrupted()) {
                throw new InterruptedException();
            }


        } catch (IOException ioException) {
            LOGGER.atDebug().log("IOException");
        } catch (InterruptedException e) {
            LOGGER.atDebug().log("ServerSocketConnectionHandler Interrupted");
        } finally {
            LOGGER.atTrace().log("Closed connection @ "+this.clientSocket.toString());
            SocketTools.closeSilently(this.clientSocket);
        }
    }
}
