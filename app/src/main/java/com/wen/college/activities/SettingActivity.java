package com.wen.college.activities;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnCompoundButtonCheckedChange;
import com.wen.college.BuildConfig;
import com.wen.college.R;
import com.wen.college.entities.AppVersion;
import com.wen.college.entities.Result;
import com.wen.college.https.BasicRequestCallBack;
import com.wen.college.https.SharePrefUtil;
import com.wen.college.https.XUtils;
import com.wen.college.views.TitleView;
import com.wen.college.views.UserInfoView;

import java.io.File;

/**
 * Created by Administrator on 2016/9/10.
 */
public class SettingActivity extends BaseActivity {

    @ViewInject(R.id.setting_title)
    private TitleView title;
    @ViewInject(R.id.setting_version)
    private UserInfoView uvVersion;
    @ViewInject(R.id.setting_cb_mode)
    private CheckBox cbMode;
    private AppVersion appVer;
    private HttpHandler<File> httpHandler;
    private ProgressDialog downloadDialog;
    private Handler handler = new Handler();
    private boolean isCancelDownload;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        registerReceiver(receiver, new IntentFilter("com.wen.MODE_CHANGED"));
    }

    private void init() {
        boolean isNight = SharePrefUtil.isNightMode(this);
        if (isNight) {
            setTheme(R.style.Night);
        } else {
            setTheme(R.style.AppTheme);
        }
        setContentView(R.layout.activity_setting);
        ViewUtils.inject(this);
        cbMode.setChecked(isNight);
        title.setBackClickListener(clickLis);
        uvVersion.setOnClickListener(clickLis);
        uvVersion.setText("v" + BuildConfig.VERSION_NAME);
    }

    @OnCompoundButtonCheckedChange(R.id.setting_cb_mode)
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        SharePrefUtil.updateMode(this, isChecked);
    }


    private View.OnClickListener clickLis = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.title_back:
                    finish();
                    break;
                case R.id.setting_version:
                    checkVersion();
                    break;
            }
        }
    };

    private void checkVersion() {
        int version = BuildConfig.VERSION_CODE;
        RequestParams params = new RequestParams();
        params.addBodyParameter("version", String.valueOf(version));
        XUtils.send(XUtils.CHECKVER, params, new BasicRequestCallBack<Result<AppVersion>>() {
            @Override
            public void success(Result<AppVersion> data) {
                if (data.state == Result.STATE_SUC) {
                    appVer = data.data;
                    showVerDialog();
                } else {
                    XUtils.show(data.descript);
                }
            }
        }, true);
    }

    private void showVerDialog() {
        String oldVerName = BuildConfig.VERSION_NAME;
        new AlertDialog.Builder(SettingActivity.this)
                .setMessage(String.format("检查到版本%s,当前版本为%s。是否更新？", appVer.getVerName(), oldVerName))
                .setNegativeButton("立即更新", dialogLis)
                .setNeutralButton("下次再说", null)
                .show();
    }

    private DialogInterface.OnClickListener dialogLis = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            downloadDialog = new ProgressDialog(SettingActivity.this);
            downloadDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            downloadDialog.setMax(100);
            downloadDialog.show();
            downloadDialog.setCanceledOnTouchOutside(false);
            httpHandler = XUtils.download(appVer.getVerUrl(), new RequestCallBack<File>() {
                @Override
                public void onLoading(long total, long current, boolean isUploading) {
                    super.onLoading(total, current, isUploading);
                    int progress = (int) (((float) current / total) * 100);
                    downloadDialog.setProgress(progress);
                }

                @Override
                public void onSuccess(ResponseInfo<File> responseInfo) {
                    downloadDialog.setProgress(100);
                    downloadDialog.dismiss();
                    downloadDialog = null;

                    Intent in = new Intent(Intent.ACTION_VIEW);
                    in.setDataAndType(Uri.fromFile(new File(XUtils.path)), "application/vnd.android.package-archive");
                    startActivity(in);
                }

                @Override
                public void onFailure(HttpException e, String s) {
                    XUtils.show(R.string.download_fail);
                }
            });
        }
    };

    public static void startActivity(Context context) {
        Intent in = new Intent(context, SettingActivity.class);
        context.startActivity(in);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.ACTION_DOWN) {
            if (!isCancelDownload) {
                isCancelDownload = true;
                XUtils.show("再次点击取消下载");
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        isCancelDownload = true;
                    }
                }, 1000);
                return true;
            } else {
                httpHandler.cancel();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            init();
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }
}
