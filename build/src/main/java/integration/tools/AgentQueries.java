package integration.tools;

import gamedata.AgentPrecept;
import gamedata.AgentResult;
import gamedata.mapdata.roomTypes.RoomType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;

public final class AgentQueries {
    private static final Logger LOGGER = LoggerFactory.getLogger(AgentQueries.class);

    public static void cullAgents(Connection conn, int percent) throws SQLException {
        Statement statement = conn.createStatement();
        ResultSet results = statement.executeQuery("""
            SELECT COUNT(*) AS total
            FROM agents
            """);
        results.next();
        int total = results.getInt("total");
        LOGGER.atTrace().log("Total: "+total);

        PreparedStatement preparedStatement = conn.prepareStatement("""
            SELECT agents.id AS id
            FROM agents
            LEFT JOIN results on agents.id = results.agent_id
            GROUP BY agents.id
            ORDER BY AVG(score) ASC
            LIMIT ?
            """);
        LOGGER.atTrace().log("Quantity: "+(int) (((double) percent/100)*total));
        preparedStatement.setInt(1,(int) (((double) percent/100)*total));
        preparedStatement.execute();
        ResultSet preparedResults = preparedStatement.getResultSet();

        StringBuilder queryBuilder = new StringBuilder("(");
        LOGGER.atTrace().log("Success:" + preparedResults.next());
        queryBuilder.append(preparedResults.getInt("id"));
        while (preparedResults.next()) {
            queryBuilder.append(", ");
            queryBuilder.append(preparedResults.getString("id"));
        }
        queryBuilder.append(")");
        String query = queryBuilder.toString();
        LOGGER.atTrace().log(query);
        PreparedStatement deleteStatement = conn.prepareStatement("""
            DELETE
            FROM agents
            WHERE agents.id IN\s
            """+query);
        deleteStatement.execute();
    }

    public static Integer[] getRandomExistingPopIDs(Connection conn, Integer count) throws SQLException {
        Statement statement = conn.createStatement();
        ResultSet results = statement.executeQuery("""
            SELECT agent_id, 1-RAND()*AVG(score) OVER (PARTITION BY agent_id) AS weight
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

    public static int insertAgent(Connection conn, AgentPrecept agent) throws SQLException {
        PreparedStatement preparedStatement;
        Statement statement;
        int id = -1;

        preparedStatement = conn.prepareStatement("""
        INSERT INTO agents (generation, accuracy, distance)
        VALUES (?, ?, ?)
        """);
        preparedStatement.setInt(1, agent.getGeneration());
        preparedStatement.setDouble(2, agent.getAccuracyFactor());
        preparedStatement.setDouble(3, agent.getDistanceFactor());
        preparedStatement.executeQuery();

        statement = conn.createStatement();
        ResultSet results = statement.executeQuery("""
        SELECT agents.id
        FROM agents
        WHERE agents.id = LAST_INSERT_ID()
        """);

        if (results.next()) {
            id = results.getInt("id");
            for (RoomType type : RoomType.values()) {
                preparedStatement = conn.prepareStatement("""
                    INSERT INTO predictions (agent_id, type, loot, difficulty)
                    VALUES (?, ?, ?, ?)
                    """);
                preparedStatement.setInt(1, id);
                preparedStatement.setString(2, type.name());
                preparedStatement.setInt(3, agent.getLootPrediction(type));
                preparedStatement.setDouble(4, agent.getDifficultyFactor(type));
                preparedStatement.execute();
            }
        }
        return id;
    }

    public static boolean insertResult(Connection conn, Integer id, AgentResult result) throws SQLException {
        PreparedStatement statement = conn.prepareStatement("""
        INSERT INTO results (agent_id, score, turns) 
        VALUES (?, ?, ?)
        """);
        statement.setInt(1, id);
        statement.setInt(2, result.getScore());
        statement.setInt(3, result.getTurns());

        ResultSet results= statement.executeQuery();
        return true;
    }

    public static boolean logEpoch(Connection conn, Integer epoch) throws SQLException {
        Statement statement = conn.createStatement();
        ResultSet results = statement.executeQuery("""
            SELECT agents.generation,
            AVG(results.score), AVG(results.turns),
            MAX(results.score), MIN(results.turns)
            FROM agents
            JOIN results ON agents.id = results.agent_id
            GROUP BY agents.generation
            """);
        PreparedStatement preparedStatement = conn.prepareStatement("""
            INSERT INTO epochs(epoch, generation, avg_score, avg_turns, max_score, min_turns)
            VALUES (?, ?, ?, ?, ?, ?)
            """);
        while(results.next()) {
            preparedStatement.setInt(1,epoch);
            preparedStatement.setInt(2, results.getInt(1));

            preparedStatement.setDouble(3, results.getDouble(2));
            preparedStatement.setDouble(4,results.getDouble(3));
            preparedStatement.setInt(5, results.getInt(4));
            preparedStatement.setInt(6, results.getInt(5));

            preparedStatement.execute();
            preparedStatement.clearParameters();
        }
        return true;
    }
}
