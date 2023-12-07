package database;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MariaDatabase implements Database {
    private static final Logger LOGGER = LoggerFactory.getLogger(MariaDatabase.class);

    private final String address;
    private final String port;
    private final String database;

    private String user;
    private String password;

    MariaDatabase(MariaDatabaseBuilder builder) {
        this.address = builder.address;
        this.port = builder.port;
        this.database = builder.database;
        LOGGER.atInfo().log("Created database link");
    }

    public Database login(String user, String password) {
        this.user = user;
        this.password = password;
        return this;
    }

    public Connection getConnectionInstance(boolean autoCommit) {
        String url = "jdbc:mariadb://"+this.address+":"+this.port+"/"+this.database;
        LOGGER.atTrace().log("Attempting to create Connection instance for: "+url);
        try {
            Connection connection = DriverManager.getConnection(url, this.user, this.password);
            connection.setAutoCommit(autoCommit);
            LOGGER.atTrace().log("Successfully created Connection instance");
            return connection;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Connection getConnectionInstance() {
        return getConnectionInstance(true);
    }
}
