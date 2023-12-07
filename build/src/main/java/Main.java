import tooling.EnvironmentReader;
import network.connection.NetworkThreadFactory;
import database.DatabaseFactory;

public class Main {
    public static void main(String[] args) {

        String socket_address = EnvironmentReader.getEnv("DIONYSUS_SOCKET_ADDRESS", "localhost");
        String socket_port = EnvironmentReader.getEnv("DIONYSUS_SOCKET_PORT", "22035");
        String socket_threads = EnvironmentReader.getEnv("DIONYSUS_SOCKET_THREADS", "1");

        String database_address = EnvironmentReader.getEnv("DIONYSUS_DB_ADDRESS", "localhost");
        String database_port = EnvironmentReader.getEnv("DIONYSUS_DB_PORT", "3306");
        String database_name = EnvironmentReader.getEnv("DIONYSUS_DB_NAME", "dionysus_local");
        String database_username = EnvironmentReader.getEnv("DIONYSUS_DB_USERNAME", "default");
        String database_password = EnvironmentReader.getEnv("DIONYSUS_DB_PASSWORD", "");


        Master master = MasterSingleton.getInstance();

        master.getNetworkManager().connectNetworkThread(
                NetworkThreadFactory.getServerSocketBuilder()
                        .setAddress(socket_address)
                        .setPort(Integer.parseInt(socket_port))
                        .setThreadCount(Integer.parseInt(socket_threads))
                        .build()
        );

        master.getResourceManager().linkDatabase(
                DatabaseFactory.getMariaBuilder()
                        .setAddress(database_address)
                        .setPort(database_port)
                        .setDatabase(database_name)
                        .build()
                        .login(database_username, database_password)
        );

        /*
        try (Connection conn = db.getConnectionInstance()) {
            Statement statement = conn.createStatement();
            ResultSet results = statement.executeQuery("SELECT * FROM dionysus_local.example");
            while (results.next()) {
                String col1 = results.getString("col1");
                System.out.println(col1);
            }
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
         */

    }
}
