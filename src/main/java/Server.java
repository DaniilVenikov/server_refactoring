import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.Executors;

public class Server {
    final static int THREADS_QUANTITY = 64;
    private int port = 9999;

    public void start() {
        final var executor = Executors.newFixedThreadPool(THREADS_QUANTITY);

        try (final var serverSocket = new ServerSocket(port)) {
            while (true) {
                final var clientSocket = serverSocket.accept();
                executor.submit(new ConnectionHandler(clientSocket));

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        executor.shutdown();
    }

    public void setPort(int newPort){
        port = newPort;
    }
}
