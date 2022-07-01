package com.example.accountbook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.accountbook.db.DBManager;
import com.example.accountbook.frag_chart.IncomeChartFragment;
import com.example.accountbook.frag_chart.OutcomeChartFragment;
import com.example.accountbook.frag_chart.adapter.ChartVpAdapter;
import com.example.accountbook.utils.SelectYearMonthDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ChartAnalyseActivity extends AppCompatActivity implements View.OnClickListener {
    Button inBtn,outBtn;
    TextView dateTv,inTv,outTv;
    ViewPager2 chartVp;
    ImageView backIv,dateIv;
    private int year,month;
    List<Fragment> chartFragList;
    private IncomeChartFragment incomeChartFragment;
    private OutcomeChartFragment outcomeChartFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settingStatusBar();
        setContentView(R.layout.activity_chart_analyse);
        initView();
        initTime();
        initStaticsData(year,month);
        initFrag();
        setVpListener();
    }

    /*
    *   初始化某年某一个月的统计情况
    * */
    private void initStaticsData(int year, int month) {
        double outMoney = DBManager.getAllMoneyOneMonthByKind(year, month, 0);
        double inMoney = DBManager.getAllMoneyOneMonthByKind(year, month, 1);

        dateTv.setText(year+" 年"+ month +" 月账单");
        outTv.setText("￥ "+ outMoney);
        inTv.setText("￥ "+ inMoney);
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
        inBtn = findViewById(R.id.chart_btn_in);
        outBtn = findViewById(R.id.chart_btn_out);
        inTv = findViewById(R.id.chart_tv_in);
        outTv = findViewById(R.id.chart_tv_out);
        backIv = findViewById(R.id.chart_iv_back);
        chartVp = findViewById(R.id.chart_vp);
        dateTv = findViewById(R.id.chart_tv_date);
        dateIv = findViewById(R.id.chart_iv_calendar);

        backIv.setOnClickListener(this);
        inBtn.setOnClickListener(this);
        outBtn.setOnClickListener(this);
        dateIv.setOnClickListener(this);


    }
    public void initTime() {
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH)+1;

    }

    public void initFrag() {
        chartFragList = new ArrayList<>();
        incomeChartFragment = new IncomeChartFragment();
        outcomeChartFragment = new OutcomeChartFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("year",year);
        bundle.putInt("month",month);
        incomeChartFragment.setArguments(bundle);
        outcomeChartFragment.setArguments(bundle);
        chartFragList.add(outcomeChartFragment);
        chartFragList.add(incomeChartFragment);
        chartVp.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        ChartVpAdapter chartVpAdapter = new ChartVpAdapter(this, chartFragList);
        chartVp.setAdapter(chartVpAdapter);

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.chart_iv_back:
                finish();
                break;
            case R.id.chart_btn_in:
                chartVp.setCurrentItem(1);
                setBtnStyle(1);
                break;
            case R.id.chart_btn_out:
                chartVp.setCurrentItem(0);
                setBtnStyle(0);
                break;
            case R.id.chart_iv_calendar:
                showDateDialog();
                break;

        }
    }

    // 改变颜色样式
    public void setBtnStyle(int kind) {
        if (kind==0) {
            outBtn.setBackgroundResource(R.drawable.tab_selected_bg);
            outBtn.setTextColor(Color.WHITE);
            inBtn.setBackgroundResource(R.drawable.tab_bg_shape);
            inBtn.setTextColor(Color.BLACK);
        }else {
            inBtn.setBackgroundResource(R.drawable.tab_selected_bg);
            inBtn.setTextColor(Color.WHITE);
            outBtn.setBackgroundResource(R.drawable.tab_bg_shape);
            outBtn.setTextColor(Color.BLACK);
        }
    }

    // 显示日期框
    public void showDateDialog() {
        SelectYearMonthDialog yearMonthDialog = new SelectYearMonthDialog(this);
        yearMonthDialog.show();
        yearMonthDialog.setOnEnsureListener(new SelectYearMonthDialog.OnEnsureListener() {
            @Override
            public void onEnsure(int year, int month) {
                initStaticsData(year,month);
                incomeChartFragment.setDate(year,month);
                outcomeChartFragment.setDate(year,month);


            }
        });
    }

    private void setVpListener() {
        chartVp.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                setBtnStyle(position);
            }
        });
    }
}