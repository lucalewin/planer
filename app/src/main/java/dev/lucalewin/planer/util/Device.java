package dev.lucalewin.planer.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Optional;

public final class Device {

    private static final String LOG_TAG = "Planer::DeviceUtil";

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    public static boolean hasInternetAccess(Context context) {
        if (isNetworkAvailable(context)) {
            Optional<Boolean> hasInternetAccess = new TaskRunner().execute(() -> {
                Log.e("####", "OPEN CONNECTION");
                HttpURLConnection uc = (HttpURLConnection) (new URL("http://clients3.google.com/generate_204").openConnection());
                uc.setRequestProperty("User-Agent", "Android");
                uc.setRequestProperty("Connection", "close");
                uc.setConnectTimeout(1500);
                uc.connect();
                return (uc.getResponseCode() == 204 && uc.getContentLength() == 0);
            });

            return hasInternetAccess.orElse(false);
        } else {
            Log.d(LOG_TAG, "No network available!");
        }
        return false;
    }

}
