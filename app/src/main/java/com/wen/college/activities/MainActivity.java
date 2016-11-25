package com.wen.college.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageButton;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.viewpagerindicator.TabPageIndicator;
import com.wen.college.R;
import com.wen.college.auxiliary.Code;
import com.wen.college.entities.User;
import com.wen.college.views.TitleView;

/**
 * Created by Administrator on 2016/8/20.
 */

public class MainActivity extends BaseActivity {
    @ViewInject(R.id.main_title)
    private TitleView title;
    @ViewInject(R.id.main_idicator)
    private TabPageIndicator tabsIndicator;
    @ViewInject(R.id.main_content)
    private ViewPager pager;
    @ViewInject(R.id.main_add_tab)
    private ImageButton btAdd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewUtils.inject(this);
        title.setRightClickListener(clickLis);
        pager.setAdapter(new PageAdapter(getSupportFragmentManager()));
        tabsIndicator.setViewPager(pager);
        btAdd.setOnClickListener(clickLis);
        registerReceiver(receiver, new IntentFilter("com.wen.LOGIN_COMPLETE"));
    }

    private View.OnClickListener clickLis = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.title_img_right:
                    if (((MyApp) getApplication()).getUser() == null) {
                        LoginActivity.startActivity(MainActivity.this, true);
                    } else {
                        CenterActivity.startActivity(MainActivity.this);
                    }
                    break;
                case R.id.main_add_tab:
                    SelectTabsActivity.startActivityForResult(MainActivity.this);
                    break;
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Code.REQ_SELECT_TAB && resultCode == Code.RESP_SELECT_TAB) {
            tabsIndicator.notifyDataSetChanged();
            pager.getAdapter().notifyDataSetChanged();
        }
    }

    class PageAdapter extends FragmentPagerAdapter {

        public PageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = new Fragment();
            return fragment;
        }

        @Override
        public int getCount() {
            return ((MyApp) getApplication()).getMyTabs().size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return ((MyApp) getApplication()).getMyTabs().get(position).getTab();
        }
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if ("com.wen.LOGIN_CHANGE".equals(intent.getAction())) {
                User u = ((MyApp) getApplication()).getUser();
                if (u == null) {
                    title.setImageResource(R.drawable.photo_unlogin);
                } else {
                    title.setImageUrl(u.getPhotoUrl());
                }
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }
}
