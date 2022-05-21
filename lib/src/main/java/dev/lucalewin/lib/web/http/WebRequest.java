package dev.lucalewin.lib.web.http;

import java.net.MalformedURLException;
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

    public static WebRequestBuilder builder(String url) throws MalformedURLException {
        return new WebRequestBuilder(url);
    }

    public static class WebRequestBuilder {

        private final WebRequest req;

        public WebRequestBuilder(String url) throws MalformedURLException {
            req = new WebRequest(new URL(url));
        }

        public WebRequestBuilder setMethod(HttpMethod method) {
            req.setMethod(method);
            return this;
        }

        public WebRequestBuilder addHeaderField(String key, String value) {
            req.addHeaderField(key, value);
            return this;
        }

        public WebRequestBuilder setBody(String body) {
            req.setBody(body);
            return this;
        }

        public WebRequestBuilder setConnectionTimeout(int connectionTimeout) {
            req.setConnectionTimeout(connectionTimeout);
            return this;
        }

        public WebRequestBuilder setRequestCharset(Charset charset) {
            req.setRequestCharset(charset);
            return this;
        }

        public WebRequestBuilder setResponseCharset(Charset charset) {
            req.setResponseCharset(charset);
            return this;
        }

        public WebRequest build() {
            return req;
        }

    }
}
