package dev.lucalewin.planer.util;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

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

    public <R> Optional<R> execute(Callable<R> callable) {
        AtomicReference<Optional<R>> reference = new AtomicReference<>(null);

        executor.execute(() -> {
            try {
                reference.set(Optional.of(callable.call()));
                return;
            } catch (Exception e) {
                e.printStackTrace();
            }

            reference.set(Optional.empty());
        });

        // noinspection OptionalAssignedToNull, StatementWithEmptyBody
        while (reference.get() == null);

        return reference.get();
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