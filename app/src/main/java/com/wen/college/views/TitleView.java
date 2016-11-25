package com.wen.college.views;


import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wen.college.R;
import com.wen.college.https.XUtils;

/**
 * Created by Administrator on 2016/8/18.
 * 自定义TITLE
 */

public class TitleView extends RelativeLayout {

    private TextView title, tvRight;
    private ImageView back, imgRight;
    private String currUrl;//记录当前地址

    public TitleView(Context context) {
        super(context);
        init(null);
    }

    public TitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public TitleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    //初始化的方法
    public void init(AttributeSet attrs) {
        //初始化页面
        LayoutInflater.from(getContext()).inflate(R.layout.layout_title, this);
        title = (TextView) findViewById(R.id.title_title);
        tvRight = (TextView) findViewById(R.id.title_tv_right);
        back = (ImageView) findViewById(R.id.title_back);
        imgRight = (ImageView) findViewById(R.id.title_img_right);

        if (attrs == null) {
            return;
        }

        //加载在attrs.xml文件中定义好的名为TitleView的样式
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.TitleView);
        int N = a.getIndexCount();
        for (int i = 0; i < N; i++) {
            int index = a.getIndex(i);
            switch (index) {
                case R.styleable.TitleView_tv_back_visibility:
                    setVisibility(back, a.getInt(index, 0));
                    break;
                case R.styleable.TitleView_tv_right:
                    tvRight.setText(a.getString(index));
                    break;
                case R.styleable.TitleView_tv_tv_right_visibility:
                    setVisibility(tvRight, a.getInt(index, 2));
                    break;
                case R.styleable.TitleView_tv_img_right_visibility:
                    setVisibility(imgRight, a.getInt(index, 2));
                    break;
                case R.styleable.TitleView_tv_title:
                    title.setText(a.getString(index));
                    break;
            }
        }

    }

    //设置隐藏的方法
    private void setVisibility(View v, int visable) {
        switch (visable) {
            case 0:
                v.setVisibility(View.VISIBLE);
                break;
            case 1:
                v.setVisibility(View.INVISIBLE);
                break;
            case 2:
                v.setVisibility(View.GONE);
                break;
        }
    }

    //添加修改Title的方法
    public void setTitle(String text) {
        title.setText(text);
    }

    //添加修改右边文字的方法
    public void setRightText(String text) {
        tvRight.setText(text);
    }

    //添加back按钮的点击事件
    public void setBackClickListener(View.OnClickListener l) {
        back.setOnClickListener(l);
    }

    //添加右边文字和图片的点击事件
    public void setRightClickListener(View.OnClickListener l) {
        tvRight.setOnClickListener(l);
        imgRight.setOnClickListener(l);
    }

    public void setImageResource(int srcId) {
        currUrl = null;
        imgRight.setImageResource(srcId);
    }

    //添加更改图片的方法
    public void setImageUrl(String url) {

        //若地址没有改变，则不用重新加载
        if (url.equals(currUrl)) {
            return;
        }
        //否则调用XUtils里面更改图片的方法
        currUrl = url;
        XUtils.display(imgRight, currUrl);
    }

    //右边文字是否显示的方法
    public void setRightTextVisibility(int visibility) {
        tvRight.setVisibility(visibility);
    }

}
