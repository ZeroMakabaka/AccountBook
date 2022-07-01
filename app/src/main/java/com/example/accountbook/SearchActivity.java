package com.example.accountbook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.accountbook.activity_adater.MainAccountAdapter;
import com.example.accountbook.db.DBManager;
import com.example.accountbook.db.entity.AccountBean;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener {
    // 控件
    RecyclerView searchRv; //搜索内容
    EditText searchEt;
    TextView emptyTv;
    ImageView backIv;

    // 数据源
    private List<AccountBean> recordDatas;
    private MainAccountAdapter adapter;
    private int year;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settingStatusBar();
        setContentView(R.layout.activity_search);
        initView();
    }

    // 状态栏设置
    public  void settingStatusBar() {
        if (Build.VERSION.SDK_INT >= 21) {//21表示5.0
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= 19) {//19表示4.4
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        }

    }

    public void initView() {
        searchRv = findViewById(R.id.search_rv);
        searchEt = findViewById(R.id.search_et);
        emptyTv = findViewById(R.id.search_tv_empty);
        backIv = findViewById(R.id.search_iv_back);

        initRecordDatas();
        adapter = new MainAccountAdapter(this,recordDatas,false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        searchRv.setLayoutManager(layoutManager);
        searchRv.setAdapter(adapter);
        searchRv.setVisibility(View.VISIBLE);

        backIv.setOnClickListener(this);
        searchEt.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER){

                    if (TextUtils.isEmpty(searchEt.getText().toString())) {
                        emptyTv.setVisibility(View.GONE);
                        searchRv.setVisibility(View.VISIBLE);
                        recordDatas.clear();

                        List<AccountBean> searchOutcome = DBManager.getAccountListByYearWithoutKind(year);
                        recordDatas.addAll(searchOutcome);
                        adapter.notifyDataSetChanged();

                        Log.e("TAG", "onTextChanged: "+recordDatas);
                    }else {
                        String msg = searchEt.getText().toString().trim();
                        // 开始搜索
                        List<AccountBean> searchOutcome = DBManager.getAccountListByDesc(msg);
                        if (searchOutcome.size()==0 && !TextUtils.isEmpty(msg)) {
                            emptyTv.setVisibility(View.VISIBLE);
                            searchRv.setVisibility(View.GONE);
                            recordDatas.clear();
                            recordDatas.addAll(searchOutcome);
                            Log.e("TAG", "输出的结果为空" );
                            adapter.notifyDataSetChanged();

                        }else {
                            emptyTv.setVisibility(View.GONE);
                            searchRv.setVisibility(View.VISIBLE);
                            recordDatas.clear();
                            recordDatas.addAll(searchOutcome);
                            adapter.notifyDataSetChanged();

                        }
                    }
                    return true;
                }
                return false;
            }
        });
/*        searchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                Log.e("输入前确认执行该方法", "开始输入");
            }

            // 根据输入内容变化监听
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.e("输入过程中执行该方法", "文字变化");

            }

            @Override
            public void afterTextChanged(Editable editable) {
                Log.e("输入结束执行该方法", "输入结束");

                if (TextUtils.isEmpty(searchEt.getText().toString())) {
                    emptyTv.setVisibility(View.GONE);
                    searchRv.setVisibility(View.VISIBLE);
                    List<AccountBean> searchOutcome = DBManager.getAccountListByYear(year, 0);
                    recordDatas.addAll(searchOutcome);
                    adapter.notifyDataSetChanged();

                    Log.e("TAG", "onTextChanged: "+recordDatas);
                }else {
                    String msg = searchEt.getText().toString().trim();
                    // 开始搜索
                    List<AccountBean> searchOutcome = DBManager.getAccountListByDesc(msg);
                    if (searchOutcome.size()==0 && !TextUtils.isEmpty(msg)) {
                        emptyTv.setVisibility(View.VISIBLE);
                        searchRv.setVisibility(View.GONE);
                        recordDatas.clear();
                        Log.e("TAG", "输出的结果为空" );
                        adapter.notifyDataSetChanged();
                    }else {
                        emptyTv.setVisibility(View.GONE);
                        searchRv.setVisibility(View.VISIBLE);
                        recordDatas.clear();
                        recordDatas.addAll(searchOutcome);
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        });*/
    }

    // 初始化数据，展示一年来的支出流水
    public void initRecordDatas() {
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        recordDatas = new ArrayList<>();
        recordDatas = DBManager.getAccountListByYearWithoutKind(year);;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.search_iv_back:
                finish();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }
}