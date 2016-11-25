package com.wen.college.auxiliary;

import android.os.AsyncTask;
import android.widget.TextView;

import com.wen.college.R;

/**
 * Created by Administrator on 2016/8/21.
 */

public class CodeTimerTask extends AsyncTask<Void, Void, Void> {

    private int time = 90;
    private TextView textView;
    private static CodeTimerTask task;
    private static boolean isNew;
    private boolean isRun;

    //使用单例模式，使时间倒数时可以同步
    private CodeTimerTask() {
    }

    public static CodeTimerTask getInstence() {
        if (task == null) {
            task = new CodeTimerTask();
            isNew = true;
        }
        return task;
    }

    public void startTimer(TextView textView) {
        this.textView = textView;
        if (isNew) {
            execute();
        }
    }


    @Override
    protected void onPreExecute() {
        time = 90;
        isNew = false;
        isRun = true;
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            for (; time >= 0; time--) {
                publishProgress();
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected void onProgressUpdate(Void... values) {
        if (textView != null) {
            textView.setText(String.format("%ds", time));
            if (textView.isEnabled()) {
                textView.setEnabled(false);
            }
        }
    }

    protected void onPostExecute(Void aVoid) {
        end();
    }

    private void end() {
        if (textView != null) {
            textView.setEnabled(true);
            textView.setText(R.string.get_code);
        }
        cancel();
    }

    //取消计时
    public void cancel() {
        if (task != null) {
            isRun = false;
            super.cancel(true);
            task = null;
            isNew = true;
        }
    }

    public boolean isRun() {
        return isRun;
    }
}
