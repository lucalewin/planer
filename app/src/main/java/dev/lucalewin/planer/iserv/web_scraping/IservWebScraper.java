package dev.lucalewin.planer.iserv.web_scraping;

import android.content.Context;
import android.content.SharedPreferences;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import dev.lucalewin.lib.web.http.HttpMethod;
import dev.lucalewin.lib.web.http.WebClient;
import dev.lucalewin.lib.web.http.WebRequest;
import dev.lucalewin.lib.web.http.WebResponse;
import dev.lucalewin.planer.iserv.IservPlan;
import dev.lucalewin.planer.iserv.IservPlanRow;
import dev.lucalewin.planer.settings.IservAccountSettingsActivity;
import dev.lucalewin.planer.util.DateUtil;

public class IservWebScraper {

    private static String base_url;

    private static final WebClient webClient = new WebClient();

    public static boolean login(Context context) throws MalformedURLException, ExecutionException, InterruptedException {
        SharedPreferences sharedPreferences = context.getSharedPreferences(IservAccountSettingsActivity.ISERV_SP_NAME, Context.MODE_PRIVATE);

        base_url = sharedPreferences.getString("base_url", "");
        if (!base_url.startsWith("http://") && !base_url.startsWith("https://")) {
            base_url = "https://" + base_url;
        }

        String username = sharedPreferences.getString("username", "");
        String password = sharedPreferences.getString("password", "");

        final String login_url = base_url + "/iserv/app/login";

        final URL loginRequestUrl = new URL(login_url);
        final String loginRequestBody = String.format("_username=%s&_password=%s&_remember_me=on", username, password);

        WebRequest loginRequest = new WebRequest(loginRequestUrl, HttpMethod.POST);
        loginRequest.addHeaderField("content-type", "application/x-www-form-urlencoded");
        loginRequest.setBody(loginRequestBody);

        Future<WebResponse> responseFuture = webClient.makeRequestAsync(loginRequest);
        WebResponse response = responseFuture.get();

        return response.getResponseCode() == 302; // 302 = Found --> username and password were correct
    }

    private static String getTodayPlanerHtml(Context context) throws MalformedURLException, ExecutionException, InterruptedException {
        SharedPreferences sharedPreferences = context.getSharedPreferences(IservAccountSettingsActivity.ISERV_SP_NAME, Context.MODE_PRIVATE);

        base_url = sharedPreferences.getString("base_url", "");
        if (!base_url.startsWith("http://") && !base_url.startsWith("https://")) {
            base_url = "https://" + base_url;
        }

        final String plan_url = base_url + "/iserv/plan/show/raw/Vertretungsplan%20Sch%C3%BCler/subst_001.htm";

        WebRequest planerRequest = new WebRequest(new URL(plan_url), HttpMethod.GET);
        planerRequest.setResponseCharset(StandardCharsets.ISO_8859_1);

        Future<WebResponse> responseFuture = webClient.makeRequestAsync(planerRequest);
        WebResponse webResponse = responseFuture.get();

        return webResponse.getBody();
    }

    private static String getTomorrowPlanerHtml(Context context) throws MalformedURLException, ExecutionException, InterruptedException {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                IservAccountSettingsActivity.ISERV_SP_NAME, Context.MODE_PRIVATE);

        base_url = sharedPreferences.getString("base_url", "");
        if (!base_url.startsWith("http://") && !base_url.startsWith("https://")) {
            base_url = "https://" + base_url;
        }

        final String planerUrl = base_url + "/iserv/plan/show/raw/Vertretungsplan%20Sch%C3%BCler/subst_002.htm";

        WebRequest planerRequest = new WebRequest(new URL(planerUrl), HttpMethod.GET);
        planerRequest.setResponseCharset(StandardCharsets.ISO_8859_1);

        Future<WebResponse> responseFuture = webClient.makeRequestAsync(planerRequest);
        WebResponse response = responseFuture.get();

        return response.getBody();
    }

    public static IservPlan getAffectedData(Context context, int day) throws MalformedURLException, ExecutionException, InterruptedException {

        // parse html document
        Document root = day == 0 ? Jsoup.parse(getTodayPlanerHtml(context)) : Jsoup.parse(getTomorrowPlanerHtml(context));

        Element infoTable = root.getElementsByClass("info").first();

        assert infoTable != null;
        Elements rows = infoTable.selectXpath("/table/tbody/tr");

        String dayOfTheWeek = Objects.requireNonNull(root.getElementsByClass("mon_title").first()).text().split(" ")[1];

        if (rows.size() < 2) {
            return new IservPlan(DateUtil.parseDayOfWeek(dayOfTheWeek, Locale.GERMAN), null);
        }

        final Map<String, List<IservPlanRow>> classes = new HashMap<>();

        // parse planer
        Element planerTable = root.getElementsByClass("mon_list").first();

        assert planerTable != null;
        Elements planerTableRows = planerTable.selectXpath("/table/tbody/tr");

        List<IservPlanRow> current;

        for (int i = 0; i < planerTableRows.size(); i++) {
            Element planerRow = planerTableRows.get(i);

            Element firstColumn = planerRow.children().first();
            assert firstColumn != null;
            String currentClass = firstColumn.text();

            current = new ArrayList<>();

            for (++i; i < planerTableRows.size(); i++) {
                Element currentPlanerRow = planerTableRows.get(i);

                if (currentPlanerRow.children().size() == 1) {
                    i--;
                    break;
                }

                // loop through all columns of current row
                current.add(new IservPlanRow(
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
            classes.put(currentClass, current);
        }
        return new IservPlan(DateUtil.parseDayOfWeek(dayOfTheWeek, Locale.GERMAN), classes);
    }
}
