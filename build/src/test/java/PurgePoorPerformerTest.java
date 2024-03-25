import integration.Database;
import integration.DatabaseFactory;
import integration.tools.AgentQueries;
import org.junit.Test;

import java.sql.SQLException;

public class PurgePoorPerformerTest {

    @Test
    public void CullLowestScore() throws SQLException {
        Database db = DatabaseFactory.getMariaBuilder()
                .setAddress("localhost")
                .setPort(3306)
                .setDatabase("dionysus_local")
                .build()
                .login("default", "");

        AgentQueries.cullAgents(db.getConnectionInstance(), 75);
    }
}
