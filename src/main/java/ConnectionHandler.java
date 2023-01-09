import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionHandler {
    final Socket clientSocket;
    final RequestHandlerImpl requestHandler;
    final ConcurrentHashMap<String, ConcurrentHashMap<String, Handler>> requestHandlersMap;
    final List<String> validPaths = List.of("/index.html", "/spring.svg", "/spring.png", "/resources.html", "/styles.css", "/app.js", "/links.html", "/forms.html", "/classic.html", "/events.html", "/events.js");

    public ConnectionHandler(Socket clientSocket, ConcurrentHashMap<String, ConcurrentHashMap<String, Handler>> requestHandlersMap) {
        this.clientSocket = clientSocket;
        this.requestHandlersMap = requestHandlersMap;
        this.requestHandler = new RequestHandlerImpl();
    }


    public void handle() {
        try {
            System.out.println(Thread.currentThread().getName());

            final BufferedOutputStream out = new BufferedOutputStream(clientSocket.getOutputStream());
            final BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            Request request;
            RequestBuilder requestBuilder = new RequestBuilder();

            final var requestLine = in.readLine();
            final var parts = requestLine.split(" ");

            if (parts.length != 3) {
                // just close socket
                return;
            }

            requestBuilder
                    .setMethod(parts[0])
                    .setPath(parts[1])
                    .setVersionHTTP(parts[2]);

//            final var path = parts[1];
//            if (!validPaths.contains(path)) {
//                requestHandler.send404NotFound(out);
//                return;
//            }
//
//            final var filePath = Path.of(".", "public", path);
//            final var mimeType = Files.probeContentType(filePath);

            //requestBuilder.setMimeType(mimeType);
            requestBuilder.setMimeType("text");

            // special case for classic
            //TODO добавить этот обработчик
//            if (path.equals("/classic.html")) {
//                final var template = Files.readString(filePath);
//                final var content = template.replace(
//                        "{time}",
//                        LocalDateTime.now().toString()
//                ).getBytes();
//                request = requestBuilder
//                        .setLength(content.length)
//                        .build();
//                requestHandler.response(out, mimeType, content.length);
//                out.write(content);
//                out.flush();
//                return;
//            }

            //final var length = Files.size(filePath);
            request = requestBuilder
                    .setLength(80)
                    //.setLength(length)
                    .build();


            if(!requestHandlersMap.containsKey(request.getMethod())){
                requestHandler.send404NotFound(out);
                return;
            }

            var pathHandlers = requestHandlersMap.get(request.getMethod());

            if(!pathHandlers.containsKey(request.getPath())){
                requestHandler.send404NotFound(out);
                return;
            }

            Handler handler = pathHandlers.get(request.getPath());

            try {
                handler.handle(request, out);
            } catch (Exception e){
                System.out.println(e.getMessage());
                requestHandler.send500InternalServerError(out);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
