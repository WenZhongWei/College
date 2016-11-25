package com.wen.college.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.wen.college.R;
import com.wen.college.entities.Result;
import com.wen.college.https.BasicRequestCallBack;
import com.wen.college.https.XUtils;
import com.wen.college.views.TitleView;

/**
 * Created by Administrator on 2016/8/21.
 */
public class ResetpwdActivity extends BaseActivity {

    @ViewInject(R.id.resetpwd_title)
    private TitleView title;
    @ViewInject(R.id.resetpwd_email)
    private TextView tvEmail;
    @ViewInject(R.id.resetpwd_send)
    private Button btSend;
    private String phone;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resetpwd);
        ViewUtils.inject(this);
        title.setOnClickListener(clickLis);
        btSend.setOnClickListener(clickLis);

        phone = getIntent().getStringExtra("phone");
//        if (!phone.matches(Code.PHONE_MATCH)) {
//            XUtils.show(R.string.error);
//            finish();
//            return;
//        }
        getEmail();
    }


    private View.OnClickListener clickLis = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.title_back:
                    finish();
                    break;
                case R.id.resetpwd_send:
                    sendEmail();
                    break;
            }
        }
    };

    private void sendEmail() {
        RequestParams params = new RequestParams();
        params.addBodyParameter("u.phone", phone);
        XUtils.send(XUtils.SENDEMAIL, params, new BasicRequestCallBack<Result<Boolean>>() {
            @Override
            public void success(Result<Boolean> data) {
                XUtils.show(data.descript);
                if (data.data) {
                    new AlertDialog.Builder(ResetpwdActivity.this)
                            .setMessage("邮件已发送，有效期为24小时")
                            .setNegativeButton(R.string.ok, null)
                            .show();

                }
            }
        }, true);
    }

    public static void startActivity(Context context, String phone) {
        Intent in = new Intent(context, ResetpwdActivity.class);
        in.putExtra("phone", phone);
        context.startActivity(in);
    }

    private void getEmail() {
        RequestParams params = new RequestParams();
        params.addBodyParameter("u.phone", phone);
        XUtils.send(XUtils.GETEMAIL, params, new BasicRequestCallBack<Result<String>>() {
            @Override
            public void success(Result<String> data) {
                if (data.state == Result.STATE_SUC) {
                    tvEmail.setText(data.data);
                } else {
                    XUtils.show(R.string.error);
                    finish();
                }
            }
        }, true);
    }
}
