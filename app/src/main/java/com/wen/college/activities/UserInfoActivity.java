package com.wen.college.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.wen.college.R;
import com.wen.college.auxiliary.Code;
import com.wen.college.entities.Result;
import com.wen.college.entities.User;
import com.wen.college.https.BasicRequestCallBack;
import com.wen.college.https.XUtils;
import com.wen.college.views.TitleView;
import com.wen.college.views.UserInfoView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Administrator on 2016/9/4.
 */
public class UserInfoActivity extends BaseActivity {
    @ViewInject(R.id.userinfo_photo)
    private UserInfoView mePhoto;
    @ViewInject(R.id.userinfo_name)
    private UserInfoView meName;
    @ViewInject(R.id.userinfo_nick)
    private UserInfoView meNick;
    @ViewInject(R.id.userinfo_gender)
    private UserInfoView meGender;
    @ViewInject(R.id.userinfo_title)
    private TitleView title;
    @ViewInject(R.id.userinfo_save)
    private Button btSave;
    private int size;
    private InputStream is;
    private User u;
    private int gender = -1;
    private boolean needUpdate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userinfo);
        ViewUtils.inject(this);
        title.setBackClickListener(clickLis);
        btSave.setOnClickListener(clickLis);
        mePhoto.setOnClickListener(clickLis);
        meGender.setOnClickListener(clickLis);
        initData();
    }

    private void initData() {
        u = ((MyApp) getApplication()).getUser();
        if (u == null) {
            XUtils.show(R.string.error);
            finish();
            return;
        }
        mePhoto.setRightImageUri(u.getPhotoUrl());
        meGender.setText(String.valueOf(u.getGender() == 1 ? getString(R.string.gril) : getString(R.string.boy)));
       /* meName.setText(u.getName() == null ? getString(R.string.weishezhi) : u.getName());
        meNick.setText(u.getNick() == null ? getString(R.string.weishezhi) : u.getNick());*/
        if (u.getName() != null) meName.setText(u.getName());
        if (u.getNick() != null) meNick.setText(u.getNick());
    }

    private View.OnClickListener clickLis = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.userinfo_title:
                    finish();
                    break;
                case R.id.userinfo_save:
                    save();
                    break;
                case R.id.userinfo_gender:
                    showGender();
                    break;
                case R.id.userinfo_photo:
                    selectPhoto();
                    break;
            }
        }
    };

    private void save() {
        RequestParams params = new RequestParams();
        params.addBodyParameter("u.token", u.getToken());
        if (is != null) {
            params.addBodyParameter("u.photoUrl", is, size, System.currentTimeMillis() + ".png");
            needUpdate = true;
        }
        if (!meName.getText().equals(u.getName()) && !meName.equals("")) {
            params.addBodyParameter("u.name", meName.getText());
            needUpdate = true;
        }
        if (!meNick.getText().equals(u.getNick()) && !meNick.equals("")) {
            params.addBodyParameter("u.nick", meNick.getText());
            needUpdate = true;
        }
        if (gender != -1 && gender != u.getGender()) {
            params.addBodyParameter("u.gender", String.valueOf(gender));
            needUpdate = true;
        }
        if (needUpdate) {
            XUtils.send(XUtils.UPINFO, params, new BasicRequestCallBack<Result<User>>() {
                @Override
                public void success(Result<User> data) {
                    if(data!=null){
                        XUtils.show(data.descript);
                        if (data.state == Result.STATE_SUC) {
                            ((MyApp) getApplication()).setUser(data.data);
                            finish();
                        }
                    }else {
                        XUtils.show(R.string.not_need_save);
                    }
                }
            }, true);
        }
    }

    private void selectPhoto() {
        Intent in = new Intent(Intent.ACTION_PICK);
        in.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(in, Code.REQ_SELECT_PHOTO);
    }

    private void showGender() {
        new AlertDialog.Builder(this).setItems(new String[]{"男", "女"}, dialogClick).show();
    }

    private DialogInterface.OnClickListener dialogClick = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case 1:
                    gender = 1;
                    meGender.setText(R.string.gril);
                    break;
                case 0:
                    gender = 0;
                    meGender.setText(R.string.boy);
                    break;
            }
        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //图片选择后触发的方法
        if (requestCode == Code.REQ_SELECT_PHOTO && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            String[] projection = new String[]{MediaStore.Images.Media.DATA};
            Cursor c = getContentResolver().query(uri, projection, null, null, null);
            if (c != null && c.moveToFirst()) {
                String path = c.getString(c.getColumnIndex(projection[0]));
                toCurPhoto(path);
            }
        }//剪切图片后的触发的方法
        else if (requestCode == Code.REQ_CUT_PHOTO && resultCode == RESULT_OK && data != null) {
            Bitmap bmp = data.getParcelableExtra("data");
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.PNG, 70, bos);
            size = bos.size();
            is = new ByteArrayInputStream(bos.toByteArray());
            mePhoto.setRightImageBitmap(bmp);
            try {
                bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //剪切图片
    private void toCurPhoto(String path) {
        Intent in = new Intent("com.android.camera.action.CROP");
        in.setDataAndType(Uri.fromFile(new File(path)), "image/*");
        in.putExtra("crop", "true");
        in.putExtra("aspectX", 1);
        in.putExtra("aspectY", 1);
        in.putExtra("outputX", 300);
        in.putExtra("outputY", 300);
        in.putExtra("return-data", true);
        startActivityForResult(in, Code.REQ_CUT_PHOTO);
    }

    public static void startActivity(Context context) {
        Intent in = new Intent(context, UserInfoActivity.class);
        context.startActivity(in);
    }
}
