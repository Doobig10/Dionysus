package integration;

public class MariaDatabaseBuilder {
    String address = "localhost";
    Integer port = 3306;
    String database;

    public MariaDatabaseBuilder setAddress(String address) {
        this.address = address;
        return this;
    }

    public MariaDatabaseBuilder setPort(Integer port) {
        this.port = port;
        return this;
    }

    public MariaDatabaseBuilder setDatabase(String database) {
        this.database = database;
        return this;
    }

    public MariaDatabase build() {
        return new MariaDatabase(this);
    }
}
