import java.io.BufferedOutputStream;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        final var server = new Server();
        // код инициализации сервера (из вашего предыдущего ДЗ)

        // добавление handler'ов (обработчиков)
        server.addHandler("GET", "/messages", (request, responseStream) -> {
            var response = "Hello from GET /message";
            responseStream.write((
                    "HTTP/1.1 200 OK\r\n" +
                            "Content-Type: " + request.getMimeType() + "\r\n" +
                            "Content-Length: " + response.length() + "\r\n" +
                            "Connection: close\r\n" +
                            "\r\n"
            ).getBytes());
            responseStream.write(response.getBytes());
            responseStream.flush();
        });
        server.addHandler("POST", "/messages", (request, responseStream) -> {
            var response = "Hello from POST /message";
            responseStream.write((
                    "HTTP/1.1 200 OK\r\n" +
                            "Content-Type: " + request.getMimeType() + "\r\n" +
                            "Content-Length: " + response.length() + "\r\n" +
                            "Connection: close\r\n" +
                            "\r\n"
            ).getBytes());
            responseStream.write(response.getBytes());
            responseStream.flush();
        });

        server.listen(9999);
    }
}
