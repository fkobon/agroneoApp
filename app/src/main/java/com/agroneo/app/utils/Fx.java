package com.agroneo.app.utils;

import android.util.Log;

public class Fx {
    public static final String ISO_DATE = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";

    public static void log(Object msg) {
        try {
            Log.d("Log", msg.toString());
        } catch (Exception e) {
            Log.d("Log", "" + msg);
        }
    }
}
