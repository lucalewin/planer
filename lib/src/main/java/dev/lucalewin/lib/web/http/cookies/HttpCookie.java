package dev.lucalewin.lib.web.http.cookies;

public class HttpCookie {

    private final String name;
    private final String value;

    public static HttpCookie parse(String cookie) {
        // FIXME: make sure all cookies can be parsed with this approach
        String name, value;
        String[] split = cookie.split(";");
        name = split[0].split("=")[0];
        value = split[0].split("=")[1];
        return new HttpCookie(name, value);
    }

    public HttpCookie(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }
}
