package com.wen.college.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.wen.college.R;
import com.wen.college.auxiliary.Code;
import com.wen.college.entities.Result;
import com.wen.college.entities.User;
import com.wen.college.https.BasicRequestCallBack;
import com.wen.college.https.XUtils;
import com.wen.college.views.MEditText;
import com.wen.college.views.TitleView;

/**
 * Created by Administrator on 2016/9/9.
 */
public class PwdUpdateActivity extends BaseActivity {

    @ViewInject(R.id.pwd_title)
    private TitleView title;
    @ViewInject(R.id.pwd_old_pwd)
    private MEditText meOldpwd;
    @ViewInject(R.id.pwd_new_pwd)
    private MEditText meNewpwd;
    @ViewInject(R.id.pwd_repwd)
    private MEditText meRepwd;
    private User u;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pwdupdate);
        ViewUtils.inject(this);

        title.setBackClickListener(clickLis);
        title.setRightClickListener(clickLis);
        u = ((MyApp) getApplication()).getUser();
    }

    private View.OnClickListener clickLis = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.title_back:
                    finish();
                    break;
                case R.id.title_tv_right:
                    save();
                    break;
            }
        }
    };

    private void save() {
        String oldpwd = meOldpwd.getText();
        String pwd = meNewpwd.getText();
        String repwd = meRepwd.getText();
        if (!oldpwd.matches(Code.PWD_MATCH)) {
            XUtils.show(R.string.pwd_format_error);
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
        params.addBodyParameter("u.token", u.getToken());
        params.addBodyParameter("u.pwd", pwd);
        params.addBodyParameter("oldpwd", oldpwd);
        XUtils.send(XUtils.UPDATEPWD, params, new BasicRequestCallBack<Result<Boolean>>() {
            @Override
            public void success(Result<Boolean> data) {
                XUtils.show(data.descript);
                if (data.state == Result.STATE_SUC) {
                    ((MyApp) getApplication()).setUser(null);
                    sendBroadcast(new Intent("com.wen.UPDATE_PWD"));
                    LoginActivity.startActivity(PwdUpdateActivity.this,true);
                    finish();
                }
            }
        }, true);
    }

    public static void startActivity(Context context) {
        Intent in = new Intent(context, PwdUpdateActivity.class);
        context.startActivity(in);
    }
}
