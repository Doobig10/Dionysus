import gamedata.AgentPrecept;
import gamedata.mapdata.roomTypes.RoomType;
import integration.tools.AgentModifier;
import tooling.Environment;
import network.connection.NetworkThreadFactory;
import integration.DatabaseFactory;

public final class Server {

    private final static Environment.EnvTool<String> stringEnvTool = new Environment.EnvTool<>(String.class);
    private final static Environment.EnvTool<Integer> integerEnvTool = new Environment.EnvTool<>(Integer.class);

    public static void main(String[] args) {

        String socket_address = stringEnvTool.getOrDefault("DIONYSUS_SOCKET_ADDRESS", "localhost");
        Integer socket_port = integerEnvTool.getOrDefault("DIONYSUS_SOCKET_PORT", 22035);
        Integer socket_threads = integerEnvTool.getOrDefault("DIONYSUS_SOCKET_THREADS", 1);

        String database_address = stringEnvTool.getOrDefault("DIONYSUS_DB_ADDRESS", "localhost");
        Integer database_port = integerEnvTool.getOrDefault("DIONYSUS_DB_PORT", 3306);

        String database_name = stringEnvTool.getOrDefault("DIONYSUS_DB_NAME", "dionysus_local");
        String database_username = stringEnvTool.getOrDefault("DIONYSUS_DB_USERNAME", "default");
        String database_password = stringEnvTool.getOrDefault("DIONYSUS_DB_PASSWORD", "");


        Master master = MasterSingleton.getInstance();

        master.getNetworkManager().connectNetworkThread(
                NetworkThreadFactory.getServerSocketBuilder()
                        .setAddress(socket_address)
                        .setPort(socket_port)
                        .setThreadCount(socket_threads)
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

        master.getResourceManager().linkModifier(
                //TODO: Replace with Builder + Envs
                new AgentModifier(
                        10,
                        0.1,
                        5
                )
        );

    }
}
