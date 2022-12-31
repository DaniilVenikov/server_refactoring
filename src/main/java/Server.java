import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

        public void start() {
            final var executor = Executors.newFixedThreadPool(64);
            try (final var serverSocket = new ServerSocket(9999)) {
                while (true) {
                    try (
                            final var socket = serverSocket.accept();
                    ) {
                        // read only request line for simplicity
                        // must be in form GET /path HTTP/1.1
                        executor.execute(new ConnectionHandler(socket));

                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            executor.shutdown();
        }
}
