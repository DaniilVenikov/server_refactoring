import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionHandler {
    public static final String GET = "GET";
    public static final String POST = "POST";
    final Socket clientSocket;
    final RequestHandlerImpl requestHandler;
    final ConcurrentHashMap<String, ConcurrentHashMap<String, Handler>> requestHandlersMap;

    public ConnectionHandler(Socket clientSocket, ConcurrentHashMap<String, ConcurrentHashMap<String, Handler>> requestHandlersMap) {
        this.clientSocket = clientSocket;
        this.requestHandlersMap = requestHandlersMap;
        this.requestHandler = new RequestHandlerImpl();
    }



    public void handle() {
        try {
            System.out.println(Thread.currentThread().getName());

            final BufferedOutputStream out = new BufferedOutputStream(clientSocket.getOutputStream());
            final BufferedInputStream in = new BufferedInputStream(clientSocket.getInputStream());
            final var allowedMethods = List.of(GET, POST);

            Request request;
            RequestBuilder requestBuilder = new RequestBuilder();

            final var limit = 4096;

            in.mark(limit);
            final var buffer = new byte[limit];
            final var read = in.read(buffer);

            final var requestLineDelimiter = new byte[]{'\r', '\n'};
            final var requestLineEnd = indexOf(buffer, requestLineDelimiter, 0, read);

            if(requestLineEnd == -1){
                requestHandler.badRequest(out);
                return;
            }

            final var requestLine = new String(Arrays.copyOf(buffer, requestLineEnd)).split(" ");
            if (requestLine.length != 3){
                requestHandler.badRequest(out);
                return;
            }

            final var method = requestLine[0];
            if (!allowedMethods.contains(method)){
                requestHandler.badRequest(out);
                return;
            }
            System.out.println(method);

            final var pathWithQueryParams = requestLine[1];
            if(!pathWithQueryParams.startsWith("/")){
                requestHandler.badRequest(out);
                return;
            }
            System.out.println(pathWithQueryParams);

            final var versionProtocol = requestLine[2];
            System.out.println(versionProtocol);


            requestBuilder
                    .setMethod(method)
                    .setPath(pathWithQueryParams)
                    .setVersionHTTP(versionProtocol);

            final var headersDelimiter = new byte[]{'\r', '\n', '\r', '\n'};
            final var headersStart = requestLineEnd + requestLineDelimiter.length;
            final var headersEnd = indexOf(buffer, headersDelimiter, headersStart, read);
            if (headersEnd == -1) {
                requestHandler.badRequest(out);
                return;
            }

            in.reset();
            in.skip(headersStart);

            final var headersBytes = in.readNBytes(headersEnd - headersStart);
            final var headers = Arrays.asList(new String(headersBytes).split("\r\n"));
            requestBuilder.setHeaders(headers);
            System.out.println(headers);

            if (!method.equals(GET)) {
                in.skip(headersDelimiter.length);
                // вычитываем Content-Length, чтобы прочитать body
                final var contentLength = Request.extractHeader(headers, "Content-Length");
                if (contentLength.isPresent()) {
                    final var length = Integer.parseInt(contentLength.get());
                    final var bodyBytes = in.readNBytes(length);

                    final String body = new String(bodyBytes);
                    requestBuilder.setBody(body);

                    System.out.println(body);
                }
            }

            request = requestBuilder.build();


            if(!requestHandlersMap.containsKey(request.getMethod())){
                requestHandler.send404NotFound(out);
                return;
            }

            var pathHandlers = requestHandlersMap.get(request.getMethod());
            if(!pathHandlers.containsKey(request.getPathWithoutQueryParams())){
                requestHandler.send404NotFound(out);
                return;
            }

            Handler handler = pathHandlers.get(request.getPathWithoutQueryParams());

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

    private static int indexOf(byte[] array, byte[] target, int start, int max) {
        outer:
        for (int i = start; i < max - target.length + 1; i++) {
            for (int j = 0; j < target.length; j++) {
                if (array[i + j] != target[j]) {
                    continue outer;
                }
            }
            return i;
        }
        return -1;
    }
}
