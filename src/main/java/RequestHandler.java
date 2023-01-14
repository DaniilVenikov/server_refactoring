import java.io.BufferedOutputStream;
import java.io.IOException;

interface RequestHandler{
    void send404NotFound(BufferedOutputStream out) throws IOException;
    void send500InternalServerError(BufferedOutputStream out) throws IOException;
    void badRequest(BufferedOutputStream out) throws IOException;
    void response(BufferedOutputStream out, long length) throws IOException;
}
