import java.util.List;

public class RequestBuilder implements IRequestBuilder{
    private  String method;
    private  String path;
    private  String versionHTTP;
    private List<String> headers;
    private String body;

    public RequestBuilder setHeaders(List<String> headers) {
        this.headers = headers;
        return this;
    }


    public RequestBuilder setMethod(String method) {
        this.method = method;
        return this;
    }

    public RequestBuilder setPath(String path) {
        this.path = path;
        return this;
    }

    public RequestBuilder setVersionHTTP(String versionHTTP) {
        this.versionHTTP = versionHTTP;
        return this;
    }

    public RequestBuilder setBody(String body) {
        this.body = body;
        return this;
    }

    @Override
    public Request build() {
        if ((!method.equals("GET")) && (path.isEmpty() || versionHTTP.isEmpty()
                || headers == null || body == null)) {
            throw new IllegalArgumentException("Недостаточно аргументов для создания запроса");
        } else if (path.isEmpty() || versionHTTP.isEmpty() || headers == null){
            throw new IllegalArgumentException("Недостаточно аргументов для создания запроса");
        }


//        if (path.isEmpty() || versionHTTP.isEmpty() || headers == null){
//            throw new IllegalArgumentException("Недостаточно аргументов для создания запроса");
//        }

        return new Request(method, path, versionHTTP, headers, body);
    }
}
