import org.apache.http.NameValuePair;

import java.net.URISyntaxException;
import java.util.List;

public interface ParseQueryParams {
    List<NameValuePair> getQueryParams() throws URISyntaxException;
    List<NameValuePair> getQueryParam(String nameParam);
}
