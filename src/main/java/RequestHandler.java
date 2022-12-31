import java.io.BufferedOutputStream;
import java.io.IOException;
import java.util.List;

interface RequestHandler{
    boolean isValidPath(BufferedOutputStream out, List<String> listPath, String path) throws IOException;
    void response(BufferedOutputStream out, String mimeType, long length) throws IOException;
}
