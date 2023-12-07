package network.connection;

public class ServerSocketNetworkThreadBuilder {

    private String address = "localhost";
    private int port;
    private int threadCount = 1;

    public ServerSocketNetworkThreadBuilder setAddress(String address) {
        this.address = address;
        return this;
    }

    public ServerSocketNetworkThreadBuilder setPort(int port) {
        this.port = port;
        return this;
    }

    public ServerSocketNetworkThreadBuilder setThreadCount(int threadCount) {
        this.threadCount = threadCount;
        return this;
    }

    public int getPort() {
        return this.port;
    }

    public int getThreadCount() {
        return this.threadCount;
    }

    public ServerSocketNetworkThread build() {
        return new ServerSocketNetworkThread(this);
    }
}
