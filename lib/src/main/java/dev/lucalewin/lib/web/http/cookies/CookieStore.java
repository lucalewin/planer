package dev.lucalewin.lib.web.http.cookies;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CookieStore {

    private final Map<String, List<HttpCookie>> cookies = new HashMap<>();

    public void addCookie(String host, HttpCookie newCookie) {
        addCookies(host, new ArrayList<HttpCookie>() {{ add(newCookie); }});
    }

    public void addCookies(String host, List<HttpCookie> newCookies) {
        List<HttpCookie> cookies = this.cookies.get(host);
        if (cookies == null) {
            this.cookies.put(host, newCookies);
        } else {
            cookies.addAll(newCookies);
        }
    }

    public List<HttpCookie> getCookies(String host) {
        return this.cookies.get(host);
    }

    public boolean containsHost(String host) {
        return cookies.containsKey(host);
    }

}
