package com.wen.college.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.wen.college.R;
import com.wen.college.auxiliary.Code;
import com.wen.college.entities.Tab;
import com.wen.college.https.XUtils;
import com.wen.college.views.TitleView;

import java.util.List;

/**
 * Created by Administrator on 2016/9/12.
 */

public class SelectTabsActivity extends BaseActivity {
    @ViewInject(R.id.tabs_title)
    private TitleView title;
    @ViewInject(R.id.tabs_my)
    private GridView gvMy;
    @ViewInject(R.id.tabs_other)
    private GridView gvOther;


//    private  boolean showText = true;
    private TabAdapter myAdp;
    private TabAdapter otherAdp;
    private View moveView;
    private View startView;
    private int[] locStart = new int[2];
    private int[] locEnd = new int[2];
    private boolean toMy;
    private FrameLayout flDecor;
    private int position;
    private boolean isAnimationing;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_tabs);
        ViewUtils.inject(this);
        title.setBackClickListener(clickLis);
        title.setRightClickListener(clickLis);
        myAdp = new TabAdapter(((MyApp) getApplication()).getMyTabs());
        gvMy.setAdapter(myAdp);
        otherAdp = new TabAdapter(((MyApp) getApplication()).getOtherTabs());
        gvOther.setAdapter(otherAdp);
        gvMy.setOnItemClickListener(itemClickLi);
        gvOther.setOnItemClickListener(itemClickLi);
        initDecor();
    }

    private AdapterView.OnItemClickListener itemClickLi = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (!isAnimationing) {
                isAnimationing = true;
                moveView = getMoveView(view);
                view.getLocationInWindow(locStart);
                SelectTabsActivity.this.position = position;
                Tab t = ((TabAdapter) parent.getAdapter()).getItem(position);
                otherAdp.setShowText(false);
                myAdp.setShowText(false);

                switch (parent.getId()) {
                    case R.id.tabs_my:
                        toMy = false;
                        t.setIsmy(1);
                        otherAdp.add(t);
                        otherAdp.setHiddenPosition(otherAdp.getCount()-1);
                        myAdp.setHiddenPosition(position);
                        gvOther.getViewTreeObserver().addOnGlobalLayoutListener(clobalLis);
                        break;
                    case R.id.tabs_other:
                        toMy = true;
                        t.setIsmy(0);
                        myAdp.add(t);
                        myAdp.setHiddenPosition(myAdp.getCount()-1);
                        otherAdp.setHiddenPosition(position);
                        gvMy.getViewTreeObserver().addOnGlobalLayoutListener(clobalLis);
                        break;
                }
                otherAdp.notifyDataSetChanged();
                myAdp.notifyDataSetChanged();
            }
        }
    };

    private ViewTreeObserver.OnGlobalLayoutListener clobalLis = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
            if (toMy) {
                gvMy.getChildAt(gvMy.getLastVisiblePosition()).getLocationInWindow(locEnd);
                gvMy.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            } else {
                gvOther.getChildAt(gvOther.getLastVisiblePosition()).getLocationInWindow(locEnd);
                gvOther.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
            startAnimation(moveView);
        }
    };

    private ImageView getMoveView(View v) {
        v.setDrawingCacheEnabled(true);
        Bitmap bmp = v.getDrawingCache();
        ImageView img = new ImageView(this);
        img.setImageBitmap(bmp);
        img.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        return img;
    }

    private ImageView getMoveView() {
        startView.setDrawingCacheEnabled(true);
        Bitmap bmp = startView.getDrawingCache();
        ImageView img = new ImageView(this);
        img.setImageBitmap(bmp);
        img.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        return img;
    }

    private void startAnimation(View moveView) {
        flDecor.addView(moveView);
        TranslateAnimation animation = new TranslateAnimation(locStart[0], locEnd[0], locStart[1], locEnd[1]);
        animation.setDuration(500);
        animation.setFillAfter(true);
        animation.setAnimationListener(animLis);
        moveView.startAnimation(animation);
    }

    private Animation.AnimationListener animLis = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {
            flDecor.removeAllViews();
            otherAdp.setShowText(true);
            myAdp.setShowText(true);
            if (toMy) {
                otherAdp.remove(position);
            } else {
                myAdp.remove(position);
            }
            otherAdp.notifyDataSetChanged();
            myAdp.notifyDataSetChanged();
            isAnimationing = false;
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    };

    private void initDecor() {
        ViewGroup root = (ViewGroup) getWindow().getDecorView();
        flDecor = new FrameLayout(this);
        flDecor.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        root.addView(flDecor);
    }

    private View.OnClickListener clickLis = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.title_back:
                    finish();
                    break;
                case R.id.title_tv_right:
                    XUtils.updateTabs(((MyApp) getApplication()).getMyTabs());
                    XUtils.updateTabs(((MyApp) getApplication()).getOtherTabs());
                    setResult(Code.RESP_SELECT_TAB);
                    finish();
                    break;
            }
        }
    };

    public static void startActivityForResult(Activity activity) {
        Intent in = new Intent(activity, SelectTabsActivity.class);
        activity.startActivityForResult(in, Code.REQ_SELECT_TAB);
    }


    class TabAdapter extends BaseAdapter {

        private List<Tab> tabs;
        private boolean showText = true;
        private int hiddenPosition = -1;

        public TabAdapter(List<Tab> tabs) {
            this.tabs = tabs;
        }

        @Override
        public int getCount() {
            return tabs != null ? tabs.size() : 0;
        }

        @Override
        public Tab getItem(int position) {
            return tabs.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View v, ViewGroup parent) {
            TextView tv = null;
            if (v == null) {
                v = getLayoutInflater().inflate(R.layout.layout_item_tab, null);
                tv = (TextView) v.findViewById(R.id.tab_item_tv);
                v.setTag(tv);
            } else {
                tv = (TextView) v.getTag();
            }
            tv.setText(getItem(position).getTab());
            if (position == tabs.size() - 1 && !showText && position == hiddenPosition) {
                tv.setTextColor(Color.TRANSPARENT);

            } else {
                tv.setTextColor(getResources().getColor(R.color.title_text_color));

            }
            return v;
        }

        public void add(Tab tab) {
            this.tabs.add(tab);
        }

        public void remove(int position) {
            this.tabs.remove(position);
        }

        public void setShowText(boolean showText) {
            this.showText = showText;
        }

        public void setHiddenPosition(int hiddenPosition) {
            this.hiddenPosition = hiddenPosition;
        }
    }
}
