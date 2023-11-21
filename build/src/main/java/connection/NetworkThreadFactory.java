package connection;

public final class NetworkThreadFactory {

    public static ServerSocketNetworkThreadBuilder getServerSocketBuilder() {return new ServerSocketNetworkThreadBuilder();}
}
