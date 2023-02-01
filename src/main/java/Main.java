import org.apache.http.NameValuePair;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        final var server = new Server();

        server.addHandler("GET", "/messages", (request, responseStream) -> {
            var response = "Hello from GET /message";
            response(responseStream, response);
        });
        server.addHandler("POST", "/messages", (request, responseStream) -> {
            var response = "Hello from POST /message";
            response(responseStream, response);
        });
        server.addHandler("POST", "/", (request, responseStream) ->{
            List<NameValuePair> params = request.getPostParams();
            String response = " ";
            if (params.isEmpty()) {
                response = "Inappropriate data format";
            } else {
                response = "Fields you entered: " + params;
            }
            System.out.println(response);
            response(responseStream, response);
        });
        server.addHandler("POST", "/multipart", (request, responseStream) ->{
            request.parseMultipart();
            response(responseStream, "Hello from multipart");
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
