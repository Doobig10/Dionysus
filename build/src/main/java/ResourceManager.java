import integration.Database;

import integration.tools.AgentModifier;
import integration.tools.AgentQueries;
import gamedata.AgentPrecept;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import integration.tools.Validation;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public class ResourceManager {
    private final static Logger LOGGER = LoggerFactory.getLogger(ResourceManager.class);

    private Database database;
    private boolean validated = false;

    private AgentModifier modifier;

    public void linkDatabase(Database db) {
        this.database = db;
    }

    public void linkModifier(AgentModifier modifier) {
        this.modifier = modifier;
    }

    public AgentPrecept[] getPopulation(int count) {
        if (!this.validated) {this.validated = Validation.validate(this.database);}

        LOGGER.atTrace().log("Generating population of size: "+count);
        ArrayList<AgentPrecept> agents = new ArrayList<>(count);
        try(Connection instance = this.database.getConnectionInstance()) {
            Integer[] ids = AgentQueries.getRandomExistingPopIDs(instance, count);
            ArrayList<AgentPrecept> rawPrecepts = new ArrayList<>();
            for (Integer id: ids) {
                AgentPrecept precept = AgentQueries.getPopDataByID(instance, id);
                rawPrecepts.add(precept);
            }
            for (AgentPrecept precept: rawPrecepts) {
                precept = modifier.attemptCross(precept, rawPrecepts.get((rawPrecepts.indexOf(precept)+1)%count));
                precept = modifier.attemptMutation(precept);
                agents.add(precept);
                LOGGER.atTrace().log("Added agent with ID: "+precept.getID());
            }
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return agents.toArray(new AgentPrecept[0]);
    }

    public boolean insertResults(Object results) {
        if (!this.validated) {this.validated = Validation.validate(this.database);}

        return true; //TODO: return true if success, else false
    }
}
