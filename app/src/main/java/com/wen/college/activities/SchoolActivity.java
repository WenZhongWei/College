package com.wen.college.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.view.ResType;
import com.lidroid.xutils.view.annotation.ResInject;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.wen.college.R;
import com.wen.college.auxiliary.Code;
import com.wen.college.entities.Result;
import com.wen.college.entities.User;
import com.wen.college.https.BasicRequestCallBack;
import com.wen.college.https.XUtils;
import com.wen.college.views.TitleView;
import com.wen.college.views.UserInfoView;

import java.util.Calendar;

/**
 * Created by Administrator on 2016/9/5.
 */
public class SchoolActivity extends BaseActivity {
    @ViewInject(R.id.school_title)
    private TitleView title;
    @ViewInject(R.id.school_role)
    private UserInfoView uvRole;
    @ViewInject(R.id.school_name)
    private UserInfoView uvSchool;
    @ViewInject(R.id.school_area)
    private UserInfoView uvArea;
    @ViewInject(R.id.school_department)
    private UserInfoView uvDepartment;
    @ViewInject(R.id.school_class)
    private UserInfoView uvClass;
    @ViewInject(R.id.school_year)
    private UserInfoView uvYear;
    @ViewInject(R.id.school_save)
    private Button btSave;
    @ResInject(id = R.array.role, type = ResType.StringArray)
    private String[] roles;
    private String[] years = new String[50];
    private User u;
    private boolean needUpdate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school);
        ViewUtils.inject(this);
        title.setBackClickListener(clickLis);
        btSave.setOnClickListener(clickLis);
        uvRole.setOnClickListener(clickLis);
        uvSchool.setOnClickListener(clickLis);
        uvYear.setOnClickListener(clickLis);

        u = ((MyApp) getApplication()).getUser();
        if (!TextUtils.isEmpty(u.getSchool())) {
            uvSchool.setText(u.getSchool());
        }
        if (!TextUtils.isEmpty(u.getArea())) {
            uvArea.setText(u.getArea());
        }
        if (!TextUtils.isEmpty(u.getDepartment())) {
            uvDepartment.setText(u.getDepartment());
        }
        if (!TextUtils.isEmpty(u.getGradeClass())) {
            uvClass.setText(u.getGradeClass());
        }
        if (!TextUtils.isEmpty(u.getYear())) {
            uvYear.setText(u.getYear());
        }
        if (u.getRoleId() != null) {
            uvRole.setValue(u.getRoleId());
            uvRole.setText(roles[u.getRoleId()]);
        }
    }

    private View.OnClickListener clickLis = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.title_back:
                    finish();
                    break;
                case R.id.school_save:
                    save();
                    break;
                case R.id.school_role:
                    showRole();
                    break;
                case R.id.school_name:
                    SchoolSelectActivity.startActivityForResult(SchoolActivity.this);
                    break;
                case R.id.school_year:
                    showYear();
                    break;
            }
        }
    };

    private void save() {
        RequestParams params = new RequestParams();
        params.addBodyParameter("u.token", u.getToken());
        int role = uvRole.getValue();
        if (role != -1 && role != u.getRoleId()) {
            params.addBodyParameter("u.roleId", String.valueOf(role));
            needUpdate = true;
        }
        String school = uvSchool.getText();
        Log.e("aaaa", "=====school====" + school);
        if (!TextUtils.isEmpty(school) && !school.equals(u.getSchool())) {
            params.addBodyParameter("u.school", school);
            needUpdate = true;
        }
        String area = uvArea.getText();
        Log.e("aaaa", "=====area====" + area);
        if (!TextUtils.isEmpty(area) && !area.equals(u.getArea())) {
            params.addBodyParameter("u.area", area);
            needUpdate = true;
        }
        String department = uvDepartment.getText();
        if (!TextUtils.isEmpty(department) && !department.equals(u.getDepartment())) {
            params.addBodyParameter("u.department", department);
            needUpdate = true;
        }
        String gradeClass = uvClass.getText();
        Log.e("aaaa", "=====gradeClass====" + gradeClass);
        if (!TextUtils.isEmpty(gradeClass) && !gradeClass.equals(u.getGradeClass())) {
            params.addBodyParameter("u.gradeClass", gradeClass);
            needUpdate = true;
        }
        String year = uvYear.getText();
        Log.e("aaaa", "=====year====" + year);
        if (!TextUtils.isEmpty(year) && !year.equals(u.getYear())) {
            params.addBodyParameter("u.year", year);
            needUpdate = true;
        }
        if (needUpdate) {
            XUtils.send(XUtils.UPINFO, params, new BasicRequestCallBack<Result<User>>() {
                @Override
                public void success(Result<User> data) {
                    XUtils.show(data.descript);
                    if (data.state == Result.STATE_SUC) {
                        ((MyApp) getApplication()).setUser(data.data);
                        setResult(Code.RESP_UPDATE_SCHOOL);
                        finish();
                    }
                }
            }, true);
        }
    }

    private void showYear() {
        if (years[0] == null) {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            for (int i = 0; i < years.length; i++) {
                years[i] = String.format("%d年", year - i);
            }
        }
        new AlertDialog.Builder(this).setItems(years, yearLis).show();
    }

    private void showRole() {
        Dialog dialog = new AlertDialog.Builder(this).setItems(roles, roleLis).show();
        dialog.getWindow().setGravity(Gravity.BOTTOM | Gravity.CENTER);
    }

    private DialogInterface.OnClickListener yearLis = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            uvYear.setText(years[which]);
        }
    };

    private DialogInterface.OnClickListener roleLis = new DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            uvRole.setValue(which);
            uvRole.setText(roles[which]);
            dialog.dismiss();
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Code.REQ_SELECT_SCHOOL && resultCode == Code.RESP_SELECT_SCHOOL && data != null) {
            uvSchool.setText(data.getStringExtra("name"));
            uvArea.setText(data.getStringExtra("area"));
        }
    }

    public static void startActivityForResult(Activity activity) {
        Intent in = new Intent(activity, SchoolActivity.class);
        activity.startActivityForResult(in, Code.REQ_UPDATE_SCHOOL);
    }
}
