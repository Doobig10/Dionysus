import database.Database;

import gamedata.Agent;
import gamedata.GameState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;

public class ResourceManager {
    private final static Logger LOGGER = LoggerFactory.getLogger(ResourceManager.class);

    private Database database;

    public void linkDatabase(Database db) {
        this.database = db;
    }

    public GameState getSetupData() {
        Connection instance = this.database.getConnectionInstance();
        return null; // TODO: return functional GameState object
    }

    public Agent[] getPopulation(Object request) {
        Connection instance = this.database.getConnectionInstance();
        return null; //TODO: return array of agents
    }

    public boolean insertResults(Object results) {

        return true; //TODO: return true if success, else false
    }
}
