package com.wen.college.activities;

import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;

import com.wen.college.R;
import com.wen.college.auxiliary.ActivityController;
import com.wen.college.https.SharePrefUtil;
import com.wen.college.https.XUtils;

/**
 * Created by Administrator on 2016/8/20.
 * 用于记录所有的Activity,确保关闭时可以彻底关闭某个Activity
 */

public class BaseActivity extends FragmentActivity {

    private boolean isExit;
    //定时器用于判断第一次按和第二次按间隔多少秒才有效
    private Handler handler = new Handler();

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        //在oncreate时调用添加
        ActivityController.addActivity(this);

        boolean isNight = SharePrefUtil.isNightMode(this);
        if (isNight) {
            setTheme(R.style.Night);
        } else {
            setTheme(R.style.AppTheme);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在onDestroy时调用移除
        ActivityController.removeActivity(this);
    }

    //用于关闭程序的方法
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.ACTION_DOWN) {
            //当程序页面退到MainActivity时代表到达根目录，提示再按一次将退出的方法
            //首先判断目前的包名是不是为MainActivity
            if (getClass().getName().equals(MainActivity.class.getName())) {
                if (!isExit) {
                    isExit = true;
                    XUtils.show(R.string.exit);
                    //第一次和第二次点击间隔最大2秒
                    handler.postDelayed(r, 2000);
                } else {
                    ActivityController.closeActivity();
                }
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private Runnable r = new Runnable() {
        @Override
        public void run() {
            isExit = false;
        }
    };
}
