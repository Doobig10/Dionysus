package integration.tools;

import integration.Database;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public final class Validation {
    private static final Logger LOGGER = LoggerFactory.getLogger(Validation.class);

    public static Boolean validate(Database db) {
        try (Connection instance = db.getConnectionInstance()) {
            if (Validation.generate(instance)) {
                LOGGER.atTrace().log("Created missing tables");
            } else {
                LOGGER.atTrace().log("No missing tables, marking validated");
            }
            return true;
        } catch (SQLException e) {
            LOGGER.atError().log("Unable to validate tables, Please see stack trace for details.");
            LOGGER.atError().log(e.getMessage());
            return false;
        }
    }

    public static boolean generate(Connection conn) throws SQLException {
        LOGGER.atTrace().log("Attempting create tables");
        Statement statement = conn.createStatement();
        statement.addBatch("""
            create table if not exists agents
            (
                id         int auto_increment
                    primary key,
                generation int    not null,
                accuracy   double not null,
                distance   double null
            );
            """);
        statement.addBatch("""
            create table if not exists predictions
            (
                agent_id   int         not null,
                type       varchar(15) not null,
                loot       int         not null,
                difficulty double      not null,
                primary key (agent_id, type),
                constraint predictions_agents_id_fk
                    foreign key (agent_id) references agents (id)
                        on delete cascade
            )
                comment 'Agent Predictions HashMap';
            """);
        statement.addBatch("""
            create table if not exists results
            (
                id       int auto_increment
                    primary key,
                agent_id int not null,
                score    int not null,
                constraint results_agents_id_fk
                    foreign key (agent_id) references agents (id)
                        on delete cascade
            )
                comment 'Stores game results';
            """);
        int[] updates = statement.executeBatch();
        return updates[0] + updates[1] + updates[2] > 0;
    }

}
