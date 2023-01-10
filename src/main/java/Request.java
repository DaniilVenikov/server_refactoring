import java.util.List;

public class Request {
    private final String method;
    private final String path;
    private final String versionHTTP;
    private final List<String> headers;
    private String body;

    public Request(String method, String path, String versionHTTP, List<String> headers, String body){
        this.method = method;
        this.path = path;
        this.versionHTTP = versionHTTP;
        this.headers = headers;
        this.body = body;
    }


    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public String getVersionHTTP() {
        return versionHTTP;
    }

    public String getBody() {
        return body;
    }
}
