package database;

import java.sql.Connection;

public interface Database {
    Connection getConnectionInstance();

}
