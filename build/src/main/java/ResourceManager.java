import database.Database;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResourceManager {
    private final static Logger LOGGER = LoggerFactory.getLogger(ResourceManager.class);

    private Database database;

    public void linkDatabase(Database db) {
        this.database = db;
    }

    public Object getSetupData() {
        return null;
    }
}
