import org.apache.http.NameValuePair;

import java.util.List;

public interface ParsePostParams {
    List<NameValuePair> getPostParam(String param);
    List<NameValuePair> getPostParams();
}
