package com.example.accountbook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.accountbook.activity_adater.MainAccountAdapter;
import com.example.accountbook.db.DBManager;
import com.example.accountbook.db.entity.AccountBean;
import com.example.accountbook.utils.SelectYearMonthDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class HistoryActivity extends AppCompatActivity implements View.OnClickListener {
    RecyclerView historyRvOut, historyRvIn;
    TextView timeTv,noDataOutTv,noDataInTv;
    ImageView timeIv, backIv;
    private List<AccountBean> historyOutcome, historyIncome;
    private MainAccountAdapter outAdapter, inAdapter;
    private int year, month;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settingStatusBar();
        setContentView(R.layout.activity_history);
        initTime();
        initData();
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
        historyRvOut = findViewById(R.id.history_rv);
        historyRvIn = findViewById(R.id.history_rv_in);
        timeTv = findViewById(R.id.history_tv_time);
        timeIv = findViewById(R.id.history_iv_calendar);
        backIv = findViewById(R.id.history_iv_back);
        noDataOutTv = findViewById(R.id.history_tv_out_not_msg);
        noDataInTv = findViewById(R.id.history_tv_in_not_msg);


        timeIv.setOnClickListener(this);
        backIv.setOnClickListener(this);

        timeTv.setText(year + "年" + month + "月");


        // 设置适配器
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        outAdapter = new MainAccountAdapter(this, historyOutcome,false);
        historyRvOut.setLayoutManager(layoutManager);
        historyRvOut.setAdapter(outAdapter);

        LinearLayoutManager layoutManagerIn = new LinearLayoutManager(this);
        layoutManagerIn.setOrientation(RecyclerView.VERTICAL);
        inAdapter = new MainAccountAdapter(this, historyIncome,false);
        historyRvIn.setLayoutManager(layoutManagerIn);
        historyRvIn.setAdapter(inAdapter);

    }

    private void initData() {
        historyOutcome = new ArrayList<>();
        historyIncome = new ArrayList<>();
        // 支出
        List<AccountBean> outList = DBManager.getAccountListByMonth(year, month, 0);
        historyOutcome.clear();
        historyOutcome.addAll(outList);
        if (historyOutcome.size()==0) {
            historyRvOut.setVisibility(View.GONE);
        }
        // 收入
        List<AccountBean> inList = DBManager.getAccountListByMonth(year, month, 1);
        historyIncome.clear();
        historyIncome.addAll(inList);
        if (historyIncome.size()==0) {
            historyRvIn.setVisibility(View.GONE);
        }

        //adapter.notifyDataSetChanged();
    }


    public void initTime() {
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.history_iv_calendar:

                showDateDialog();
                break;
            case R.id.history_iv_back:
                finish();
                break;
        }
    }

    // 显示日期框
    public void showDateDialog() {
        SelectYearMonthDialog yearMonthDialog = new SelectYearMonthDialog(this);
        yearMonthDialog.show();
        // 处理传递过来的年份月份信息
        yearMonthDialog.setOnEnsureListener(new SelectYearMonthDialog.OnEnsureListener() {
            @Override
            public void onEnsure(int year, int month) {
                timeTv.setText(year+"年"+month+"月");
                // 更新数据
                // 支出
                List<AccountBean> outList = DBManager.getAccountListByMonth(year, month, 0);
                historyOutcome.clear();
                historyOutcome.addAll(outList);
                if (historyOutcome.size()==0) {
                    historyRvOut.setVisibility(View.GONE);
                    noDataOutTv.setVisibility(View.VISIBLE);
                }
                // 收入
                List<AccountBean> inList = DBManager.getAccountListByMonth(year, month, 1);
                historyIncome.clear();
                historyIncome.addAll(inList);
                if (historyIncome.size()==0) {
                    historyRvIn.setVisibility(View.GONE);
                    noDataInTv.setVisibility(View.VISIBLE);
                }

                outAdapter.notifyDataSetChanged();
                inAdapter.notifyDataSetChanged();

            }
        });
    }
}