package com.example.accountbook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.accountbook.activity_adater.MainAccountAdapter;
import com.example.accountbook.db.DBManager;
import com.example.accountbook.db.entity.AccountBean;
import com.example.accountbook.utils.MainBudgetDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 *      第三次修改：直接将headerView放在了layout布局文件里
 *      recyclerView不用载添加头布局，这里这样考虑是为了后面方便复用adapter，
 *
 *      ！！！！如果增加了头布局，一定要注意此时item的position与数据的position是不一样的
 *      AccountBean bean = list.get(position-1),不然数据会错
 *
 *      cursor 用完最好要关闭，但是这里好像不关闭也没影响？？？？
 *
 *
 *
 *      总体待改进部分：
 *      1、键盘对不齐，不管怎么修改gap，都无法对齐，待改
 *      2、对于数据的操作应该放在子线程中，然后主线程在处理UI
 *      3、应该增设用户注册和登录功能，保障账本的安全性（注册不知道要怎么实现）
 *      4、修改页面时，如果修改的是收入，选中的图标就会变灰，即不能显示
 *      5、增设图表分析，在实现其余绘图时，折线图和柱状图无法重新绘制，版本有问题？？？最新版本是3.1，目前用3.0，
 *       好像3.1后面需要自己写个适配器啥的，待查
 *      6、增设用户自定义部分，因为可能存在一些收支类型，app上并没有提供，增设一个让用户自定义的功能会好一点
 *      7、精度问题
 *      8、有些模拟器上，自定义的年月对话框会有一点问题，会把年份给隐藏掉，emmmmmmmm
 *
 *
 * ***/
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageButton searchBtn;
    private ImageView editBtn,analyseIv,historyIv,myIv;
    public List<AccountBean> recordDatas;
    RecyclerView todayRv;
    private MainAccountAdapter accountAdapter;
    private int year,month,day;
    TextView topOutTv,topInTv,topBudgetTv,topConTv,topConTvIn,countInTodayTv,countOutTodayTv;
    ImageView topShowIv;
    public static boolean isShow = true;

    /***
     *  这里用sharedPreferences存储预算数据：
     *  由于其配置信息并不多，如果采用数据库来存放并不划算，因为数据库连接跟操作等耗时大大影响了程序的效率，
     * **/
    public static SharedPreferences preferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settingStatusBar();
        preferences = getSharedPreferences("budget", Context.MODE_PRIVATE);
        setContentView(R.layout.activity_main);
        init();


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


    /*  加载数据    */
    @Override
    protected void onResume() {
        super.onResume();
        loadRecordData();
        // 重新更新头部数据
        showHeader();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void loadRecordData() {
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH)+1;
        day = calendar.get(Calendar.DAY_OF_MONTH);
        // 今日收支笔数
        List<AccountBean> outList = DBManager.getAccountListByDayKind(year, month, day, 0);
        List<AccountBean> inList = DBManager.getAccountListByDayKind(year, month, day, 1);
        countOutTodayTv.setText(outList.size()+"");
        countInTodayTv.setText(inList.size()+"");
        List<AccountBean> accountList = DBManager.getAccountList(year, month, day);
        recordDatas.clear();

        // 重新更新数据
        recordDatas.addAll(accountList);
        Log.e("TAG", "重新加载数据中 "+accountList.size() );
        accountAdapter.notifyDataSetChanged();
    }

    public void init(){

        // 按钮
        searchBtn = findViewById(R.id.main_iv_search);
        editBtn = findViewById(R.id.main_btn_edit);
        analyseIv = findViewById(R.id.main_biaoge_iv);
        historyIv = findViewById(R.id.main_biaodan_iv);
        myIv = findViewById(R.id.main_my_iv);

        // 头部
        topOutTv = findViewById(R.id.item_mainlv_top_tv_out);
        topInTv = findViewById(R.id.item_mainlv_top_tv_in);
        topShowIv = findViewById(R.id.item_mainlv_top_iv_hide);
        topBudgetTv = findViewById(R.id.item_mainlv_top_tv_budget);
        topConTv = findViewById(R.id.item_mainlv_top_tv_day_out);
        topConTvIn = findViewById(R.id.item_mainlv_top_tv_day_in);
        countInTodayTv = findViewById(R.id.item_mainlv_top_tv_count_in);
        countOutTodayTv = findViewById(R.id.item_mainlv_top_tv_count_out);

        // 显示头部信息
        showHeader();

        // 按钮监听
        searchBtn.setOnClickListener(this);

        editBtn.setOnClickListener(this);
        analyseIv.setOnClickListener(this);
        historyIv.setOnClickListener(this);
        myIv.setOnClickListener(this);

        todayRv = findViewById(R.id.main_lv);
        recordDatas = new ArrayList<>();
        // 设置适配器
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        accountAdapter = new MainAccountAdapter(this,recordDatas,false);

        /**
         *      这里需要注意长按点击与点击冲突，当长按监听返回true时，才不会触发点击事件
         * */
        // 监听
        accountAdapter.setOnItemLongClickListener(new MainAccountAdapter.OnItemLongClickListener() {
            @Override
            public boolean onLongClick(View view, AccountBean delBean,int position) {
                showDeleteDialog(delBean);
                return true;
            }
        });
        todayRv.setLayoutManager(layoutManager);
        todayRv.setAdapter(accountAdapter);

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.item_mainlv_top_iv_hide:
                showMoney();
            break;
            case R.id.item_mainlv_top_tv_budget:
                showBudgetDialog();
                break;
            case R.id.main_iv_search:
                Intent searchIntent = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(searchIntent);
                break;
            case R.id.main_btn_edit:
                Intent recordIntent = new Intent(MainActivity.this, RecordActivity.class);
                startActivity(recordIntent);
                break;
            case R.id.main_biaoge_iv:
                Intent chartIntent = new Intent(MainActivity.this,ChartAnalyseActivity.class);
                startActivity(chartIntent);
                break;
            case R.id.main_biaodan_iv:
                Intent historyIntent = new Intent(MainActivity.this, HistoryActivity.class);
                startActivity(historyIntent);
                break;
            case R.id.main_my_iv:
                Intent userIntent = new Intent(MainActivity.this, UserSettingActivity.class);
                startActivity(userIntent);
                break;
        }
    }



    // header栏
    public void showHeader() {
        // 获取今日支出和收入
        double outcomeDay = DBManager.getAllMoneyOneDayByKind(year,month,day,0);
        double incomeDay = DBManager.getAllMoneyOneDayByKind(year,month,day,1);
        String outStr = "￥ "+outcomeDay;
        String inStr = "￥ "+incomeDay;
        topConTv.setText(outStr);
        topConTvIn.setText(inStr);

        // 获取本月支出和收入
        double outcomeMonth = DBManager.getAllMoneyOneMonthByKind(year,month,0);
        double incomeMonth = DBManager.getAllMoneyOneMonthByKind(year,month,1);
        topOutTv.setText("￥ "+outcomeMonth);
        topInTv.setText("￥ "+incomeMonth);

        // 预算数据显示
        String budgetStr = preferences.getString("bMoney", "0");
        double budget = Double.parseDouble(budgetStr);
        if (budget==0) {
            topBudgetTv.setText("￥ 0");
        }else {
            double remainBudget = budget-outcomeMonth;
            topBudgetTv.setText("￥ " + remainBudget);
        }

        // 隐藏或显示按钮
        topShowIv.setOnClickListener(this);
        topBudgetTv.setOnClickListener(this);

    }

    /*
     *   显示或隐藏money
     * */
    public void showMoney(){
        if (isShow) {
            PasswordTransformationMethod passwordMethod = PasswordTransformationMethod.getInstance();
            topOutTv.setTransformationMethod(passwordMethod);
            topInTv.setTransformationMethod(passwordMethod);
            topBudgetTv.setTransformationMethod(passwordMethod);
            topConTv.setTransformationMethod(passwordMethod);
            topConTvIn.setTransformationMethod(passwordMethod);
            topShowIv.setImageResource(R.mipmap.ih_hide);
            // 更新adapter
            accountAdapter.setIsShow(true);
            accountAdapter.notifyDataSetChanged();
            isShow = false;
        }else {
            HideReturnsTransformationMethod hideMethod = HideReturnsTransformationMethod.getInstance();
            topOutTv.setTransformationMethod(hideMethod);
            topInTv.setTransformationMethod(hideMethod);
            topBudgetTv.setTransformationMethod(hideMethod);
            topConTv.setTransformationMethod(hideMethod);
            topConTvIn.setTransformationMethod(hideMethod);
            topShowIv.setImageResource(R.mipmap.ih_show);
            // 更新adapter
            accountAdapter.setIsShow(false);
            accountAdapter.notifyDataSetChanged();
            isShow = true;
        }
    }

    /*
     *   显示预算设置对话框
     * */
    public void showBudgetDialog(){
        MainBudgetDialog budgetDialog = new MainBudgetDialog(this);
        budgetDialog.show();
        budgetDialog.setViewHeight();

        budgetDialog.setOnEnsureListener(new MainBudgetDialog.OnEnsureListener() {
            @Override
            public void onEnsure(Double budget) {
                // 写入预算金额
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("bMoney",String.valueOf(budget));
                editor.commit();

                // 计算本月剩余金额
                double outcomeMonth = DBManager.getAllMoneyOneMonthByKind(year, month, 0);
                Double remainMonth = budget - outcomeMonth;
                topBudgetTv.setText("￥ "+remainMonth);
            }
        });
    }

    /*   弹出删除提示  */
    public void showDeleteDialog(final AccountBean delBean){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示信息").setMessage("确定要删除这条记录吗？")
                .setNegativeButton("取消",null)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // 执行从数据库中删除记录
                        DBManager.deleteItemRecordById(delBean.getaId());
                        recordDatas.remove(delBean);
                        accountAdapter.notifyDataSetChanged();
                        // 刷新页面数据
                        showHeader();
                    }
                });

        // 显示对话框
        builder.create().show();
    }
}