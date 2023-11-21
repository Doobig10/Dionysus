import com.google.gson.Gson;
import interactions.NetworkRequest;
import interactions.NetworkRequestSetup;

import java.io.*;
import java.net.Socket;

public class DummyConnection {

    public static void main(String[] args)
            throws IOException {
        Gson gson = new Gson();

        Socket clientSocket = new Socket("127.0.0.1", 22035);
        PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);
        BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        NetworkRequest request = new NetworkRequestSetup();
        String json = gson.toJson(request);

        writer.println(json);
        clientSocket.close();
    }

}
