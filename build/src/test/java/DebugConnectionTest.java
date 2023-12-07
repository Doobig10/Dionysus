import com.google.gson.Gson;
import network.interactions.InteractionType;
import network.interactions.NetworkInteraction;
import network.interactions.NetworkInteractionBuilder;
import org.junit.Test;


import java.io.*;
import java.net.Socket;

import static org.junit.Assert.assertEquals;

public class DebugConnectionTest {
    @Test
    public void echoTest() throws IOException {
        Master expectation = new Master();

        Gson gson = new Gson();

        Socket clientSocket = new Socket("127.0.0.1", 22035);
        PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);
        BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        NetworkInteraction interaction = new NetworkInteractionBuilder(InteractionType.DEBUG_ECHO)
                .setJson(expectation)
                .build()
                ;
        String json = gson.toJson(interaction);
        writer.println(json);

        String response;
        if ((response = reader.readLine()) != null) {
            clientSocket.close();

            System.out.println("Read: "+response);
            NetworkInteraction received = gson.fromJson(response, NetworkInteraction.class);
            Master output = gson.fromJson(received.getJson(), Master.class);
            assertEquals(output.getNetworkManager().getRequestClass(), expectation.getNetworkManager().getRequestClass());
        }
    }

}
