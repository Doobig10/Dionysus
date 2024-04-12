import com.google.gson.Gson;
import gamedata.AgentPrecept;
import gamedata.AgentResult;
import gamedata.gameplay.GameContainer;
import gamedata.gameplay.PlayerObject;
import network.interactions.Body;
import network.interactions.InteractionType;
import network.interactions.NetworkInteraction;
import network.interactions.NetworkInteractionBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tooling.Environment;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;

public final class Client {

    private static final Logger LOGGER = LoggerFactory.getLogger(Client.class);
    private static final Gson gson = new Gson();

    private static final Client instance = new Client();

    private final int defaultTurnLimit = 100;
    private final int defaultScoreRequired = 15000;

    private GameContainer container;

    private void initGame(String boardPath, int turnLimit, int goal) {
        this.container = new GameContainer(
                boardPath,
                turnLimit,
                goal
        );
    }

    private void initGame(String boardPath) {
        this.initGame(
                boardPath,
                this.defaultTurnLimit,
                this.defaultScoreRequired
        );
    }

    private void addPlayer(AgentPrecept precept) {
        this.container.addPlayer(
                new PlayerObject(precept)
        );
    }

    private void addAllPlayers(AgentPrecept[] precepts) {
        for (AgentPrecept precept : precepts) {
            this.addPlayer(precept);
        }
    }

    private void start() {
        this.container.setup();
        this.container.startGame();
    }

    private void sendResults(String address, int port) throws IOException {
        HashMap<AgentPrecept, AgentResult> results = this.container.getResults();
        HashMap<Integer, AgentResult> content = new HashMap<>();
        for (AgentPrecept key : results.keySet()) {content.put(key.getID(), results.get(key));}
        LOGGER.atInfo().log("Sending Results: "+content);

        Socket clientSocket = new Socket(address, port);
        PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);

        Body.FinaliseRequest req = new Body.FinaliseRequest(content);
        NetworkInteraction interaction = new NetworkInteractionBuilder(InteractionType.FINALISE)
                .setJson(req)
                .build();
        String json = gson.toJson(interaction);
        writer.println(json);

        clientSocket.close();
    }

    private AgentPrecept[] getPrecepts(int count, String address, int port) throws IOException {
        Socket clientSocket = new Socket(address, port);
        PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);
        BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        Body.PopulateRequest req = new Body.PopulateRequest(count);
        NetworkInteraction interaction = new NetworkInteractionBuilder(InteractionType.POPULATE)
                .setJson(req)
                .build();
        String json = gson.toJson(interaction);
        writer.println(json);

        String response;
        AgentPrecept[] precepts;
        if ((response = reader.readLine()) != null) {
            clientSocket.close();
            NetworkInteraction received = gson.fromJson(response, NetworkInteraction.class);
            Body.PopulateRequest output = gson.fromJson(received.getJson(), Body.PopulateRequest.class);
            precepts = output.getPopulation();
        }
        else {
            throw new RuntimeException();
        }

        return precepts;
    }

    public static void main (String[] args) {
        Environment.EnvTool<String> stringEnvTool = new Environment.EnvTool<>(String.class);
        Environment.EnvTool<Integer> integerEnvTool = new Environment.EnvTool<>(Integer.class);

        String path = stringEnvTool.getOrDefault("CLIENT_BOARD_PATH", "default_board.ser");
        String address = stringEnvTool.getOrDefault("CLIENT_SOCKET_ADDRESS","localhost");
        Integer port = integerEnvTool.getOrDefault("CLIENT_SOCKET_PORT", 22035);
        Integer maxIterations = integerEnvTool.getOrDefault("CLIENT_MAX_ITERATIONS", -1);

        AgentPrecept[] precepts;
        int iterations = 0;
        while (iterations++ < maxIterations || maxIterations == -1) {

            try {
                precepts = instance.getPrecepts(4, address, port);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            instance.initGame(path);
            instance.addAllPlayers(precepts);
            instance.start();

            try {
                instance.sendResults(address, port);
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
