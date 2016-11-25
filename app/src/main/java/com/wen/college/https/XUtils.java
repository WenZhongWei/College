package com.wen.college.https;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.wen.college.R;
import com.wen.college.entities.Tab;
import com.wen.college.entities.User;
import com.wen.college.utils.Loading;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Administrator on 2016/8/18.
 */

public class XUtils {
        public static final String U = "http://172.16.40.101:8080/college_online_wen/";
//    public static final String U = "http://172.16.40.103:8080/college_online_wen/";
    //    public static final String U = "http://192.168.12.1:8080/college_online_wen/";//没网
//        public static final String U = "http://10.51.29.134:8080/college_online_wen/";//wifi
//    public static final String U = "http://172.18.17.16:8080/college_online_wen/";
    public static String path = Environment.getExternalStorageDirectory().getPath() + File.separator + "college" + File.separator + "download" + File.separator + "college.apk";
    public static final String LOGIN = "login";
    public static final String REG = "register";
    public static final String EXIT_LOGIN = "com.wen.college.EXIT_LOGIN";
    public static final String SENDEMAIL = "sendemail";
    public static final String GETEMAIL = "getemail";
    public static final String FEEDBACK = "feedback";
    public static final String UPINFO = "upinfo";
    public static final String UPDATEPWD = "uppwd";
    public static final String CHECKVER = "checkver";
    public static final String TABS = "tabs";
    private static Context mContext;
    private static HttpHandler handler;//停止网络请求
    private static BitmapUtils bitmapUtils;
    private static HttpUtils httpUtils;
    private static DbUtils db;

    //初始化各种Utils
    public static void init(Context context) {
        mContext = context;
        if (bitmapUtils == null) {
            bitmapUtils = new BitmapUtils(context);
            bitmapUtils.configDefaultLoadingImage(R.drawable.logo_tr);
            bitmapUtils.configDefaultLoadFailedImage(R.drawable.no_data);
            bitmapUtils.configDiskCacheEnabled(true);
        }
        if (httpUtils == null) {
            httpUtils = new HttpUtils();
        }
        if (db == null) {
            db = DbUtils.create(context, "collegewen.db");
        }
    }

    //封装一个专门用于get请求的方法
    public static <T> void send(String url, RequestCallBack<T> callBack, boolean loading) {
        send(url, null, callBack, loading);
    }

    //封装httputils的send方法
    public static <T> void send(String url, RequestParams params, RequestCallBack<T> callBack, boolean loading) {
        if (loading) {
            Loading.show();

        }
        if (params == null) {
            handler = httpUtils.send(HttpRequest.HttpMethod.GET, U + url, callBack);
        } else {
            handler = httpUtils.send(HttpRequest.HttpMethod.POST, U + url, params, callBack);
        }
    }

    public static HttpHandler<File> download(String url, RequestCallBack<File> callBack) {
        return httpUtils.download(U + url, path, false, true, callBack);
    }


    //取消请求的方法
    public static void cancel() {
        if (handler != null) {
            handler.cancel();
            handler = null;
        }
    }

    //加载图片的方法
    public static void display(ImageView img, String url) {
        bitmapUtils.display(img, url);
    }

    //Toast方法
    public static void show(String text) {
        Toast.makeText(mContext, text, Toast.LENGTH_SHORT).show();
    }

    //Toast方法
    public static void show(int resId) {
        Toast.makeText(mContext, resId, Toast.LENGTH_SHORT).show();
    }

    public static void save(Object o) {
        try {
            db.save(o);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    public static void deleteUsers() {
        try {
            db.deleteAll(User.class);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    public static User loadUser() {
        try {
            return db.findFirst(User.class);
        } catch (DbException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void saveTabs(List<Tab> tabs) {
        try {
            List<Tab> other = findAll("1");
            try {
                db.delete(Tab.class);
            } catch (Exception e) {
                Log.e("college", "=====数据为空，删除失败=====");
            }
            if (other != null) {
                for (Tab t : tabs) {
                    if (other.contains(t)) {
                        t.setIsmy(1);
                    }
                }
            }
            db.saveAll(tabs);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    public static List<Tab> findAll(String ismyValue) {
        try {
            List<Tab> l = db.findAll(Selector.from(Tab.class).where("ismy", "=", ismyValue));
            if (l != null && l.size() > 0) {
                return l;
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
        return new LinkedList<>();
    }

    public static void updateTabs(List<Tab> tabs) {
        try {
            db.updateAll(tabs);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }
}
