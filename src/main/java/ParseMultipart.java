import org.apache.commons.fileupload.FileUploadException;
import org.apache.http.HttpRequest;
import org.apache.http.NameValuePair;

import java.util.List;

public interface ParseMultipart {
    void parseMultipart() throws FileUploadException;
}
