package dev.lucalewin.planer.iserv.web_scraping;

import android.content.Context;
import android.content.SharedPreferences;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

import dev.lucalewin.planer.iserv.IservPlanRow;
import dev.lucalewin.planer.IservAccountSettingsActivity;

public class IservWebScraper {

    private static final TaskRunner taskRunner = new TaskRunner();
    private static final Executor executor = Executors.newSingleThreadExecutor();
    private static String base_url;

    public static boolean isLoggedIn = false;
    private static List<HttpCookie> cookies = new ArrayList<>();

    public static void login(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                IservAccountSettingsActivity.ISERV_SP_NAME, Context.MODE_PRIVATE);

        base_url = sharedPreferences.getString("base_url", "");

        String username = sharedPreferences.getString("username", "");
        String password = sharedPreferences.getString("password", "");

        final String login_url = base_url + "/iserv/app/login";

        taskRunner.executeAsync(() -> {
            URL url = new URL(login_url);
            HttpURLConnection uc = (HttpURLConnection) url.openConnection();

            uc.setRequestProperty("content-type", "application/x-www-form-urlencoded");
            uc.setRequestMethod("POST");
            uc.setDoInput(true);
            uc.setInstanceFollowRedirects(false);
            uc.connect();
            OutputStreamWriter writer = new OutputStreamWriter(uc.getOutputStream(), StandardCharsets.UTF_8);
            writer.write(String.format("_username=%s&_password=%s&_remember_me=on", username, password));
            writer.close();
            BufferedReader br = new BufferedReader(new InputStreamReader(uc.getInputStream()));
            br.close();
            uc.disconnect();

            for (String cookieStr : Objects.requireNonNull(uc.getHeaderFields().get("set-cookie"))) {
                String s = String.join(";", Objects.requireNonNull(uc.getHeaderFields().get("set-cookie")));
                List<HttpCookie> cookies1 = HttpCookie.parse(cookieStr);
            }

            cookies = new ArrayList<>();

            for (String cookie : Objects.requireNonNull(uc.getHeaderFields().get("set-cookie"))) {
                cookies.add(HttpCookie.parse(cookie).get(0));
            }

            isLoggedIn = !cookies.isEmpty();

            return null;
        });
    }

    private static String getTodayPlanerHtml(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                IservAccountSettingsActivity.ISERV_SP_NAME, Context.MODE_PRIVATE);

        base_url = sharedPreferences.getString("base_url", "");

        final String login_url = base_url + "/iserv/plan/show/raw/Vertretungsplan%20Sch%C3%BCler/subst_001.htm";

        AtomicReference<String> response = new AtomicReference<>("null");

        taskRunner.executeAsync(() -> {
            URL url = new URL(login_url);
            HttpURLConnection uc = (HttpURLConnection) url.openConnection();

            StringBuilder cookieHeaderValue = new StringBuilder();
            for (HttpCookie cookie : cookies) {
                cookieHeaderValue.append(cookie.getName()).append("=").append(cookie.getValue()).append("; ");
            }

            String cookie = cookieHeaderValue.substring(0, cookieHeaderValue.length() - 2);
            uc.setRequestProperty("cookie", cookie);
            uc.setRequestMethod("GET");
            uc.setDoInput(true);
            uc.setInstanceFollowRedirects(false);
            uc.connect();
            BufferedReader br = new BufferedReader(new InputStreamReader(uc.getInputStream(), StandardCharsets.ISO_8859_1));
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                builder.append(line);
            }
            br.close();
            uc.disconnect();

            response.set(builder.toString());

            return null;
        });

        // wait for request to finish
        while (response.get().equals("null"));

        return response.get();
    }

    private static String getTomorrowPlanerHtml(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                IservAccountSettingsActivity.ISERV_SP_NAME, Context.MODE_PRIVATE);

        base_url = sharedPreferences.getString("base_url", "");

        final String login_url = base_url + "/iserv/plan/show/raw/Vertretungsplan%20Sch%C3%BCler/subst_002.htm";

        AtomicReference<String> response = new AtomicReference<>("null");

        taskRunner.executeAsync(() -> {
            URL url = new URL(login_url);
            HttpURLConnection uc = (HttpURLConnection) url.openConnection();

            StringBuilder cookieHeaderValue = new StringBuilder();
            for (HttpCookie cookie : cookies) {
                cookieHeaderValue.append(cookie.getName()).append("=").append(cookie.getValue()).append("; ");
            }

            String cookie = cookieHeaderValue.substring(0, cookieHeaderValue.length() - 2);
            uc.setRequestProperty("cookie", cookie);
            uc.setRequestMethod("GET");
            uc.setDoInput(true);
            uc.setInstanceFollowRedirects(false);
            uc.connect();
            BufferedReader br = new BufferedReader(new InputStreamReader(uc.getInputStream(), StandardCharsets.ISO_8859_1));
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                builder.append(line);
            }
            br.close();
            uc.disconnect();

            response.set(builder.toString());

            return null;
        });

        // wait for request to finish
        while (response.get().equals("null"));

        return response.get();
    }

    public static List<IservPlanRow> getAffectedData(Context context, int day) {

        String _class = "Q1";


        // parse html document
        Document root = day == 0 ? Jsoup.parse(getTodayPlanerHtml(context)) : Jsoup.parse(getTomorrowPlanerHtml(context));

        Element infoTable = root.getElementsByClass("info").first();

        assert infoTable != null;
        Elements rows = infoTable.selectXpath("/table/tbody/tr");

        if (rows.size() < 2) {
            return new ArrayList<>();
        }

//        StringBuilder data = new StringBuilder();
        List<IservPlanRow> data = new ArrayList<>();

        // parse info table
        for (Element row : rows) {
            if (Objects.requireNonNull(row.getAllElements().first()).text().contains("Betroffene Klassen")) {
                String allAffectedClasses = row.getAllElements().get(2).text();

                if (allAffectedClasses.contains(_class)) {

                    // parse planer
                    Element planerTable = root.getElementsByClass("mon_list").first();

                    assert planerTable != null;
                    Elements planerTableRows = planerTable.selectXpath("/table/tbody/tr");

                    outer_loop:
                    {
                        for (int i = 0; i < planerTableRows.size(); i++) {
                            Element planerRow = planerTableRows.get(i);

                            Element firstColumn = planerRow.children().first();
                            assert firstColumn != null;
                            String title = firstColumn.text();

                            if (title.equals(_class)) {
                                for (int j = i + 1; j < planerTableRows.size(); j++) {
                                    Element currentPlanerRow = planerTableRows.get(j);

                                    int size = currentPlanerRow.children().size();

                                    if (currentPlanerRow.children().size() == 1) {
                                        break outer_loop;
                                    }

                                    // loop through all columns of current row
//                                    data.add(currentPlanerRow.text());
                                    data.add(new IservPlanRow(
                                            currentPlanerRow.children().get(0).text(),
                                            currentPlanerRow.children().get(1).text(),
                                            currentPlanerRow.children().get(2).text(),
                                            currentPlanerRow.children().get(3).text(),
                                            currentPlanerRow.children().get(4).text(),
                                            currentPlanerRow.children().get(5).text(),
                                            currentPlanerRow.children().get(6).text(),
                                            currentPlanerRow.children().get(7).text(),
                                            currentPlanerRow.children().get(8).text()
                                    ));
                                }
                            }
                        }
                    }
                }
            }
        }

        return data;
    }

}
