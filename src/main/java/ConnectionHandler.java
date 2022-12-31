import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;

public class ConnectionHandler implements Runnable{
    final Socket clientSocket;
    final RequestHandlerImpl requestHandler;
    final List<String> validPaths = List.of("/index.html", "/spring.svg", "/spring.png", "/resources.html", "/styles.css", "/app.js", "/links.html", "/forms.html", "/classic.html", "/events.html", "/events.js");

    public ConnectionHandler(Socket clientSocket){
        this.clientSocket = clientSocket;
        this.requestHandler = new RequestHandlerImpl();
    }


    @Override
    public void run() {
        while (true) {
            try (BufferedOutputStream out = new BufferedOutputStream(clientSocket.getOutputStream());
                 BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
                final var requestLine = in.readLine();
                final var parts = requestLine.split(" ");

                if (parts.length != 3) {
                    // just close socket
                    continue;
                }

                final var path = parts[1];
                if (!requestHandler.isValidPath(out,validPaths,path)) {
                    continue;
                }

                final var filePath = Path.of(".", "public", path);
                final var mimeType = Files.probeContentType(filePath);

                // special case for classic
                if (path.equals("/classic.html")) {
                    final var template = Files.readString(filePath);
                    final var content = template.replace(
                            "{time}",
                            LocalDateTime.now().toString()
                    ).getBytes();
                    requestHandler.response(out, mimeType, content.length);
                    out.write(content);
                    out.flush();
                    continue;
                }

                final var length = Files.size(filePath);
                requestHandler.response(out,mimeType, length);
                Files.copy(filePath, out);
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
