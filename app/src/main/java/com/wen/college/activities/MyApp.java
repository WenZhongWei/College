package com.wen.college.activities;

import android.app.Application;

import com.wen.college.entities.Tab;
import com.wen.college.entities.User;
import com.wen.college.https.XUtils;
import com.wen.college.utils.Loading;

import java.util.List;

/**
 * Created by Administrator on 2016/8/20.
 */

public class MyApp extends Application {

    private User user;
    private List<Tab> myTabs;
    private List<Tab> otherTabs;

    public List<Tab> getMyTabs() {
        if (myTabs == null) {
            myTabs = XUtils.findAll("0");
        }
        return myTabs;
    }

    public void setMyTabs(List<Tab> myTabs) {
        this.myTabs = myTabs;
    }

    public List<Tab> getOtherTabs() {
        if (otherTabs == null) {
            otherTabs = XUtils.findAll("1");
        }
        return otherTabs;
    }

    public void setOtherTabs(List<Tab> otherTabs) {

        this.otherTabs = otherTabs;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Loading.init(this);
        XUtils.init(this);
    }

//    public static void release() {
//        user = null;
//    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }



    /* public static void setUser(User user) {
        user = user;
        if (user != null) {
            XUtils.save(user);
        } else {
            XUtils.deleteUsers();
        }
    }


    public static User getUser() {
        if (user == null) {
            user = XUtils.loadUser();
        }
        return user;
    }*/


}
