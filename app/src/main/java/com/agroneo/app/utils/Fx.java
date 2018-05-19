package com.agroneo.app.utils;

import android.util.Log;

import java.util.Iterator;
import java.util.List;

public class Fx {
    public static final String ISO_DATE = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";

    public static void log(Object msg) {
        try {
            Log.d("Log", msg.toString());
        } catch (Exception e) {
            Log.d("Log", "" + msg);
        }
    }

    public static String join(List<String> items, String delimiter) {
        String joined = "";
        Iterator<String> items_it = items.iterator();
        while (items_it.hasNext()) {
            joined += items_it.next();
            if (items_it.hasNext()) {
                joined += delimiter + " ";
            }
        }
        return joined;
    }
}
