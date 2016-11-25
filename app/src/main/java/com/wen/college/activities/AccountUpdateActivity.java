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
 * Created by Administrator on 2016/9/8.
 */
public class AccountUpdateActivity extends BaseActivity {

    @ViewInject(R.id.account_title)
    private TitleView title;
    @ViewInject(R.id.account_phone_input)
    private MEditText mePhone;
    @ViewInject(R.id.account_email_input)
    private MEditText meEmail;
    private User u;
    private boolean isUpdatePhone;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        isUpdatePhone = getIntent().getBooleanExtra("isUpdatePhone", false);
        ViewUtils.inject(this);
        title.setBackClickListener(clickLis);
        title.setRightClickListener(clickLis);
        u = ((MyApp) getApplication()).getUser();

        if (isUpdatePhone) {
            mePhone.setVisibility(View.VISIBLE);
            meEmail.setVisibility(View.GONE);
        } else {
            mePhone.setVisibility(View.GONE);
            meEmail.setVisibility(View.VISIBLE);
        }
        if (u.getPhone() != null && isUpdatePhone) {
            mePhone.setText(u.getPhone());
        }
        if (u.getEmail() != null && !isUpdatePhone) {
            meEmail.setText(u.getEmail());
        }
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
        String phone = null;
        String email = null;
        RequestParams params = new RequestParams();
        params.addBodyParameter("u.token", u.getToken());
        if (isUpdatePhone) {
            phone = mePhone.getText();
            if (phone.equals(u.getPhone()) || !phone.matches(Code.PHONE_MATCH)) {
                XUtils.show(R.string.not_need_update);
                return;
            }
            params.addBodyParameter("u.phone", phone);
        } else {
            email = meEmail.getText();
            if (email.equals(u.getEmail()) || !email.matches(Code.EMAIL_MATCH)) {
                XUtils.show(R.string.not_need_update);
                return;
            }
            params.addBodyParameter("u.email", email);
        }
        XUtils.send(XUtils.UPINFO, params, new BasicRequestCallBack<Result<User>>() {
            @Override
            public void success(Result<User> data) {
                XUtils.show(data.descript);
                if (data.state == Result.STATE_SUC) {
                    ((MyApp) getApplication()).setUser(data.data);
                    finish();
                }
            }
        }, true);
    }

    public static void startActivity(Context context, boolean isUpdatePhone) {
        Intent in = new Intent(context, AccountUpdateActivity.class);
        in.putExtra("isUpdatePhone", isUpdatePhone);
        context.startActivity(in);
    }
}
