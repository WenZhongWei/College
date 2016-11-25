package com.wen.college.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.KeyEvent;

import com.wen.college.R;
import com.wen.college.entities.Result;
import com.wen.college.entities.Tab;
import com.wen.college.https.BasicRequestCallBack;
import com.wen.college.https.XUtils;
import com.wen.college.utils.NetWorkUtil;

import java.util.List;

/**
 * Created by Administrator on 2016/9/12.
 */

public class WelcomeActivity extends BaseActivity {
    private Handler handler = new Handler();
    private boolean isCan;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (NetWorkUtil.getNetWorkMode(this) != NetWorkUtil.NONE) {
            XUtils.send(XUtils.TABS, new BasicRequestCallBack<Result<List<Tab>>>() {
                @Override
                public void success(Result<List<Tab>> data) {
                    if (data.state == Result.STATE_SUC) {
                        XUtils.saveTabs(data.data);
                        toMain();
                    } else {
                        loadDB();
                    }
                }

                @Override
                public void failure() {
                    loadDB();
                }
            }, false);
        } else {
            loadDB();
        }
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                toMain();
            }
        }, 2000);
    }

    private void loadDB() {
        if (haveData()) {
            toMain();
        } else {
            XUtils.show(getString(R.string.data_null_exit));
            finish();
        }
    }

    private boolean haveData() {
        if (((MyApp) getApplication()).getMyTabs() != null || ((MyApp) getApplication()).getOtherTabs() != null)
            return true;
        return false;
    }

    private void toMain() {
        if (isCan) {
            Intent in = new Intent(this, MainActivity.class);
            startActivity(in);
            finish();
        } else {
            isCan = true;
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
