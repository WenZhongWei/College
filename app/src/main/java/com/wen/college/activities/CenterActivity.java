package com.wen.college.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.view.ResType;
import com.lidroid.xutils.view.annotation.ResInject;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.lidroid.xutils.view.annotation.event.OnItemClick;
import com.wen.college.R;
import com.wen.college.auxiliary.Code;
import com.wen.college.entities.Result;
import com.wen.college.entities.User;
import com.wen.college.https.BasicRequestCallBack;
import com.wen.college.https.SharePrefUtil;
import com.wen.college.https.XUtils;
import com.wen.college.views.FeedBackDialog;
import com.wen.college.views.TitleView;

/**
 * Created by Administrator on 2016/8/20.
 */

public class CenterActivity extends BaseActivity {

    @ViewInject(R.id.center_title)
    private TitleView title;
    @ViewInject(R.id.center_exit)
    private Button btExit;
    @ViewInject(R.id.center_items)
    private ListView items;
    @ViewInject(R.id.center_nick)
    private TextView tvNick;
    @ViewInject(R.id.center_account)
    private TextView tvAccount;
    @ViewInject(R.id.center_school)
    private TextView tvSchool;
    @ViewInject(R.id.center_photo)
    private ImageView imgPhoto;
    private User u;

    private int[] items_icons = new int[]{R.drawable.icon_mail, R.drawable.icon_lock, R.drawable.icon_set, R.drawable.icon_write};
    @ResInject(id = R.array.center_items_text, type = ResType.StringArray)
    private String[] items_text;
    @ResInject(id = R.string.weishezhi, type = ResType.String)
    private String kong;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*setContentView(R.layout.activity_center);
        ViewUtils.inject(this);

        title.setBackClickListener(clickLis);
        btExit.setOnClickListener(clickLis);

        u = ((MyApp) getApplication()).getUser();
        if (u != null) {
            tvAccount.setText(getString(R.string.account_layout) + (u.getPhone() == null ? (u.getEmail() == null ? getString(R.string.weishezhi) : u.getEmail()) : u.getPhone()));
            tvSchool.setText(getString(R.string.school_layout) + (u.getSchool() == null ? getString(R.string.weishezhi) : u.getSchool()));
            tvNick.setText(getString(R.string.nick_layout) + (u.getNick() == null ? getString(R.string.weishezhi) : u.getNick()));
            if (u.getPhotoUrl() != null) {
                XUtils.display(imgPhoto, u.getPhotoUrl());
            }
        }

        items.setAdapter(new CenterAdapter());*/
        init();
//        registerReceiver(receiver, new IntentFilter("com.wen.UPDATE_PWD"));
        registerReceiver(receiver, new IntentFilter("com.wen.MODE_CHANGED"));
    }

    private void init() {
        boolean isNight = SharePrefUtil.isNightMode(this);
        if (isNight) {
            setTheme(R.style.Night);
        } else {
            setTheme(R.style.AppTheme);
        }
        setContentView(R.layout.activity_center);
        ViewUtils.inject(this);

        title.setBackClickListener(clickLis);
        btExit.setOnClickListener(clickLis);

        u = ((MyApp) getApplication()).getUser();
        if (u != null) {
            tvAccount.setText(getString(R.string.account_layout) + (u.getPhone() == null ? (u.getEmail() == null ? getString(R.string.weishezhi) : u.getEmail()) : u.getPhone()));
            tvSchool.setText(getString(R.string.school_layout) + (u.getSchool() == null ? getString(R.string.weishezhi) : u.getSchool()));
            tvNick.setText(getString(R.string.nick_layout) + (u.getNick() == null ? getString(R.string.weishezhi) : u.getNick()));
            if (u.getPhotoUrl() != null) {
                XUtils.display(imgPhoto, u.getPhotoUrl());
            }
        }

        items.setAdapter(new CenterAdapter());
        registerReceiver(receiver, new IntentFilter("com.wen.UPDATE_PWD"));
    }

    @OnClick(R.id.center_private)
    public void click(View v) {
        UserInfoActivity.startActivity(this);
    }

    @OnItemClick(R.id.center_items)
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                SchoolActivity.startActivityForResult(CenterActivity.this);
                break;
            case 1:
                SafeActivity.startActivity(this);
                break;
            case 2:
                SettingActivity.startActivity(this);
                break;
            case 3:
                showFeedback();
                break;
        }
    }

    private FeedBackDialog dialog;

    private void showFeedback() {
        dialog = new FeedBackDialog(this);
        dialog.setOnClickListener(dialogLis);
        dialog.show();
    }

    private FeedBackDialog.OnClickListener dialogLis = new FeedBackDialog.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, String text) {
            feedback(text);
        }
    };

    private void feedback(String text) {
        if (text.length() < 15) {
            XUtils.show(R.string.need_15);
            return;
        }
        if (((MyApp) getApplication()).getUser().getToken() == null) {
            XUtils.show(R.string.please_login);
            finish();
            return;
        }
        RequestParams params = new RequestParams();
        params.addBodyParameter("f.token", String.valueOf(((MyApp) getApplication()).getUser().getToken()));
        params.addBodyParameter("f.content", text);
        XUtils.send(XUtils.FEEDBACK, params, new BasicRequestCallBack<Result<Boolean>>() {
            @Override
            public void success(Result<Boolean> data) {
                XUtils.show(data.descript);
                if (dialog != null && data.data) {
                    dialog.dismiss();
                }
            }
        }, true);
    }


    private View.OnClickListener clickLis = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.title_back:
                    finish();
                    break;
                case R.id.center_exit:
                   /* MyApp.setUser(null);
                    sendBroadcast(new Intent(Code.EXIT_LOGIN));*/
                    ((MyApp) getApplication()).setUser(null);
                    sendBroadcast(new Intent("com.wen.LOGIN_CHANGE"));
                    finish();
                    break;
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Code.REQ_UPDATE_SCHOOL && resultCode == Code.RESP_UPDATE_SCHOOL) {
/*
            try {
                String school= URLEncoder.encode(u.getSchool(),"UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }*/
            tvSchool.setText(u.getSchool() + " ");
        }
    }

    public static void startActivity(Context context) {
        Intent in = new Intent(context, CenterActivity.class);
        context.startActivity(in);
    }


    private class CenterAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return items_text.length;
        }

        @Override
        public String getItem(int position) {
            return items_text[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View v, ViewGroup parent) {

            ViewHolder holder = null;
            if (v == null) {
                v = getLayoutInflater().inflate(R.layout.layout_simple_list_item_2, null);
                holder = new ViewHolder();
                holder.icon = (ImageView) v.findViewById(R.id.list_item_2_icon);
                holder.tv = (TextView) v.findViewById(R.id.list_item_2_item);
                v.setTag(holder);
            } else {
                holder = (ViewHolder) v.getTag();
            }
            holder.icon.setImageResource(items_icons[position]);
            holder.tv.setText(items_text[position]);
            return v;
        }
    }

    class ViewHolder {
        ImageView icon;
        TextView tv;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            init();
            if (intent.getAction().equals("com.wen.UPDATE_PWD")) {
                finish();
            }
        }
    };


}
