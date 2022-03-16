package dev.lucalewin.lib.web.http;

public class WebResponse {

    private final String body;
    private final int responseCode;

    public WebResponse(String body, int responseCode) {
        this.body = body;
        this.responseCode = responseCode;
    }

    public String getBody() {
        return body;
    }

    public int getResponseCode() {
        return responseCode;
    }
}
