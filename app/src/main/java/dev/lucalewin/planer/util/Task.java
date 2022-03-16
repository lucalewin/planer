package dev.lucalewin.planer.util;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Task {

    private static final Executor executor = Executors.newSingleThreadExecutor();
    private static final Handler handler = new Handler(Looper.getMainLooper());

    public static Task run(Runnable runnable) {
        executor.execute(() -> handler.post(runnable));
        return null;
    }

}
