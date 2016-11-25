package com.wen.college.https;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.wen.college.R;
import com.wen.college.utils.Loading;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by Administrator on 2016/8/20.
 * 将RequestCallBack封装成方法
 */

public abstract class BasicRequestCallBack<T> extends RequestCallBack<String> {
    private Type type;

    //使用构造方法使其可以用相应的泛型解析
    public BasicRequestCallBack() {
        Type superClass = this.getClass().getGenericSuperclass();
        this.type = ((ParameterizedType) superClass).getActualTypeArguments()[0];
    }

    @Override
    public void onSuccess(ResponseInfo<String> responseInfo) {
        Loading.dismiss();
        if (responseInfo != null) {
            String json = responseInfo.result;
            if (json.matches("^\\{.*\\}$")) {
                T t = JSON.parseObject(json, type);
                if (t != null) {
                    success(t);
                } else {
                    XUtils.show(R.string.no_data_return);
                }
            } else {
                XUtils.show(R.string.data_format_error);
            }
        } else {
            XUtils.show(R.string.data_load_fail);
        }
    }

    @Override
    public void onFailure(HttpException e, String s) {
        Loading.dismiss();
        XUtils.show(R.string.network_fail);
        Log.e("college", "=====error=====" + s);
        e.printStackTrace();
        failure();
    }

    public abstract void success(T data);

    public void failure() {

    }
}
