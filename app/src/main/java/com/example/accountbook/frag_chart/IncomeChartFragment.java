package com.example.accountbook.frag_chart;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.accountbook.R;
import com.example.accountbook.db.DBManager;
import com.example.accountbook.db.entity.TypeAccountBean;
import com.example.accountbook.frag_chart.adapter.ChartItemAdapter;

import java.util.ArrayList;
import java.util.List;

public class IncomeChartFragment extends BaseChartFragment {
    private int kind = 1;
    private ChartItemAdapter adapter;
    private int year,month;

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
      //  getItemAdapter().onRefresh(year,month,kind);


    }

    @Override
    public void setDate(int year, int month) {
        this.year = year;
        this.month = month;
        super.setDate(year,month);
        loadData(year,month,kind);
     //   getItemAdapter().onRefresh(year,month,kind);
    }

    /*    RecyclerView chartRv;
    ChartItemAdapter itemAdapter;
    private int year,month;
    private List<TypeAccountBean> mDatas;


    public IncomeChartFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_income_chart, container, false);
        chartRv = view.findViewById(R.id.chart_frag_rv);
        initView();
        return view;
    }

    public void initView() {

        Bundle bundle = getArguments();
        year = bundle.getInt("year");
        month = bundle.getInt("month");

        mDatas = new ArrayList<>();

        // 设置适配器
        itemAdapter = new ChartItemAdapter(getContext(),mDatas);
        chartRv.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        chartRv.setAdapter(itemAdapter);



    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData(year,month,1);
    }

    public void loadData(int year, int month,int kind) {
        List<TypeAccountBean> list = DBManager.getListByOneTypeYearMonth(year, month, kind);
        mDatas.clear();
        mDatas.addAll(list);
        itemAdapter.notifyDataSetChanged();
    }*/
}