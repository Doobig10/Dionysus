import gamedata.AgentPrecept;
import gamedata.AgentResult;
import gamedata.mapdata.roomTypes.RoomType;
import integration.Database;
import integration.DatabaseFactory;
import integration.tools.AgentQueries;
import integration.tools.Validation;
import org.junit.Test;

import java.sql.SQLException;
import java.util.concurrent.ThreadLocalRandom;

public class CreateAgentTest {

    @Test
    public void insertTest() throws SQLException {
        Database db = DatabaseFactory.getMariaBuilder()
                .setAddress("localhost")
                .setPort(3306)
                .setDatabase("dionysus_local")
                .build()
                .login("default", "");

        Validation.validate(db);

        AgentPrecept precept = new AgentPrecept(-1, 0,
                ThreadLocalRandom.current().nextDouble(0.1, 0.9),
                ThreadLocalRandom.current().nextDouble(0.1, 0.9)
        );

        for (RoomType type: RoomType.values()) {
            if (type != RoomType.STAIRCASE) {
                precept.addLootPrediction(type,
                        ThreadLocalRandom.current().nextInt(0, 10000)
                );
                precept.addDifficultyFactor(type,
                        ThreadLocalRandom.current().nextDouble(0.1, 0.9)
                );
            }
        }
        int id = AgentQueries.insertAgent(db.getConnectionInstance(),
                precept
        );
        precept.setID(id);
        boolean success = AgentQueries.insertResult(db.getConnectionInstance(),
                precept.getID(),
                new AgentResult(1, 0)
        );
    }

    @Test
    public void insertBatchTest() throws SQLException {
        int batchSize = 10;

        Database db = DatabaseFactory.getMariaBuilder()
                .setAddress("localhost")
                .setPort(3306)
                .setDatabase("dionysus_local")
                .build()
                .login("default", "");

        Validation.validate(db);

        for (int index = 0; index < batchSize; index++) {
            AgentPrecept precept = new AgentPrecept(-1, 0,
                    ThreadLocalRandom.current().nextDouble(0.1, 0.9),
                    ThreadLocalRandom.current().nextDouble(0.1, 0.9)
            );

            for (RoomType type : RoomType.values()) {
                if (type != RoomType.STAIRCASE) {
                    precept.addLootPrediction(type,
                            ThreadLocalRandom.current().nextInt(0, 10000)
                    );
                    precept.addDifficultyFactor(type,
                            ThreadLocalRandom.current().nextDouble(0.1, 0.9)
                    );
                }
            }
            int id = AgentQueries.insertAgent(db.getConnectionInstance(),
                    precept
            );
            precept.setID(id);
            boolean success = AgentQueries.insertResult(db.getConnectionInstance(),
                    precept.getID(),
                    new AgentResult(1, 0)
            );
        }
    }
}
