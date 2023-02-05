import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUpload;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.RequestContext;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

public class Request implements ParseQueryParams, ParsePostParams{
    private final String method;
    private final String pathWithQueryParams;
    private final String versionHTTP;
    private final List<String> headers;
    private final String body;
    private List<NameValuePair> postParams;
    private List<NameValuePair> queryParams;
    private InputStream in;
    private final Map<String, FileItem> files;

    public Request(String method, String path, String versionHTTP, List<String> headers, String body){
        this.method = method;
        this.pathWithQueryParams = path;
        this.versionHTTP = versionHTTP;
        this.headers = headers;
        this.body = body;
        this.postParams = new ArrayList<>();
        this.queryParams = new ArrayList<>();
        files = new HashMap<>();
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

    public List<String> getHeaders(){
        return headers;
    }

    public void setInputStream(InputStream in) {
        this.in = in;
    }

    public void putFile(String name, FileItem item) {
        files.put(name, item);
    }

    public Set<String> getFilesName() {
        return files.keySet();
    }

    public FileItem getFile(String name) {
        return files.get(name);
    }

    @Override
    public List<NameValuePair> getQueryParams() throws URISyntaxException {
        if(queryParams.isEmpty()) {
            queryParams = URLEncodedUtils.parse(new URI(pathWithQueryParams), StandardCharsets.UTF_8);
        }
        return queryParams;
    }

    @Override
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

    public void putRequestParam(String name, String value) {
        if (getQueryParam(name).get(0).getName().equals(name)) {
            getQueryParam(name).add(new BasicNameValuePair(name, value));
        } else {
            queryParams.add(new BasicNameValuePair(name, value));
        }
    }

    public String getPathWithoutQueryParams(){
        String[] partsPath = pathWithQueryParams.split("\\?");
        return partsPath[0];
    }

    @Override
    public List<NameValuePair> getPostParam(String nameParam) {
        if(getPostParams().stream().anyMatch(valuePair -> valuePair.getName().equals(nameParam))){
            return getPostParams().stream()
                    .filter(valuePair -> valuePair.getName().equals(nameParam))
                    .collect(Collectors.toList());
        } else {
            System.out.println("В запросе не было такого query-парамтра");
            return null;
        }
    }

    @Override
    public List<NameValuePair> getPostParams() {
        if(postParams.isEmpty()){
            String type = " ";
            Optional<String> contentType = extractHeader(headers, "Content-Type");
            if(contentType.isPresent()){
                type = contentType.get();
            } else throw new IllegalArgumentException("Incorrect request");

            if(method.equals("POST") && type.equals("application/x-www-form-urlencoded")){
                postParams = URLEncodedUtils.parse(body, StandardCharsets.UTF_8);
            } else throw new IllegalArgumentException("Inappropriate data format");
        }
        return postParams;
    }

    public static Optional<String> extractHeader(List<String> headers, String header) {
        return headers.stream()
                .filter(o -> o.startsWith(header))
                .map(o -> o.substring(o.indexOf(" ")))
                .map(String::trim)
                .findFirst();

    }

    //TODO parse multipart request
//    @Override
//    public void parseMultipart() throws FileUploadException {
//        var list = new FileUpload(new DiskFileItemFactory()).parseRequest(this);
//        for (FileItem item : list){
//            if(item.getContentType() == null){
//                this.putRequestParam(item.getFieldName(), item.getString());
//            } else this.putFile(item.getName(), item);
//        }
//    }

//    @Override
//    public String getCharacterEncoding() {
//        return StandardCharsets.UTF_8.toString();
//    }
//
//    @Override
//    public String getContentType() {
//        return extractHeader(headers, "Content-Type").orElse(null);
//    }
//
//    @Override
//    public int getContentLength() {
//        return extractHeader(headers, "Content-Length").map(Integer::parseInt).orElse(0);
//    }
//
//    @Override
//    public InputStream getInputStream() {
//        return in;
//    }
}
