import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Request {
    private final String method;
    private final String pathWithQueryParams;
    private final String versionHTTP;
    private final List<String> headers;
    private String body;
    private List<NameValuePair> queryParams;

    public Request(String method, String path, String versionHTTP, List<String> headers, String body){
        this.method = method;
        this.pathWithQueryParams = path;
        this.versionHTTP = versionHTTP;
        this.headers = headers;
        this.body = body;
        this.queryParams = new ArrayList<>();
    }


    public String getMethod() {
        return method;
    }

    public String getPathWithQueryParams() {
        return pathWithQueryParams;
    }

    public String getVersionHTTP() {
        return versionHTTP;
    }

    public String getBody() {
        return body;
    }

    public List<NameValuePair> getQueryParams() throws URISyntaxException {
        if(queryParams.isEmpty()) {
            queryParams = URLEncodedUtils.parse(new URI(pathWithQueryParams), StandardCharsets.UTF_8);
        }
        return queryParams;
    }

    public List<NameValuePair> getQueryParam(String nameParam){
        if(queryParams.isEmpty()) {
            System.out.println("В запросе не было query-параметров");
            return null;
        }
        if(queryParams.stream().anyMatch(valuePair -> valuePair.getName().equals(nameParam))){
           return queryParams.stream()
                   .filter(valuePair -> valuePair.getName().equals(nameParam))
                   .collect(Collectors.toList());
        } else {
            System.out.println("В запросе не было такого query-парамтра");
            return null;
        }
    }

    public String getPathWithoutQueryParams(){
        String[] partsPath = pathWithQueryParams.split("\\?");
        return partsPath[0];
    }
}
