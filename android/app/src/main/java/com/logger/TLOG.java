package com.logger;

import android.os.SystemClock;
import android.util.Log;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

class Timer {
    long starttime = SystemClock.elapsedRealtime();
    long lastSplit = SystemClock.elapsedRealtime();
    String TAG;

    public Timer(String tag) {
        this(tag, "");
    }

    public Timer(String tag, String text) {
        TAG = tag;
        Log.d("JOGO-" + TAG, "BEGIN: " + text);
    }

    public void addSplit(String text) {
        Log.d("JOGO-" + TAG, "[ " + (SystemClock.elapsedRealtime() - lastSplit) + "ms ] " + text);
        lastSplit = SystemClock.elapsedRealtime();
    }

    public void end() {
        Log.d("JOGO-" + TAG, "END [ " + (SystemClock.elapsedRealtime() - starttime) + "ms ]");
    }

}

public class TLOG {
    //todo we can improve multithreaded performance here by working with an operation call queue for operations
    //todo make sure the functions only execute when the app is in debug mode

    private static final ConcurrentMap<String, Timer> timings = new ConcurrentHashMap<>();

    public static String build_tag(String TAG) {
        //this function adds the thread ID, to allow for multi threaded tlogging
        long id = Thread.currentThread().getId();
        return TAG + "-" + id;
    }

    public static void start(String TAG, String text) {
        TAG = build_tag(TAG);
        if (timings.get(TAG) != null) {
            SLOG.e("removing:" + TAG);
            timings.remove(TAG);
        }
        timings.put(TAG, new Timer(TAG, text));
    }

    public static void start(String TAG) {
        start(TAG, "");
    }

    public static void addSplit(String TAG, String text) {
        String idTAG = build_tag(TAG);
        if (timings.get(idTAG) == null) start(TAG, text);//start already adds the id
        else Objects.requireNonNull(timings.get(idTAG)).addSplit(text);
    }

    public static void end(String TAG) {
        TAG = build_tag(TAG);
        Objects.requireNonNull(timings.remove(TAG)).end();
    }


}
