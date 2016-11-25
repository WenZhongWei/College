package com.wen.college.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.wen.college.R;

/**
 * Created by Administrator on 2016/8/19.
 */

public class MEditText extends RelativeLayout implements View.OnFocusChangeListener, View.OnClickListener, View.OnTouchListener, TextWatcher {

    public static final int INPUTTYPE_NORMAL = 0;
    public static final int INPUTTYPE_PASSWORD = 1;
    public static final int INPUTTYPE_NUMBER = 2;
    private RelativeLayout rlRoot;
    private EditText etInput;
    private ImageView imgLable, imgDel, imgEye;
    private boolean delEnable, eyeEnable;
//    private int paddingTop, paddingBottom;

    public MEditText(Context context) {
        super(context);
        init(null);
    }

    public MEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public MEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    //初始化控件的方法
    public void init(AttributeSet attrs) {
        /*paddingTop = getContext().getResources().getDimensionPixelSize(R.dimen.medit_padding_top);
        paddingBottom = getContext().getResources().getDimensionPixelSize(R.dimen.medit_padding_bottom);*/

        LayoutInflater.from(getContext()).inflate(R.layout.layout_medit, this);

        rlRoot = (RelativeLayout) findViewById(R.id.medit_root);
        etInput = (EditText) findViewById(R.id.medit_input);
        imgLable = (ImageView) findViewById(R.id.medit_lable);
        imgDel = (ImageView) findViewById(R.id.medit_del);
        imgEye = (ImageView) findViewById(R.id.medit_eye);

        etInput.setOnFocusChangeListener(this);
        etInput.addTextChangedListener(this);
        imgDel.setOnClickListener(this);
        imgEye.setOnTouchListener(this);

        //判断是否为空
        if (attrs == null) {
            return;
        }
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.MEditText);
        int N = a.getIndexCount();
        for (int i = 0; i < N; i++) {
            int index = a.getIndex(i);
            switch (index) {
                case R.styleable.MEditText_me_text:
                    setText(a.getString(index));
                    break;
                case R.styleable.MEditText_me_del_enable:
                    delEnable = a.getBoolean(index, false);
                    break;
                case R.styleable.MEditText_me_lable_src:
                    imgLable.setImageDrawable(a.getDrawable(index));
                    break;
                case R.styleable.MEditText_me_hint:
                    setHint(a.getString(index));
                    break;
                case R.styleable.MEditText_me_eye_enable:
                    eyeEnable = a.getBoolean(index, false);
                    break;
                case R.styleable.MEditText_me_inputType:
                    setInputType(a.getInt(index, 0));
                    break;
                case R.styleable.MEditText_me_maxLength:
                    int max = a.getInt(index, -1);
                    if (max != -1) {
                        setMaxLength(max);
                    }
                    break;
            }
        }
    }

    //限制最大长度的方法
    public void setMaxLength(int maxLength) {
        etInput.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
    }

    //getText()方法
    public String getText() {
        return etInput.getText().toString().trim();
    }

    //设置一打开输入框就是密码状态
    public void setInputType(int type) {
        switch (type) {
            case INPUTTYPE_PASSWORD:
                etInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                break;
            case INPUTTYPE_NORMAL:
                etInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
                break;
            case INPUTTYPE_NUMBER:
                etInput.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_NORMAL);
                break;
        }
    }

    public void setText(String text) {
        etInput.setText(text);
    }

    public void setHint(String text) {
        etInput.setHint(text);
    }


    //两个图片的显示隐藏操作
    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        rlRoot.setSelected(hasFocus);

        if (delEnable && hasFocus && etInput.getText().length() > 0) {
            imgDel.setVisibility(View.VISIBLE);
        } else if (delEnable && !hasFocus) {
            imgDel.setVisibility(View.GONE);
        }

        if (eyeEnable && hasFocus) {
            imgEye.setVisibility(View.VISIBLE);
        } else if (eyeEnable) {
            imgEye.setVisibility(View.GONE);
        }
    }

    //输入框有内容时改变控件的大小
   /* @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        int hh = h - paddingBottom - paddingTop;
        imgLable.getLayoutParams().width = hh;
        imgDel.getLayoutParams().width = hh;
        imgEye.getLayoutParams().width = hh;
    }*/

    //点击删除图片时清空输入框内容
    @Override
    public void onClick(View view) {
        etInput.getText().clear();
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            setInputType(INPUTTYPE_NORMAL);
            return true;
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            setInputType(INPUTTYPE_PASSWORD);
            return true;

        }
        return false;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    //当输入框有内容时图案X显示，否则隐藏
    @Override
    public void afterTextChanged(Editable s) {
        if (delEnable && s.length() > 0) {
            imgDel.setVisibility(View.VISIBLE);
        } else if (delEnable) {
            imgDel.setVisibility(View.GONE);
        }
    }

    //文本改变监听方法
    public void addTextChangeListener(TextWatcher watcher) {
        etInput.addTextChangedListener(watcher);
    }
}
