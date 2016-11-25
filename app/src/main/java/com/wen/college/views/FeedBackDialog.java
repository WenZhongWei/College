package com.wen.college.views;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.wen.college.R;

/**
 * Created by Administrator on 2016/9/5.
 */

public class FeedBackDialog extends Dialog implements TextWatcher, View.OnClickListener {

    private EditText etInput;
    private TextView tvNums;
    private Button btOk;
    private final int MAXNUMS = 140;
    private OnClickListener l;

    public FeedBackDialog(Context context) {
        this(context, R.style.feedback_Dialog);
    }

    public FeedBackDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    protected FeedBackDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_feedback);
        etInput = (EditText) findViewById(R.id.feedback_suggest);
        tvNums = (TextView) findViewById(R.id.feedback_nums);
        btOk = (Button) findViewById(R.id.feedback_ok);

        etInput.addTextChangedListener(this);
        btOk.setOnClickListener(this);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int count, int after) {

    }

    /**
     * 控制只能输入140个字
     *
     * @param s
     */
    @Override
    public void afterTextChanged(Editable s) {

        if (s.length() > 140) {
            s.delete(140, s.length());
        } else {
            tvNums.setText(String.valueOf(MAXNUMS - s.length()));
        }
    }


    @Override
    public void onClick(View v) {
        if (l != null) {
            l.onClick(this, etInput.getText().toString());
        }
    }

    /**
     * 自定义接口用于给onClick（）方法发送建议
     */
    public interface OnClickListener {
        void onClick(DialogInterface dialog, String text);
    }

    public void setOnClickListener(OnClickListener l) {
        this.l = l;
    }
}
