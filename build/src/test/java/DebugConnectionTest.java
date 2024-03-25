import com.google.gson.Gson;
import gamedata.AgentPrecept;
import gamedata.AgentResult;
import integration.tools.AgentQueries;
import network.interactions.Body;
import network.interactions.InteractionType;
import network.interactions.NetworkInteraction;
import network.interactions.NetworkInteractionBuilder;
import org.junit.Test;


import java.io.*;
import java.net.Socket;
import java.util.HashMap;

import static org.junit.Assert.*;

public class DebugConnectionTest {
    @Test
    public void echoTest() throws IOException {
        String expectation = "echo";

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
            String output = gson.fromJson(received.getJson(), String.class);
            assertEquals(expectation, output);
        }
    }

    @Test
    public void getPopTest() throws IOException {
        Body.PopulateRequest req = new Body.PopulateRequest(6);
        Gson gson = new Gson();

        Socket clientSocket = new Socket("127.0.0.1", 22035);
        PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);
        BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        NetworkInteraction interaction = new NetworkInteractionBuilder(InteractionType.POPULATE)
                .setJson(req)
                .build()
                ;
        String json = gson.toJson(interaction);
        writer.println(json);

        String response;
        if ((response = reader.readLine()) != null) {
            clientSocket.close();

            NetworkInteraction received = gson.fromJson(response, NetworkInteraction.class);
            Body.PopulateRequest output = gson.fromJson(received.getJson(), Body.PopulateRequest.class);
            for (AgentPrecept precept: output.getPopulation()) {
                System.out.println("Pop Data:"+
                        "\n\tID: "+precept.getID()+
                        "\n\tGeneration: "+precept.getGeneration()+
                        "\n\tData: "+precept
                );
            }
            assertTrue((output.getPopulation().length == req.getRequiredPopulation()));
        }
        else {
            fail();
        }
    }

    @Test
    public void FakeGamesTest() throws IOException {
        Body.PopulateRequest req = new Body.PopulateRequest(6);
        Gson gson = new Gson();

        Socket clientSocket = new Socket("127.0.0.1", 22035);
        PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);
        BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        NetworkInteraction interaction = new NetworkInteractionBuilder(InteractionType.POPULATE)
                .setJson(req)
                .build()
                ;
        String json = gson.toJson(interaction);
        writer.println(json);

        String response;
        if ((response = reader.readLine()) != null) {
            clientSocket.close();

            Socket clientSocket2 = new Socket("127.0.0.1", 22035);
            PrintWriter writer2 = new PrintWriter(clientSocket2.getOutputStream(), true);

            NetworkInteraction received = gson.fromJson(response, NetworkInteraction.class);
            Body.PopulateRequest output = gson.fromJson(received.getJson(), Body.PopulateRequest.class);

            HashMap<Integer, AgentResult> map = new HashMap<>();
            for (AgentPrecept precept: output.getPopulation()) {
                map.put(precept.getID(), new AgentResult(precept.getGeneration(), precept.getGeneration()));
            }

            Body.FinaliseRequest finalise = new Body.FinaliseRequest(map);
            NetworkInteraction finaliseInteraction = new NetworkInteractionBuilder(InteractionType.FINALISE)
                    .setJson(finalise)
                    .build();
            String finaliseJson = gson.toJson(finaliseInteraction);
            writer2.println(finaliseJson);
        }
        else {
            fail();
        }
    }

}
