package com.wen.college.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wen.college.R;
import com.wen.college.https.XUtils;

/**
 * Created by Administrator on 2016/9/4.
 */

public class UserInfoView extends RelativeLayout implements View.OnClickListener, View.OnFocusChangeListener {

    private TextView tvLabel;
    private ImageView img;
    private ImageView arr;
    private EditText etInput;
    private OnClickListener l;
    private boolean etEnable;
    private int value;

    public UserInfoView(Context context) {
        super(context);
        init(null);
    }

    public UserInfoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public UserInfoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    /*
    初始化布局
    * */
    private void init(AttributeSet attrs) {
        LayoutInflater.from(getContext()).inflate(R.layout.layout_userinfo, this);
        tvLabel = (TextView) findViewById(R.id.userinfo_label);
        img = (ImageView) findViewById(R.id.userinfo_img);
        arr = (ImageView) findViewById(R.id.userinfo_next);
        etInput = (EditText) findViewById(R.id.userinfo_input);
        etInput.setOnFocusChangeListener(this);
        if (attrs == null) {
            return;
        }
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.UserInfoView);
        int N = a.getIndexCount();
        for (int i = 0; i < N; i++) {
            int index = a.getIndex(i);
            switch (index) {
                case R.styleable.UserInfoView_uv_arr_visibility:
                    setVisibility(arr, a.getInt(index, 0));
                    break;
                case R.styleable.UserInfoView_uv_img_visibility:
                    setVisibility(img, a.getInt(index, 0));
                    break;
                case R.styleable.UserInfoView_uv_input_enable:
                    etEnable = a.getBoolean(index, false);
                    break;
                case R.styleable.UserInfoView_uv_input_visibility:
                    setVisibility(etInput, a.getInt(index, 0));
                    break;
                case R.styleable.UserInfoView_uv_input_hint:
                    etInput.setHint(a.getString(index));
                    break;
                case R.styleable.UserInfoView_uv_label_text:
                    tvLabel.setText(a.getString(index));
                    break;
                case R.styleable.UserInfoView_uv_input_text:
                    etInput.setText(a.getString(index));
                    break;
            }
        }
    }

    //设置可见性的方法
    private void setVisibility(View v, int visable) {
        switch (visable) {
            case 0:
                v.setVisibility(View.VISIBLE);
                break;
            case 1:
                v.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_UP) {
            onClick(this);
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        if (!etEnable) {
            if (l != null) {
                l.onClick(this);
            }
        } else {
            setEnabled(true);
            etInput.requestFocus();
        }
    }

    public void setOnClickListener(OnClickListener l) {
        this.l = l;
    }

    public void setText(String text) {
        etInput.setText(text);
    }

    public void setText(int resid) {
        etInput.setText(resid);
    }

    public void setEnabled(boolean enable) {
        etInput.setEnabled(enable);
    }

    public String getText() {
        return etInput.getText().toString().trim();
    }

    public void setRightImageUri(String uri) {
        XUtils.display(img, uri);
    }

    public void setRightImageBitmap(Bitmap bmp) {
        img.setImageBitmap(bmp);
    }

    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        if (!hasFocus) {
            setEnabled(false);
        }
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
