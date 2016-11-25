package com.wen.college.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.wen.college.R;
import com.wen.college.entities.Result;
import com.wen.college.entities.User;
import com.wen.college.https.BasicRequestCallBack;
import com.wen.college.https.XUtils;
import com.wen.college.views.MEditText;
import com.wen.college.views.TitleView;

public class LoginActivity extends BaseActivity {

    @ViewInject(R.id.login_title)
    private TitleView title;
    @ViewInject(R.id.login_account)
    private MEditText meAccount;
    @ViewInject(R.id.login_pwd)
    private MEditText mePwd;
    @ViewInject(R.id.login_sign_in)
    private Button btSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //xUtils包的方法，注解式
        ViewUtils.inject(this);

        //设置各个控件的点击事件
        title.setBackClickListener(clickLis);
        title.setRightClickListener(clickLis);
        btSignIn.setOnClickListener(clickLis);
    }

    //点击的方法
    private View.OnClickListener clickLis = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.login_sign_in:
                    login();
                    break;
                case R.id.title_back:
                    finish();
                    break;
                case R.id.title_tv_right:
                    break;
            }
        }
    };

    //为快速注册，重置密码添加点击事件
    @OnClick({R.id.login_reset_pwd, R.id.login_sign_up})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_reset_pwd:
                ValidateActivity.startActivity(this, ValidateActivity.ACTION_RESET_PWD);
//                ResetpwdActivity.startActivity(this, "13522222222");
                break;
            case R.id.login_sign_up:
//                ValidateActivity.startActivity(this, ValidateActivity.ACTION_REG);
                RegisterActivity.startActivity(this, "13511111111");
                break;
        }
    }

    private void login() {
        String account = meAccount.getText();
        String pwd = mePwd.getText();
        RequestParams params = new RequestParams();
        //判断账号的类型
        if (account.matches("^1(3|4|5|7|8)\\d{9}$")) {
            params.addBodyParameter("u.phone", account);
        } else if (account.matches("^\\w+@\\w+\\.(com|cn)(.cn)?$")) {
            params.addBodyParameter("u.email", account);
        } else {
            XUtils.show(R.string.account_fomat_error);
            return;
        }
        if (!pwd.matches("^\\w{6,20}$")) {
            XUtils.show(R.string.pwd_format_error);
            return;
        }
        params.addBodyParameter("u.pwd", pwd);

        //网络请求
        XUtils.send(XUtils.LOGIN, params, new BasicRequestCallBack<Result<User>>() {
            @Override
            public void success(Result<User> data) {
                XUtils.show(data.descript);
                if (data.state == Result.STATE_SUC) {
                    ((MyApp) getApplication()).setUser(data.data);
                    //跳转
                    if (getIntent().getBooleanExtra("isToCenter", true)) {
                        CenterActivity.startActivity(LoginActivity.this);
                    }
                    sendBroadcast(new Intent("com.wen.LOGIN_CHANGE"));
                    finish();

                }
            }
        }, true);
    }

    public static void startActivity(Context context, boolean isToCenter) {
        Intent in = new Intent(context, LoginActivity.class);
        in.putExtra("isToCenter", isToCenter);
        context.startActivity(in);
    }
}
