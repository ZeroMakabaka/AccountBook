package com.example.accountbook.frag_chart;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.accountbook.R;
import com.example.accountbook.db.DBManager;
import com.example.accountbook.db.entity.TypeAccountBean;
import com.example.accountbook.frag_chart.adapter.ChartItemAdapter;

import java.util.ArrayList;
import java.util.List;


public class OutcomeChartFragment extends BaseChartFragment {
    private int kind = 0;
    private ChartItemAdapter adapter;

    @Override
    public void initView() {
        super.initView();
        // 设置适配器
        getItemAdapter().setKind(kind);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData(getYear(),getMonth(),kind);
    }

    @Override
    public void setDate(int year, int month) {
        super.setDate(year,month);
        loadData(year,month,kind);
        //getItemAdapter().onRefresh(year,month,kind);
    }
}