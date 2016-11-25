package com.wen.college.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.view.WindowManager;

import com.wen.college.R;

/**
 * Created by Administrator on 2016/8/20.
 */

public class Loading {
    private static Dialog loading;
    private static Context mContext;

    public static void init(Context context) {
        mContext = context;
    }

    //显示Dialog
    public static void show() {
        if (loading == null) {
            loading = new AlertDialog.Builder(mContext).create();
            loading.setCanceledOnTouchOutside(false);
            Window w = loading.getWindow();
            w.setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
            loading.show();
            w.setContentView(R.layout.layout_loading);
        } else {
            loading.show();
        }
    }

    //隐藏Dialog
    public static void dismiss() {
        if (loading != null && loading.isShowing()) {
            loading.dismiss();
        }
    }

    //判断Dialog是否已经显示过
    public static boolean isShowing() {
        if (loading != null) {
            return loading.isShowing();
        }
        return false;
    }

    //销毁对话框
    public static void destory() {
        dismiss();

        if (loading != null) {
            loading = null;
            mContext = null;
        }
    }
}
