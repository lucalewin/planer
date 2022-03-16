package dev.lucalewin.lib.web.http;

import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class WebRequest {

    private final URL url;
    private HttpMethod method = HttpMethod.GET;
    private int connectionTimeout = 5000; // 5 sec
    private String body;
    private final Map<String, String> headerFields = new HashMap<>();
    private Charset requestCharset = StandardCharsets.UTF_8;
    private Charset responseCharset = StandardCharsets.UTF_8;

    // constructor

    public WebRequest(URL url) {
        this.url = url;
    }

    public WebRequest(URL url, HttpMethod method) {
        this.url = url;
        this.method = method;
    }

    // Getter

    public URL getUrl() {
        return url;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    public String getBody() {
        return body;
    }

    public Map<String, String> getHeaderFields() {
        return headerFields;
    }

    public Charset getRequestCharset() {
        return requestCharset;
    }

    public Charset getResponseCharset() {
        return responseCharset;
    }

    // Setter

    public void setMethod(HttpMethod method) {
        this.method = method;
    }

    public void addHeaderField(String key, String value) {
        headerFields.put(key, value);
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public void setRequestCharset(Charset requestCharset) {
        this.requestCharset = requestCharset;
    }

    public void setResponseCharset(Charset responseCharset) {
        this.responseCharset = responseCharset;
    }
}
