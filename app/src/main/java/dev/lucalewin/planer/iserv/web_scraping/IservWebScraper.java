package dev.lucalewin.planer.iserv.web_scraping;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.MalformedURLException;
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

/**
 *
 * @author Luca Lewin
 * @since Planer v1.0
 */
public class IservWebScraper {

    private static final WebClient webClient = new WebClient();

    /**
     *
     * The values are specific for the url of the substitution plan
     * subst_001.htm is the page for the current plan --> TODAY = 1
     * subst_002.htm is the page for the next plan    --> TOMORROW = 2
     */
    public static final int TODAY = 1, TOMORROW = 2;

    /**
     *
     * @param context the context of the current activity
     * @return true if the login attempt was successful, otherwise false
     */
    public static boolean login(Context context) {
        final SharedPreferences sharedPreferences = context.getSharedPreferences(
                IservAccountSettingsActivity.ISERV_SP_NAME, Context.MODE_PRIVATE);

        final String domain   = sharedPreferences.getString("base_url", "");
        final String username = sharedPreferences.getString("username", "");
        final String password = sharedPreferences.getString("password", "");

        final String login_url = String.format("https://%s/iserv/app/login", domain);
        final String loginRequestBody = String.format("_username=%s&_password=%s&_remember_me=on",
                username,
                password);

        try {
            /*
             create WebRequest
              - the content type needs to be 'application/x-www-form-urlencoded'
                otherwise the login request will fail
              - the login credentials are sent in the request body
            */
            final WebRequest loginRequest = WebRequest.builder(login_url)
                    .setMethod(HttpMethod.POST)
                    .addHeaderField("content-type", "application/x-www-form-urlencoded")
                    .setBody(loginRequestBody)
                    .build();

            final Future<WebResponse> responseFuture = webClient.makeRequestAsync(loginRequest);
            final WebResponse response = responseFuture.get();

            // 302 = Found --> username and password were correct
            return response.getResponseCode() == 302;
        } catch (MalformedURLException | ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        // login failed
        return false;
    }

    /**
     *
     * @param context the context of the current activity
     * @param day ({@link IservWebScraper#TODAY}, {@link IservWebScraper#TOMORROW})
     * @return the html of the response body
     */
    private static String get(Context context, int day) {
        final SharedPreferences sharedPreferences = context.getSharedPreferences(
                IservAccountSettingsActivity.ISERV_SP_NAME, Context.MODE_PRIVATE);

        final String base_url = sharedPreferences.getString("base_url", "");

        @SuppressLint("DefaultLocale")
        final String url = String.format("https://%s/iserv/plan/show/raw/Vertretungsplan%%20Sch%%C3%%BCler/subst_00%d.htm",
                base_url, day);

        try {
            final WebRequest planerRequest = WebRequest.builder(url)
                    .setMethod(HttpMethod.GET)
                    .setResponseCharset(StandardCharsets.ISO_8859_1)
                    .build();

            final Future<WebResponse> responseFuture = webClient.makeRequestAsync(planerRequest);
            final WebResponse response = responseFuture.get();

            return response.getBody();
        } catch (MalformedURLException | ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     *
     * @param context the context of the current activity
     * @param day see {@link #TODAY}, {@link #TOMORROW}
     * @return the iserv substitution plan as an {@link IservPlan} for the specified <i>day</i>
     */
    public static IservPlan getAffectedData(Context context, int day) {

        final String res_body = get(context, day);

        if (res_body == null) return null;

        // parse html document
        Document root = Jsoup.parse(res_body);

        Element infoTable = root.getElementsByClass("info").first();


        if (infoTable == null) return null;

        Elements rows = infoTable.selectXpath("/table/tbody/tr");

        String dayOfTheWeek = Objects.requireNonNull(root.getElementsByClass("mon_title").first()).text().split(" ")[1];

        if (rows.size() < 2) {
            return new IservPlan(DateUtil.parseDayOfWeek(dayOfTheWeek, Locale.GERMAN), null);
        }

        final Map<String, List<IservPlanRow>> classes = new HashMap<>();

        // parse planer
        Element planerTable = root.getElementsByClass("mon_list").first();

        if (planerTable == null) {
            return new IservPlan(DateUtil.parseDayOfWeek(dayOfTheWeek, Locale.GERMAN), classes);
        }

        Elements planerTableRows = planerTable.selectXpath("/table/tbody/tr");

        List<IservPlanRow> current;

        for (int i = 0; i < planerTableRows.size(); i++) {
            Element planerRow = planerTableRows.get(i);

            Element firstColumn = planerRow.children().first();

            if (firstColumn == null) {
                return new IservPlan(DateUtil.parseDayOfWeek(dayOfTheWeek, Locale.GERMAN), classes);
            }

            String currentClass = firstColumn.text().toUpperCase();

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
