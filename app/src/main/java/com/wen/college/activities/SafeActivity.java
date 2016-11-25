package com.wen.college.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.wen.college.R;
import com.wen.college.entities.User;
import com.wen.college.views.TitleView;
import com.wen.college.views.UserInfoView;

/**
 * Created by Administrator on 2016/9/8.
 */
public class SafeActivity extends BaseActivity {

    @ViewInject(R.id.safe_title)
    private TitleView title;
    @ViewInject(R.id.safe_phone)
    private UserInfoView uvPhone;
    @ViewInject(R.id.safe_email)
    private UserInfoView uvEmail;
    @ViewInject(R.id.safe_repwd)
    private UserInfoView uvRepwd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_safe);
        ViewUtils.inject(this);
        title.setBackClickListener(clickLis);
        uvPhone.setOnClickListener(clickLis);
        uvEmail.setOnClickListener(clickLis);
        uvRepwd.setOnClickListener(clickLis);

        initData();
        registerReceiver(receiver, new IntentFilter("com.wen.UPDATE_PWD"));
    }

    private void initData() {
        User u = ((MyApp) getApplication()).getUser();
        String email = u.getEmail();
        if (email != null) {
            String[] strs = email.split("@");
            if (strs[0].length() >= 3) {
                strs[0] = strs[0].substring(0, 3) + "******";
            } else {
                strs[0] = strs[0].substring(0, 1) + "******";
            }
            email = strs[0] + "@" + strs[1];
            uvEmail.setText(email);
        }

        String phone = u.getPhone();
        if (phone != null) {
            String[] strs = phone.split(phone.substring(3, 9));
            phone = strs[0] + "*******" + strs[1];
            uvPhone.setText(phone);
        }
    }

    private View.OnClickListener clickLis = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.safe_title:
                    finish();
                    break;
                case R.id.safe_repwd:
                    PwdUpdateActivity.startActivity(SafeActivity.this);
                    break;
                case R.id.safe_email:
//                    AccountUpdateActivity.startActivity(SafeActivity.this, false);
                    ValidateActivity.startActivity(SafeActivity.this, ValidateActivity.ACTION_UPDATE_EMAIL);
                    break;
                case R.id.safe_phone:
//                    AccountUpdateActivity.startActivity(SafeActivity.this, true);
                    ValidateActivity.startActivity(SafeActivity.this, ValidateActivity.ACTION_UPDATE_PHONE);
                    break;
            }
        }
    };

    public static void startActivity(Context context) {
        Intent in = new Intent(context, SafeActivity.class);
        context.startActivity(in);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("com.wen.UPDATE_PWD")) {
                finish();
            }
        }
    };
}
