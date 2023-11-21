import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class DummyConnection {

    public static void main(String[] args)
            throws IOException {
        Socket clientSocket = new Socket("127.0.0.1", 22035);
        PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);
        BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        writer.println("testMessage");
    }

}
