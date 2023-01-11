import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;

public class Server {
    final static int THREADS_QUANTITY = 64;
    private final ConcurrentHashMap<String, ConcurrentHashMap<String, Handler>> requestHandlersMap = new ConcurrentHashMap<>();

    public void listen(int port) {
        final var executor = Executors.newFixedThreadPool(THREADS_QUANTITY);

        try (final var serverSocket = new ServerSocket(port)) {
            System.out.println("Server started");
            while (true) {
                final var clientSocket = serverSocket.accept();
                executor.submit(() -> new ConnectionHandler(clientSocket, requestHandlersMap).handle());

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        executor.shutdown();
    }

    public void addHandler(String method, String path, Handler handler){
        if(!requestHandlersMap.containsKey(method)){
            requestHandlersMap.put(method, new ConcurrentHashMap<>());
        }

        requestHandlersMap.get(method).put(path, handler);
    }
}
