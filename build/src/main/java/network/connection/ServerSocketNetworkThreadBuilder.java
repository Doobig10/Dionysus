package network.connection;

public class ServerSocketNetworkThreadBuilder {

    private int port;
    private int threadCount = 1;

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
