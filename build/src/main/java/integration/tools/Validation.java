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
            create table if not exists dionysus_local.agents
            (
                id         int auto_increment
                    primary key,
                generation int    not null,
                accuracy   double not null,
                distance   double not null
            );
            """);
        statement.addBatch("""
            create table if not exists dionysus_local.predictions
            (
                agent_id   int         not null,
                type       varchar(15) not null,
                loot       int         not null,
                difficulty double      not null,
                primary key (agent_id, type),
                constraint predictions_agents_id_fk
                    foreign key (agent_id) references dionysus_local.agents (id)
                        on delete cascade
            )
                comment 'Agent Predictions HashMap';
            """);
        statement.addBatch("""
            create table if not exists dionysus_local.results
            (
                id       int auto_increment
                    primary key,
                agent_id int not null,
                score    int not null,
                turns    int null,
                constraint results_agents_id_fk
                    foreign key (agent_id) references dionysus_local.agents (id)
                        on delete cascade
            )
                comment 'Stores game results';
            """);
        statement.addBatch("""
            create table if not exists dionysus_local.epochs
            (
                epoch      int    not null,
                generation int    not null,
                avg_score  double not null,
                avg_turns  double not null,
                max_score  double not null,
                min_turns  double not null,
                primary key (epoch, generation)
            )
                comment 'Stores generational averages across epochs';      
            """);
        int[] updates = statement.executeBatch();
        return (updates[0] + updates[1] + updates[2] + updates[3]) > 0;
    }

}
