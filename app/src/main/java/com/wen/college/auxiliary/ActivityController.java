package com.wen.college.auxiliary;

import android.app.Activity;
import android.os.Process;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Administrator on 2016/8/20.
 */

public class ActivityController {
    //创建队列用于存放Activity
    private static List<Activity> activities = new LinkedList<>();

    //添加Activity的方法
    public static void addActivity(Activity activity){
        activities.add(activity);
    }

    //移除
    public static void removeActivity(Activity activity){
        activities.remove(activity);
    }

    //关闭
    public static void closeActivity(){
        for (Activity activity : activities){
            activity.finish();
        }
        //杀掉进程
        Process.killProcess(Process.myPid());
    }
}
