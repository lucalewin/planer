package dev.lucalewin.planer.iserv.web_scraping;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class TaskRunner {

    private final Executor executor = Executors.newSingleThreadExecutor();
    private final Handler handler = new Handler(Looper.getMainLooper());

    @FunctionalInterface
    public interface Callback<R> {
        void onComplete(R result);
    }

    @FunctionalInterface
    public interface ErrorCallback {
        void onError(String message);
    }

    public <R> void executeAsync(Callable<R> callable) {
        executor.execute(() -> {
            try {
                callable.call();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public <R> void executeAsync(Callable<R> callable, ErrorCallback errorCallback) {
        executor.execute(() -> {
            try {
                callable.call();
            } catch (Exception e) {
                e.printStackTrace();
                errorCallback.onError(e.getMessage());
            }
        });
    }

    public <R> void executeAsync(Callable<R> callable, Callback<R> callback) {
        executor.execute(() -> {
            try {
                final R result = callable.call();
                handler.post(() -> callback.onComplete(result));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public <R> void executeAsync(Callable<R> callable, Callback<R> callback, ErrorCallback errorCallback) {
        executor.execute(() -> {
            try {
                final R result = callable.call();
                handler.post(() -> callback.onComplete(result));
            } catch (Exception e) {
                e.printStackTrace();
                errorCallback.onError(e.getMessage());
            }
        });
    }

}