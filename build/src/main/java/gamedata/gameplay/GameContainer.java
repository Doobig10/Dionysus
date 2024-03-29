package gamedata.gameplay;

import gamedata.AgentPrecept;
import gamedata.AgentResult;
import gamedata.mapdata.roomTypes.RedRoom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class GameContainer {
    private static final Logger LOGGER = LoggerFactory.getLogger(GameContainer.class);

    private final int turnLimit;
    private final int requiredLoot;

    private boolean running = false;
    private PlayerObject winner = null;

    private final Board board;
    private final ArrayList<PlayerObject> players = new ArrayList<>();
    private final ArrayList<PlayerObject> active_players = new ArrayList<>();
    private final ArrayList<PlayerObject> playersToRemove = new ArrayList<>();

    public GameContainer(String boardSerialPath, int turnLimit, int requiredLoot) {
        Board boardObject = new Board(new RedRoom());
        try (
                ObjectInputStream objStream = new ObjectInputStream(new FileInputStream(boardSerialPath))
        )
        {
            boardObject = (Board) objStream.readObject();
        }
        catch (IOException | ClassNotFoundException
                        exception)
        {
            exception.printStackTrace();
        }
        finally {
            this.board = boardObject;
        }

        this.turnLimit = turnLimit;
        this.requiredLoot = requiredLoot;
    }

    public void addPlayer(PlayerObject player) {
        this.players.add(player);
    }

    public void removePlayer(PlayerObject player) {
        this.playersToRemove.add(player);
    }

    private void clearPlayers() {
        for (PlayerObject player : playersToRemove) {
            this.active_players.remove(player);
        }
    }

    public void setup() {
        LOGGER.atInfo().log("Initialising Game");
        this.active_players.addAll(players);
        for (PlayerObject player : active_players) {
            player.setLocation(board.getInitialLocation());
        }
        for (Encounter encounter : board.getEncounters()) {
            encounter.populate();
        }
    }

    public void startGame() {
        int turnCount = 0;
        this.running = true;
        while (turnCount++ < turnLimit && this.running && !active_players.isEmpty()) {
            LOGGER.atTrace().log("Starting turn: "+turnCount);
            for (PlayerObject player : active_players) {
                player.processTurn(this);
            }
            this.clearPlayers();
        }
        this.stop();
    }

    public HashMap<AgentPrecept, AgentResult> getResults() {
        if (this.running) {return null;}
        HashMap<AgentPrecept, AgentResult> results = new HashMap<>();
        for (PlayerObject player: players) {
            results.put(player.getPrecepts(), new AgentResult(
                    this.calculateScore(player),
                    player.getTurnsTaken()
            ));
        }
        return results;
    }

    public int calculateScore(PlayerObject player) {
        double score = 1000;
        score = score * Math.log(Math.min(player.getCurrentLootValue(), this.requiredLoot) + 1);
        score = score / (Math.log(Math.pow((player.getTurnsTaken() + 1), 1.3)));
        score = score / ((winner == player) ? 1 : 2);
        return (int) Math.floor(score);
    }

    public int getRequiredLoot() {
        return this.requiredLoot;
    }

    public Board getBoard() {
        return this.board;
    }

    public void stop() {
        this.running = false;
    }

    public void setWinner(PlayerObject winner) {
        if (this.winner == null) {this.winner = winner;}
    }
}
