package com.wen.college.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.wen.college.R;
import com.wen.college.auxiliary.Code;
import com.wen.college.auxiliary.CodeTimerTask;
import com.wen.college.auxiliary.MyTextWatcher;
import com.wen.college.entities.User;
import com.wen.college.https.XUtils;
import com.wen.college.utils.Loading;
import com.wen.college.views.MEditText;
import com.wen.college.views.TitleView;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

/**
 * Created by Administrator on 2016/8/20.
 */
public class ValidateActivity extends BaseActivity {

    public static final int ACTION_REG = 1;
    public static final int ACTION_RESET_PWD = 2;
    public static final int ACTION_UPDATE_PHONE = 3;
    public static final int ACTION_UPDATE_EMAIL = 4;
    @ViewInject(R.id.validate_title)
    private TitleView title;
    @ViewInject(R.id.validate_phone)
    private MEditText mePhone;
    @ViewInject(R.id.validate_code)
    private MEditText meCode;
    @ViewInject(R.id.validate_get_code)
    private TextView tvGetCode;
    private static String phone;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    XUtils.show((String) msg.obj);
                    break;
                case 1:
                    XUtils.show(msg.arg1);
                    if (msg.arg2 == 1) {
                        CodeTimerTask.getInstence().cancel();
                    }
                    break;
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SMSSDK.initSDK(this, "164fc911fd768", "a6090f62ebabfae5e1dee70f89d984d3");
        setContentView(R.layout.activity_validate);
        ViewUtils.inject(this);
        title.setBackClickListener(clickLis);
        title.setRightClickListener(clickLis);
        tvGetCode.setOnClickListener(clickLis);
        mePhone.addTextChangeListener(phoneWatcher);
        meCode.addTextChangeListener(codeWatcher);
        SMSSDK.registerEventHandler(eventHandler);

        if (phone != null) {
            mePhone.setText(phone);
        }

        if (CodeTimerTask.getInstence().isRun()) {
            CodeTimerTask.getInstence().startTimer(tvGetCode);
        }
    }

    //SMSSDK的注册方法
    private EventHandler eventHandler = new EventHandler() {
        @Override
        public void afterEvent(int event, int result, Object data) {
            if (result == SMSSDK.RESULT_COMPLETE) {//完成
                switch (event) {
                    case SMSSDK.EVENT_GET_VERIFICATION_CODE:
                        handler.sendMessage(handler.obtainMessage(1, R.string.code_send_suc, 0));
                        break;
                    case SMSSDK.EVENT_GET_VOICE_VERIFICATION_CODE:
                        Loading.dismiss();
                        switch (getIntent().getIntExtra("action", -1)) {
                            case ACTION_REG:
                                RegisterActivity.startActivity(ValidateActivity.this, phone);
                                break;
                            case ACTION_UPDATE_PHONE:
                                User u = ((MyApp) getApplication()).getUser();
                                if (u.getPhone().equals(getIntent().getStringExtra(phone))) {
                                    AccountUpdateActivity.startActivity(ValidateActivity.this, true);
                                } else {
                                    XUtils.show(R.string.validate_phone_fail);
                                }
                                break;
                            case ACTION_UPDATE_EMAIL:
                                u = ((MyApp) getApplication()).getUser();
                                if (u.getPhone().equals(getIntent().getStringExtra(phone))) {
                                    AccountUpdateActivity.startActivity(ValidateActivity.this, false);
                                } else {
                                    XUtils.show(R.string.validate_email_fail);
                                }
                                break;
                            case ACTION_RESET_PWD:
                                ResetpwdActivity.startActivity(ValidateActivity.this, phone);
                                break;
                        }
                        finish();
                        break;
                }
            } else {//失败
                Log.e("college", "====code error======" + JSON.toJSONString(data));
                switch (event) {
                    case SMSSDK.EVENT_GET_VERIFICATION_CODE:
                        handler.sendMessage(handler.obtainMessage(1, R.string.code_sent_fail, 1));
                        break;
                    case SMSSDK.EVENT_GET_VOICE_VERIFICATION_CODE:
                        handler.sendMessage(handler.obtainMessage(1, R.string.validate_code_fail, 0));
                        Loading.dismiss();
                        break;
                }
            }
        }
    };


    //监听验证码是否匹配
    private MyTextWatcher codeWatcher = new MyTextWatcher() {
        @Override
        public void afterTextChanged(Editable s) {
            if (s.length() == 4) {
                title.setRightTextVisibility(View.VISIBLE);
            } else {
                title.setRightTextVisibility(View.GONE);
            }
        }
    };

    //监听输入的手机号是否匹配
    private MyTextWatcher phoneWatcher = new MyTextWatcher() {
        @Override
        public void afterTextChanged(Editable s) {
            if (s.toString().matches(Code.PHONE_MATCH)) {
                tvGetCode.setEnabled(true);
            } else {
                tvGetCode.setEnabled(false);
            }
        }
    };

    //点击事件
    private View.OnClickListener clickLis = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.title_back:
                    finish();
                    break;
                case R.id.validate_get_code:
                    getCode();
                    break;
                case R.id.title_tv_right:
                    phone = mePhone.getText();
                    String code = meCode.getText();
                    if (!phone.matches(Code.PHONE_MATCH)) {
                        XUtils.show(R.string.phone_format_error);
                        return;
                    }
                    Loading.show();
                    SMSSDK.submitVerificationCode("86", phone, code);
                    break;
            }
        }
    };


    public static void startActivity(Context context, int action) {
        Intent in = new Intent(context, ValidateActivity.class);
        in.putExtra("action", action);
        context.startActivity(in);
    }

    //获取验证码
    private void getCode() {
        phone = mePhone.getText();
        if (phone.matches(Code.PHONE_MATCH)) {
            SMSSDK.getVerificationCode("86", phone);
            CodeTimerTask.getInstence().startTimer(tvGetCode);
        }
    }

    //取消smssdk的方法
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterEventHandler(eventHandler);
    }

}
