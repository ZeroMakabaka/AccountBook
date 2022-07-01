package com.example.accountbook;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.accountbook.db.DBManager;

public class UserSettingActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView backIv;
    TextView clearAllTv,aboutTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settingStatusBar();
        setContentView(R.layout.activity_user_setting);
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
        backIv = findViewById(R.id.user_iv_back);
        clearAllTv = findViewById(R.id.user_clear_all_tv);
        aboutTv = findViewById(R.id.user_tv_about);
        backIv.setOnClickListener(this);
        clearAllTv.setOnClickListener(this);
        aboutTv.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.user_iv_back:
                finish();
                break;
            case R.id.user_clear_all_tv:
                showAlertClearDialog();
                break;
            case R.id.user_tv_about:
                Intent aboutIntent = new Intent(this,AboutActivity.class);
                startActivity(aboutIntent);
                break;
        }
    }

    public void showAlertClearDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("清空提示")
                .setMessage("亲，是否确定删库跑路？ 〒▽〒")
                .setNegativeButton("我再考虑考虑",null)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // 删库跑路
                        DBManager.clearAllRecords();
                        SharedPreferences.Editor editor = MainActivity.preferences.edit();
                        editor.putString("bMoney","0");
                        editor.commit();


                        // 操作成功提示
                        Toast.makeText(UserSettingActivity.this,"已清空所有数据！",Toast.LENGTH_SHORT).show();
                    }
                });
        builder.create().show();
    }
}