public class RequestBuilder implements IRequestBuilder{
    private  String method;
    private  String path;
    private  String versionHTTP;
    private  String mimeType;
    private  Long length;
    private byte[] body;

    public RequestBuilder setMimeType(String mimeType) {
        this.mimeType = mimeType;
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

    public RequestBuilder setLength(long length) {
        this.length = length;
        return this;
    }

    public RequestBuilder setBod(byte[] body) {
        this.body = body;
        return this;
    }

    @Override
    public Request build() {
//        if ((!method.equals("GET")) && (path.isEmpty() || versionHTTP.isEmpty() || mimeType.isEmpty()
//                || length == null || body == null)) {
//            throw new IllegalArgumentException("Недостаточно аргументов для создания запроса");
//        } else if (path.isEmpty() || versionHTTP.isEmpty() || mimeType.isEmpty() || length == null){
//            throw new IllegalArgumentException("Недостаточно аргументов для создания запроса");
//        }


        if (path.isEmpty() || versionHTTP.isEmpty() || mimeType.isEmpty() || length == null){
            throw new IllegalArgumentException("Недостаточно аргументов для создания запроса");
        }

        return new Request(method, path, versionHTTP, mimeType, length, body);
    }
}
