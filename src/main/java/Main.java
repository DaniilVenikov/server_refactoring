import java.io.BufferedOutputStream;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        final var server = new Server();
        // код инициализации сервера (из вашего предыдущего ДЗ)

        // добавление handler'ов (обработчиков)
        server.addHandler("GET", "/messages", (request, responseStream) -> {
            var response = "Hello from GET /message";
            response(responseStream, response);
        });
        server.addHandler("POST", "/messages", (request, responseStream) -> {
            var response = "Hello from POST /message";
            response(responseStream, response);
        });
        server.listen(9999);
    }

    public static void response(BufferedOutputStream responseStream, String response) throws IOException {
        responseStream.write((
                "HTTP/1.1 200 OK\r\n" +
                        "Content-Length: " + response.length() + "\r\n" +
                        "Connection: close\r\n" +
                        "\r\n"
        ).getBytes());
        responseStream.write(response.getBytes());
        responseStream.flush();
    }
}
