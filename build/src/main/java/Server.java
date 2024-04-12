import gamedata.AgentPrecept;
import gamedata.mapdata.roomTypes.RoomType;
import integration.tools.AgentModifier;
import tooling.Environment;
import network.connection.NetworkThreadFactory;
import integration.DatabaseFactory;
public final class Server {

    private final static Environment.EnvTool<String> stringEnvTool = new Environment.EnvTool<>(String.class);
    private final static Environment.EnvTool<Integer> integerEnvTool = new Environment.EnvTool<>(Integer.class);
    private final static Environment.EnvTool<Double> doubleEnvTool = new Environment.EnvTool<>(Double.class);

    public static void main(String[] args) {

        String socket_address = stringEnvTool.getOrDefault("SERVER_SOCKET_ADDRESS", "localhost");
        Integer socket_port = integerEnvTool.getOrDefault("SERVER_SOCKET_PORT", 22035);
        Integer socket_threads = integerEnvTool.getOrDefault("SERVER_SOCKET_THREADS", 1);

        String database_address = stringEnvTool.getOrDefault("SERVER_DB_ADDRESS", "localhost");
        Integer database_port = integerEnvTool.getOrDefault("SERVER_DB_PORT", 3306);

        String database_name = stringEnvTool.getOrDefault("SERVER_DB_NAME", "dionysus_local");
        String database_username = stringEnvTool.getOrDefault("SERVER_DB_USERNAME", "default");
        String database_password = stringEnvTool.getOrDefault("SERVER_DB_PASSWORD", "");

        Integer mutation_chance = integerEnvTool.getOrDefault("SERVER_MUTATOR_MUTATION_CHANCE", 25);
        Double mutation_factor = doubleEnvTool.getOrDefault("SERVER_MUTATOR_MUTATION_FACTOR", 0.05);
        Integer cross_chance = integerEnvTool.getOrDefault("SERVER_MUTATOR_CROSS_CHANCE", 10);
        Integer epoch_population = integerEnvTool.getOrDefault("SERVER_MUTATOR_EPOCH_REQUIREMENT", 100);
        Integer epoch_cull = integerEnvTool.getOrDefault("SERVER_MUTATOR_EPOCH_CULL", 75);


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
                new AgentModifier(
                        mutation_chance,
                        mutation_factor,
                        cross_chance,
                        epoch_population,
                        epoch_cull
                )
        );

    }
}
