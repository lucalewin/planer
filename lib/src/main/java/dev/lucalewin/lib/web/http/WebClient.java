package dev.lucalewin.lib.web.http;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import dev.lucalewin.lib.exceptions.NotImplementedException;
import dev.lucalewin.lib.web.html.HtmlPage;
import dev.lucalewin.lib.web.http.cookies.CookieStore;
import dev.lucalewin.lib.web.http.cookies.HttpCookie;

public class WebClient {

    private static final ExecutorService EXECUTOR_SERVICE = Executors.newSingleThreadExecutor();

    private Options options;
    private final CookieStore cookieStore = new CookieStore();

    public static WebClient load(Context context, String id) {
        return null;
    }

    public HtmlPage getPage(WebRequest request) {
        return null;
    }

    public WebResponse makeRequest(WebRequest request) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) request.getUrl().openConnection();

        // add additional header fields
        request.getHeaderFields().forEach(connection::setRequestProperty);
        // add saved cookies
        if (cookieStore.containsHost(request.getUrl().getHost())) {
            List<HttpCookie> savedCookies = cookieStore.getCookies(request.getUrl().getHost());
            String headerCookieValue = savedCookies.stream()
                    .map(cookie -> cookie.getName() + "=" + cookie.getValue())
                    .collect(Collectors.joining(";"));
            connection.setRequestProperty("cookie", headerCookieValue);
        }

        connection.setRequestMethod(request.getMethod().name());
        connection.setDoOutput(request.getBody() != null);
        connection.setInstanceFollowRedirects(false);
        connection.connect();

        // send request body
        if (request.getBody() != null) {
            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream(), request.getRequestCharset());
            writer.write(request.getBody());
            writer.flush();
            writer.close();
        }

        // read response body
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), request.getResponseCharset()));
        StringBuilder responseBodyStringBuilder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            responseBodyStringBuilder.append(line);
        }
        reader.close();

        connection.disconnect();

        // parse cookies
        List<String> headerCookieField = connection.getHeaderFields().get("set-cookie");
        if (headerCookieField != null) {
            List<HttpCookie> cookies = headerCookieField
                    .stream()
                    .map(HttpCookie::parse)
                    .collect(Collectors.toList());

            // save cookies to cookie-store
            cookieStore.addCookies(request.getUrl().getHost(), cookies);
        }

        return new WebResponse(responseBodyStringBuilder.toString(), connection.getResponseCode());
    }

    public Future<WebResponse> makeRequestAsync(WebRequest request) {
        return EXECUTOR_SERVICE.submit(() -> makeRequest(request));
    }

    public void save(Context context) throws NotImplementedException {
        throw new NotImplementedException();
    }

    public Options getOptions() {
        return options;
    }

    public static class Options {

    }

}
