package com.wen.college.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.wen.college.R;
import com.wen.college.auxiliary.Code;
import com.wen.college.entities.Result;
import com.wen.college.https.BasicRequestCallBack;
import com.wen.college.https.XUtils;
import com.wen.college.views.MEditText;
import com.wen.college.views.TitleView;

/**
 * Created by Administrator on 2016/8/21.
 */
public class RegisterActivity extends BaseActivity {

    @ViewInject(R.id.reg_title)
    private TitleView title;
    @ViewInject(R.id.reg_email)
    private MEditText meEmail;
    @ViewInject(R.id.reg_pwd)
    private MEditText mePwd;
    @ViewInject(R.id.reg_repwd)
    private MEditText meRepwd;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ViewUtils.inject(this);

        title.setBackClickListener(clickLis);
        title.setRightClickListener(clickLis);
    }

    private View.OnClickListener clickLis = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.title_back:
                    finish();
                    break;
                case R.id.title_tv_right:
                    signUp();
                    break;
            }
        }
    };

    private void signUp() {
        String phone = getIntent().getStringExtra("phone");
        String email = meEmail.getText();
        String pwd = mePwd.getText();
        String repwd = meRepwd.getText();
        if (phone == null) {
            XUtils.show(R.string.error);
            return;
        }
        if (!email.matches(Code.EMAIL_MATCH)) {
            XUtils.show(R.string.email_format_error);
            return;
        }
        if (!pwd.matches(Code.PWD_MATCH)) {
            XUtils.show(R.string.pwd_format_error);
            return;
        }
        if (!pwd.equals(repwd)) {
            XUtils.show(R.string.repwd_deffience);
            return;
        }

        RequestParams params = new RequestParams();
        params.addBodyParameter("u.phone", phone);
        params.addBodyParameter("u.email", email);
        params.addBodyParameter("u.pwd", pwd);
        XUtils.send(XUtils.REG, params, new BasicRequestCallBack<Result<Boolean>>() {
            @Override
            public void success(Result<Boolean> data) {
                XUtils.show(data.descript);
                if (data.data) {
                    finish();
                }
            }
        }, true);
    }


    public static void startActivity(Context context, String phone) {
        Intent in = new Intent(context, RegisterActivity.class);
        in.putExtra("phone", phone);
        context.startActivity(in);
    }
}
