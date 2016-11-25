package com.wen.college.https;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

/**
 * Created by Administrator on 2016/9/11.
 */

public class SharePrefUtil {
    private static final String NAME = "college.spf";

    public static void updateMode(Context context, boolean isNight) {
        SharedPreferences sp = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        if (sp.getBoolean("isNight", false) != isNight) {
            SharedPreferences.Editor e = sp.edit();
            e.putBoolean("isNight", isNight);
            e.commit();
            context.sendBroadcast(new Intent("com.wen.MODE_CHANGED"));
        }
    }

    public static boolean isNightMode(Context context) {
        SharedPreferences sp = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        return sp.getBoolean("isNight", false);
    }
}
