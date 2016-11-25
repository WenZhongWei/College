package com.wen.college.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Administrator on 2016/9/12.
 */

public class NetWorkUtil {
    public static final int NONE = -1;
    public static final int WIFI = ConnectivityManager.TYPE_WIFI;
    public static final int MOBILE = ConnectivityManager.TYPE_MOBILE;

    public static int getNetWorkMode(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        if (info.isAvailable()) {
            return info.getType();
        } else {
            return NONE;
        }
    }
}
