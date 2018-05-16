package com.agroneo.app.utils;

import android.util.Log;

public class Fx {
    public static void log(Object msg) {
        try {
            Log.e("Log", msg.toString());
        } catch (Exception e) {
            Log.e("Log", "" + msg);
        }
    }
}
