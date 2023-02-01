import org.apache.commons.fileupload.FileUploadException;

import java.io.BufferedOutputStream;
import java.io.IOException;

@FunctionalInterface
public interface Handler {
    void handle(Request request, BufferedOutputStream responseStream) throws IOException, FileUploadException;
}
