package com.wen.college.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.lidroid.xutils.view.annotation.event.OnItemClick;
import com.wen.college.R;
import com.wen.college.auxiliary.Code;
import com.wen.college.auxiliary.MyTextWatcher;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Administrator on 2016/9/6.
 */
public class SchoolSelectActivity extends BaseActivity {

    @ViewInject(R.id.select_input)
    private EditText etInput;
    @ViewInject(R.id.select_items)
    private ListView items;

    private SuggestionSearch search;
    private SuggestAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_select_school);
        ViewUtils.inject(this);
        search = SuggestionSearch.newInstance();
        search.setOnGetSuggestionResultListener(listener);
        etInput.addTextChangedListener(watcher);
        adapter = new SuggestAdapter();
        items.setAdapter(adapter);
    }

    @OnItemClick(R.id.select_items)
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        SuggestionResult.SuggestionInfo info = adapter.getItem(position);
        Intent in = new Intent();
        in.putExtra("name", info.key);
        in.putExtra("area", info.city + " " + info.district);
        setResult(Code.RESP_SELECT_SCHOOL, in);
        finish();
    }

    private MyTextWatcher watcher = new MyTextWatcher() {
        @Override
        public void afterTextChanged(Editable s) {
            if (s.length() > 0) {
                searchSuggest();
            }
        }
    };

    private OnGetSuggestionResultListener listener = new OnGetSuggestionResultListener() {
        @Override
        public void onGetSuggestionResult(SuggestionResult suggestionResult) {
            if (suggestionResult != null && suggestionResult.error == SearchResult.ERRORNO.NO_ERROR) {
                List<SuggestionResult.SuggestionInfo> infos = suggestionResult.getAllSuggestions();
                if (infos != null) {
                    adapter.addAll(infos);
                }
            }
        }
    };

    @OnClick({R.id.select_back, R.id.select_search})
    public void clickLis(View v) {
        switch (v.getId()) {
            case R.id.select_back:
                finish();
                break;
            case R.id.select_search:
                searchSuggest();
                break;
        }
    }

    private void searchSuggest() {
        String str = etInput.getText().toString().trim();
        if (!TextUtils.isEmpty(str)) {
            SuggestionSearchOption option = new SuggestionSearchOption();
            option.city("");
            option.keyword(str);
            search.requestSuggestion(option);
        }
    }


    public static void startActivityForResult(Activity activity) {
        Intent in = new Intent(activity, SchoolSelectActivity.class);
        activity.startActivityForResult(in, Code.REQ_SELECT_SCHOOL);
    }

    class SuggestAdapter extends BaseAdapter {

        List<SuggestionResult.SuggestionInfo> list = new ArrayList<>();

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public SuggestionResult.SuggestionInfo getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View v, ViewGroup parent) {
            ViewHolder holder = null;
            if (v == null) {
                v = getLayoutInflater().inflate(R.layout.layout_item_suggest, null);
                holder = new ViewHolder();
                holder.tvArea = (TextView) v.findViewById(R.id.item_sug_area);
                holder.tvName = (TextView) v.findViewById(R.id.item_sug_name);
                v.setTag(holder);
                //// TODO: 2016/9/6

            } else holder = (ViewHolder) v.getTag();
            SuggestionResult.SuggestionInfo info = getItem(position);
            holder.tvName.setText(info.key);
            holder.tvArea.setText(info.city + " " + info.district);
            return v;
        }

        public void addAll(List<SuggestionResult.SuggestionInfo> infos) {
            Iterator<SuggestionResult.SuggestionInfo> it = infos.iterator();
            while (it.hasNext()) {
                SuggestionResult.SuggestionInfo info = it.next();
                if (!info.key.matches(".*(学校|学院|校区|大学)") || TextUtils.isEmpty(info.city)) {
                    it.remove();
                }
            }
            if (infos.size() <= 0) {
                return;
            }
            list.clear();
            list.addAll(infos);
            notifyDataSetChanged();
        }

    }

    class ViewHolder {
        TextView tvArea;
        TextView tvName;
    }
}
