package integration.tools;

import gamedata.AgentPrecept;
import gamedata.mapdata.roomTypes.RoomType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public final class AgentQueries {
    private static final Logger LOGGER = LoggerFactory.getLogger(AgentQueries.class);

    public static Integer[] getRandomExistingPopIDs(Connection conn, Integer count) throws SQLException {
        Statement statement = conn.createStatement();
        ResultSet results = statement.executeQuery("""
            SELECT agent_id, -LOG(1-RAND())*AVG(score) OVER (PARTITION BY agent_id) AS weight
                FROM results
                GROUP BY agent_id
                ORDER BY weight DESC
                LIMIT\s
            """+count);

        ArrayList<Integer> ids = new ArrayList<>();
        while (results.next()){
            ids.add(results.getInt("agent_id"));
        }
        return ids.toArray(new Integer[0]);
    }

    public static AgentPrecept getPopDataByID(Connection conn, Integer id) throws SQLException {
        LOGGER.atTrace().log("Getting pop with ID: "+id);
        AgentPrecept precept = null;

        Statement statement = conn.createStatement();
        ResultSet results= statement.executeQuery("""
            SELECT *
                FROM agents
                JOIN predictions on agents.id = predictions.agent_id
                WHERE agents.id =\s
            """+id);

        if (results.next()) {
            precept = new AgentPrecept(
                    id,
                    results.getInt("generation"),
                    results.getDouble("accuracy"),
                    results.getDouble("distance")
                    ).addLootPrediction(
                    RoomType.valueOf(results.getString("type")),
                    results.getInt("loot")
                    ).addDifficultyFactor(
                    RoomType.valueOf(results.getString("type")),
                    results.getDouble("difficulty")
            );
            while (results.next()) {
                precept.addLootPrediction(
                        RoomType.valueOf(results.getString("type")),
                        results.getInt("loot")
                ).addDifficultyFactor(
                        RoomType.valueOf(results.getString("type")),
                        results.getDouble("difficulty")
                );
            }
        }
        LOGGER.atDebug().log("Found: "+precept);
        return precept;
    }

    public static Integer getRandomExistingPopID(Connection conn) throws SQLException {
        return getRandomExistingPopIDs(conn, 1)[0];
    }
}
