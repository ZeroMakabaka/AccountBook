package com.example.accountbook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;

import com.example.accountbook.frag_record.IncomeFragment;
import com.example.accountbook.frag_record.OutcomeFragment;
import com.example.accountbook.frag_record.adapter.RecordFragAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;

public class UpdateRecordActivity extends AppCompatActivity {

    ImageButton backHomeBtn;
    TabLayout tabLayout; // 滑动菜单栏
    ViewPager2 viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settingStatusBar();
        setContentView(R.layout.activity_record);

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

    public void init() {
        // 查找控件
        backHomeBtn = findViewById(R.id.record_iv_back);
        tabLayout = findViewById(R.id.record_tabs);
        viewPager = findViewById(R.id.record_vp);

        // 加载fragment
        initPager();


        backHomeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }

    // 加载fragment
    public void initPager() {
        // 创建支出和收入fragment并存放至list中
        ArrayList<Fragment> fragmentList = new ArrayList<>();
        OutcomeFragment outcomeFrag = new OutcomeFragment();
        IncomeFragment incomeFrag = new IncomeFragment();
        fragmentList.add(outcomeFrag);
        fragmentList.add(incomeFrag);

        Bundle bundle = getIntent().getExtras();
        outcomeFrag.setArguments(bundle);
        incomeFrag.setArguments(bundle);

        // 设置滑动方向
        viewPager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        // 设置适配器
        RecordFragAdapter recordFragAdapter = new RecordFragAdapter(this, fragmentList);
        viewPager.setAdapter(recordFragAdapter);
        // 关联tabLayout
        new TabLayoutMediator(tabLayout, viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setText(recordFragAdapter.getTabName(position));
            }
        }).attach();

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });

    }

}