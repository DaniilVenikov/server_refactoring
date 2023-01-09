public class Request {
    private final String method;
    private final String path;
    private final String versionHTTP;
    private final String mimeType;
    private final long length;
    private byte[] body;

    public Request(String method, String path, String versionHTTP, String mimeType, long length, byte[] body){
        this.method = method;
        this.path = path;
        this.versionHTTP = versionHTTP;
        this.mimeType = mimeType;
        this.length = length;
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

    public String getMimeType() {
        return mimeType;
    }

    public long getLength() {
        return length;
    }

    public byte[] getBody() {
        return body;
    }
}
