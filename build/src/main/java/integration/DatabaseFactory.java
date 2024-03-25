package integration;

public class DatabaseFactory {
    public static MariaDatabaseBuilder getMariaBuilder() {
        return new MariaDatabaseBuilder();
    }
}
