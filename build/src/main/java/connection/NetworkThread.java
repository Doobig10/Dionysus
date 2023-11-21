package connection;

import connection.exceptions.AlreadyBoundException;

public interface NetworkThread extends Runnable {

    void bind(NetworkMaster master) throws AlreadyBoundException;

    boolean isBound();

    void start();
}
